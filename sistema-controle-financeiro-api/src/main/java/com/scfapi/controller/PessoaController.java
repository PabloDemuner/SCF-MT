package com.scfapi.controller;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.scfapi.dto.EnderecoViaCepDTO;
import com.scfapi.dto.PessoaDTO;
import com.scfapi.model.Lancamento;
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
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA')")
	public Page<Pessoa> pesquisar(Pageable pageable) {
		return pessoaRepository.findAll(pageable);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA')")
	public ResponseEntity<PessoaDTO> adicionar(@Valid @RequestBody PessoaDTO pessoa, HttpServletResponse response) {
		PessoaDTO pessoaSalva = pessoaService.adicionar(pessoa);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(pessoaSalva.getId())
				.toUri();
		response.setHeader("Location", uri.toASCIIString());

		return ResponseEntity.created(uri).body(pessoaSalva);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA')")
	public ResponseEntity<Pessoa> buscaId(@PathVariable Long id) {
		Pessoa pessoa = pessoaRepository.findById(id).orElse(null);
		return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA')")
	public void remover(@PathVariable Long id) {
		pessoaRepository.deleteById(id);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA')")
	public ResponseEntity<PessoaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PessoaDTO pessoa) {
		PessoaDTO pessoaSalva = pessoaService.atualizar(id, pessoa);
		return ResponseEntity.ok(pessoaSalva);
	}
	
	@PutMapping("/{id}/ativo")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA')")
	public void atualizarPropriedadeAtiva(@PathVariable Long id, @RequestBody Boolean ativo) {
		pessoaService.atualizarPropriedadeAtiva(id, ativo);
	}
	
	@GetMapping("/endereco/{cep}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA')")
	public ResponseEntity<EnderecoViaCepDTO> buscaEndereco(@PathVariable String cep) throws Exception {
		EnderecoViaCepDTO endereco = pessoaService.buscaEndereco(cep);
		return endereco != null ? ResponseEntity.ok(endereco) : ResponseEntity.notFound().build();
	}
}