package com.scfapi.repository;

import java.util.List;

import com.scfapi.controller.filter.LancamentoFilter;
import com.scfapi.model.Lancamento;

public interface LancamentoRepositoryQuery {

	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);
}
