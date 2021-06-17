package com.scfapi.controller;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.scfapi.model.Pessoa;
import com.scfapi.repository.PessoaRepository;
import com.scfapi.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaService pessoaService;

	@GetMapping
	public List<Pessoa> listar() {
		return pessoaRepository.findAll();
	}

	// @ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping
	private ResponseEntity<Pessoa> adicionar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(pessoaSalva.getId())
				.toUri();
		response.setHeader("Location", uri.toASCIIString());

		return ResponseEntity.created(uri).body(pessoaSalva);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pessoa> buscaId(@PathVariable Long id) {
		Pessoa pessoa = pessoaRepository.findById(id).orElse(null);
		return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void remover(@PathVariable Long id) {
		pessoaRepository.deleteById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Pessoa> atualizar(@PathVariable Long id, @Valid @RequestBody Pessoa pessoa) {
		Pessoa pessoaSalva = pessoaService.atualizar(id, pessoa);
		return ResponseEntity.ok(pessoaSalva);
	}
	
	@PutMapping("/{id}/ativo")
	public void atualizarPropriedadeAtiva(@PathVariable Long id, @RequestBody Boolean ativo) {
		pessoaService.atualizarPropriedadeAtiva(id, ativo);
	}
}