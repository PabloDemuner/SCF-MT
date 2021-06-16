package com.scfapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scfapi.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
