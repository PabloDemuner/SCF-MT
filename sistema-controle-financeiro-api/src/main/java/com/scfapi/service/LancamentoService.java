package com.scfapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfapi.model.Lancamento;
import com.scfapi.model.Pessoa;
import com.scfapi.repository.LancamentoRepository;
import com.scfapi.repository.PessoaRepository;

@Service
public class LancamentoService {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	public Lancamento salvar(Lancamento lancamento) {
		Optional<Pessoa> pessoaSalva = pessoaRepository
				.findById(Optional.ofNullable(lancamento.getPessoa().getId()).orElse(0L));
		if (!pessoaSalva.isPresent() || !pessoaSalva.get().getAtivo()) {
			throw new PessoaInexistenteOuInativoException();
		}
		return lancamentoRepository.save(lancamento);
	}
}
