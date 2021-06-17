package com.scfapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.scfapi.model.Pessoa;
import com.scfapi.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	public Pessoa atualizar(@PathVariable Long id, @RequestBody Pessoa pessoa) {
		Pessoa pessoaSalva = this.pessoaRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		BeanUtils.copyProperties(pessoa, pessoaSalva, "id");
		return this.pessoaRepository.save(pessoaSalva);
	}

	public void atualizarPropriedadeAtiva(Long id, Boolean ativo) {
		Pessoa pessoaSalva = buscaPessoaPeloCogido(id);
		pessoaSalva.setAtivo(ativo);
		pessoaRepository.save(pessoaSalva);
	}

	private Pessoa buscaPessoaPeloCogido(Long id) {
		Pessoa pessoaSalva = this.pessoaRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		BeanUtils.copyProperties(id, pessoaSalva, "id");
		return this.pessoaRepository.save(pessoaSalva);
	}

}
