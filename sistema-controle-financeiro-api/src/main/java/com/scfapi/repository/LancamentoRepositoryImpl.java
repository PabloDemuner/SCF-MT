package com.scfapi.repository;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import com.scfapi.controller.filter.LancamentoFilter;
import com.scfapi.controller.filter.ResumoLancamento;
import com.scfapi.dto.LancamentoEstatisticaCategoriaDTO;
import com.scfapi.dto.LancamentoEstatisticaDiariaDTO;
import com.scfapi.dto.LancamentoEstatisticaPessoaDTO;
import com.scfapi.model.Lancamento;

//Consultas customizadas com Criteria
public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery{
	
	@PersistenceContext
	private EntityManager entityManager;

	
	@Override //Filtro de Lancamentos
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteriaQuery = criteriaBuilder.createQuery(Lancamento.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		//Cria Restrições
		Predicate[] predicates = criarRestricoes(lancamentoFilter, criteriaBuilder, root);
		criteriaQuery.where(predicates);
		
		TypedQuery<Lancamento> typedQuery = entityManager.createQuery(criteriaQuery);
		
		adicionarestricoesDePaginacao(typedQuery, pageable);
		
		return new PageImpl<>(typedQuery.getResultList(), pageable, totalPaginas(lancamentoFilter));
	}
	
	@Override // Resumo de lancamentos
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteriaQuery = criteriaBuilder.createQuery(ResumoLancamento.class);

		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

		criteriaQuery.select(criteriaBuilder.construct(ResumoLancamento.class
				, root.get("id")
				, root.get("descricao")
				,root.get("dataVencimento")
				, root.get("dataPagamento")
				, root.get("valor")
				, root.get("tipo")
				,root.get("categoria").get("nome")
				, root.get("pessoa").get("nome")));
				

		// Cria Restrições
		Predicate[] predicates = criarRestricoes(lancamentoFilter, criteriaBuilder, root);
		criteriaQuery.where(predicates);

		TypedQuery<ResumoLancamento> typedQuery = entityManager.createQuery(criteriaQuery);

		adicionarestricoesDePaginacao(typedQuery, pageable);

		return new PageImpl<>(typedQuery.getResultList(), pageable, totalPaginas(lancamentoFilter));
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder criteriaBuilder,
			Root<Lancamento> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if(!ObjectUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(criteriaBuilder.like(
					criteriaBuilder.lower(root.get("descricao")),"%" + lancamentoFilter.getDescricao()
					.toLowerCase() + "%" ));
		}
		
		if(lancamentoFilter.getDataVencimentoApartir() != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataVencimento"), 
					lancamentoFilter.getDataVencimentoApartir()));
		}
		
		if(lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataVencimento"), 
					lancamentoFilter.getDataVencimentoAte()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	//Efetua contagem de regsitros por página(Pageable)
	private void adicionarestricoesDePaginacao(TypedQuery<?> typedQuery, Pageable pageable) {
		
		int paginaAtual = pageable.getPageNumber();
		int totalResgistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalResgistrosPorPagina;
		
		typedQuery.setFirstResult(primeiroRegistroDaPagina);
		typedQuery.setMaxResults(totalResgistrosPorPagina);
	}
	
	//Pageable com Criteria
	private long totalPaginas(LancamentoFilter lancamentoFilter) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, criteriaBuilder, root);
		criteriaQuery.where(predicates);
		
		criteriaQuery.select(criteriaBuilder.count(root));
		
		return entityManager.createQuery(criteriaQuery).getSingleResult();
	}

	@Override
	public List<LancamentoEstatisticaCategoriaDTO> porCategoria(LocalDate mesReferencia) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaCategoriaDTO> criteriaQuery = criteriaBuilder
				.createQuery(LancamentoEstatisticaCategoriaDTO.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoriaDTO.class, 
				root.get("categoria"),
				criteriaBuilder.sum(root.get("valor"))));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get("dataVencimento"),
						primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(root.get("dataVencimento"),
						ultimoDia));
		
		criteriaQuery.groupBy(root.get("categoria"));
		
		TypedQuery<LancamentoEstatisticaCategoriaDTO> typedQuery = entityManager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}

	@Override
	public List<LancamentoEstatisticaDiariaDTO> porDia(LocalDate mesReferencia) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaDiariaDTO> criteriaQuery = criteriaBuilder
				.createQuery(LancamentoEstatisticaDiariaDTO.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaDiariaDTO.class, 
				root.get("tipo"),
				root.get("dataVencimento"),
				criteriaBuilder.sum(root.get("valor"))));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get("dataVencimento"),
						primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(root.get("dataVencimento"),
						ultimoDia));
		
		criteriaQuery.groupBy(root.get("tipo"), root.get("dataVencimento"));
		
		TypedQuery<LancamentoEstatisticaDiariaDTO> typedQuery = entityManager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}

	@Override
	public List<LancamentoEstatisticaPessoaDTO> porPessoa(LocalDate dataInicio, LocalDate dataFim) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaPessoaDTO> criteriaQuery = criteriaBuilder
				.createQuery(LancamentoEstatisticaPessoaDTO.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaPessoaDTO.class, 
				root.get("tipo"),
				root.get("pessoa"),
				criteriaBuilder.sum(root.get("valor"))));
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get("dataVencimento"),
						dataInicio),
				criteriaBuilder.lessThanOrEqualTo(root.get("dataVencimento"),
						dataFim));
		
		criteriaQuery.groupBy(root.get("tipo"), root.get("pessoa"));
		
		TypedQuery<LancamentoEstatisticaPessoaDTO> typedQuery = entityManager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}
}
