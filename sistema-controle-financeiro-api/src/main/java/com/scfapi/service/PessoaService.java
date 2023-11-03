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
	
	public Pessoa adicionar(Pessoa pessoa) {
		pessoa.getContatos().forEach(contato -> contato.setPessoa(pessoa));
		return pessoaRepository.save(pessoa);
	}

	public Pessoa atualizar(@PathVariable Long id, @RequestBody Pessoa pessoa) {
		Pessoa pessoaSalva = pessoaRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		pessoa.getContatos().forEach(contato -> contato.setPessoa(pessoa));
		
		BeanUtils.copyProperties(pessoa, pessoaSalva, "id");
		return pessoaRepository.save(pessoaSalva);
	}

	public void atualizarPropriedadeAtiva(Long id, Boolean ativo) {
		Pessoa pessoaSalva = buscaPessoaPeloId(id);
		pessoaSalva.setAtivo(ativo);
		pessoaRepository.save(pessoaSalva);
	}

	public Pessoa buscaPessoaPeloId(Long id) {
		Pessoa pessoaSalva = pessoaRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		BeanUtils.copyProperties(id, pessoaSalva, "id");
		return pessoaRepository.save(pessoaSalva);
	}

}
