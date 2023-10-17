package com.scfapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scfapi.controller.filter.LancamentoFilter;
import com.scfapi.controller.filter.ResumoLancamento;
import com.scfapi.dto.LancamentoEstatisticaCategoriaDTO;
import com.scfapi.dto.LancamentoEstatisticaDiariaDTO;
import com.scfapi.dto.LancamentoEstatisticaPessoaDTO;
import com.scfapi.model.Lancamento;

public interface LancamentoRepositoryQuery {

	public List<LancamentoEstatisticaCategoriaDTO> porCategoria(LocalDate mesReferencia);
	public List<LancamentoEstatisticaDiariaDTO> porDia(LocalDate mesReferencia);
	public List<LancamentoEstatisticaPessoaDTO> porPessoa(LocalDate dataInicio, LocalDate dataFim);
	
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir (LancamentoFilter lancamentoFilter, Pageable pageable);
}
