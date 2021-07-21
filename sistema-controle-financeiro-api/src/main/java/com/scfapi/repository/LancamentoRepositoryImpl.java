package com.scfapi.repository;

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
import com.scfapi.model.Lancamento;
//Consultas customizadas com Criteria
public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery{
	
	@PersistenceContext
	private EntityManager entityManager;

	
	@Override
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
	private void adicionarestricoesDePaginacao(TypedQuery<Lancamento> typedQuery, Pageable pageable) {
		
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
}
