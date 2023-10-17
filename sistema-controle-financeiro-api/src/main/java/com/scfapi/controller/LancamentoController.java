package com.scfapi.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

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

import com.scfapi.controller.filter.LancamentoFilter;
import com.scfapi.controller.filter.ResumoLancamento;
import com.scfapi.dto.LancamentoEstatisticaCategoriaDTO;
import com.scfapi.dto.LancamentoEstatisticaDiariaDTO;
import com.scfapi.model.Lancamento;
import com.scfapi.repository.LancamentoRepository;
import com.scfapi.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;

	@GetMapping("/estatisticas/por-categoria")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public List<LancamentoEstatisticaCategoriaDTO> porCategoria() {
		return this.lancamentoRepository.porCategoria(LocalDate.now());
	}
	
	@GetMapping("/estatisticas/por-dia")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public List<LancamentoEstatisticaDiariaDTO> porDia() {
		return this.lancamentoRepository.porDia(LocalDate.now());
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}
	
	@GetMapping(params = "resumo")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.resumir(lancamentoFilter, pageable);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public ResponseEntity<Lancamento> buscaPorId(@PathVariable Long id) {
		Lancamento lancamento = lancamentoRepository.findById(id).orElse(null);
		return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
	}
	
	// @ResponseStatus(value = HttpStatus.CREATED)
		@PostMapping
		@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
		public ResponseEntity<Lancamento> adicionar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
			Lancamento LancamentoSalva = lancamentoService.salvar(lancamento);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(LancamentoSalva.getId())
					.toUri();
			response.setHeader("Location", uri.toASCIIString());

			return ResponseEntity.created(uri).body(LancamentoSalva);
		}
		
		@ResponseStatus(HttpStatus.NO_CONTENT)
		@DeleteMapping("/{id}")
		@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO')")
		public void remover(@PathVariable Long id) {
			lancamentoRepository.deleteById(id);
		}

		@PutMapping("/{id}")
		@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
		public ResponseEntity<Lancamento> atualizar(@PathVariable Long id, @Valid @RequestBody Lancamento lancamento) {
			try {
				Lancamento lancamentoSalvo = lancamentoService.atualizar(id, lancamento);
				return ResponseEntity.ok(lancamentoSalvo);
			} catch (IllegalArgumentException e) {
				return ResponseEntity.notFound().build();
			}
		}
}
