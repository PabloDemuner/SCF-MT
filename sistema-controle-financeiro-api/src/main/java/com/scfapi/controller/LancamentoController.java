package com.scfapi.controller;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.scfapi.config.aws.S3Service;
import com.scfapi.controller.filter.LancamentoFilter;
import com.scfapi.controller.filter.ResumoLancamento;
import com.scfapi.dto.LancamentoEstatisticaCategoriaDTO;
import com.scfapi.dto.LancamentoEstatisticaDiariaDTO;
import com.scfapi.dto.S3AnexoDTO;
import com.scfapi.model.Lancamento;
import com.scfapi.repository.LancamentoRepository;
import com.scfapi.service.LancamentoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Lancamentos")
@RequestMapping("/lancamentos")
public class LancamentoController {

	private static final String CAMINHOARQUIVO = "F:\\Anexos-API-SCF\\";

	@Autowired
	private S3Service s3Service;

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@GetMapping(path = "/estatisticas/por-categoria", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public List<LancamentoEstatisticaCategoriaDTO> porCategoria() {
		return lancamentoRepository.porCategoria(LocalDate.now());
	}

	@GetMapping(path = "/estatisticas/por-dia", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public List<LancamentoEstatisticaDiariaDTO> porDia() {
		return lancamentoRepository.porDia(LocalDate.now());
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}

	@GetMapping(params = "resumo", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Resumo de lançamentos", hidden = true)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.resumir(lancamentoFilter, pageable);
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public ResponseEntity<Lancamento> buscaPorId(@PathVariable Long id) {
		Lancamento lancamento = lancamentoRepository.findById(id).orElse(null);
		return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
	}

	// @ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public ResponseEntity<Lancamento> adicionar(@Valid @RequestBody Lancamento lancamento,
			HttpServletResponse response) {
		Lancamento LancamentoSalva = lancamentoService.salvar(lancamento);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(LancamentoSalva.getId()).toUri();
		
		response.setHeader("Location", uri.toASCIIString());

		return ResponseEntity.created(uri).body(LancamentoSalva);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO')")
	public void remover(@PathVariable Long id) {
		lancamentoRepository.deleteById(id);
	}

	@PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public ResponseEntity<Lancamento> atualizar(@PathVariable Long id, @Valid @RequestBody Lancamento lancamento) {
		try {
			Lancamento lancamentoSalvo = lancamentoService.atualizar(id, lancamento);
			return ResponseEntity.ok(lancamentoSalvo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping(path = "/relatorios/por-pessoa", produces = MediaType.APPLICATION_PDF_VALUE)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public ResponseEntity<byte[]> relatorioLancamentosPessoa(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicio,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFim) throws Exception {

		byte[] relatorio = lancamentoService.relatorioLancamentosPessoa(dataInicio, dataFim);

		return ResponseEntity.ok()
				.header("Content-Disposition", "attachment; filename=relatorioLancamentos.pdf")
				//.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}

	@PostMapping(path = "/anexo")
	@ApiOperation(value = "Anexos do cadastro de lançamentos", hidden = true)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public S3AnexoDTO uploadAnexo(@RequestParam MultipartFile anexo) throws IOException {
		/*
		 * OutputStream outputStream = new FileOutputStream(CAMINHOARQUIVO +
		 * anexo.getOriginalFilename()); System.out.println("Caminho do arquivo " +
		 * CAMINHOARQUIVO + "Arquivo " + anexo.getOriginalFilename());
		 * outputStream.write(anexo.getBytes()); outputStream.close();
		 */

		String arquivo = s3Service.salvarAnexo(anexo);
		return new S3AnexoDTO(arquivo, s3Service.configuraUrl(arquivo));
	}

}
