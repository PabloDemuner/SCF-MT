package com.scfapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scfapi.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery{

}
