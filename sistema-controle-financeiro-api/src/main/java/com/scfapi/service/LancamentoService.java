package com.scfapi.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
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
	
	public Lancamento atualizar(Long id, Lancamento lancamento) {
		Lancamento lancamentoSalvo = existeLancamento(id);
		if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			existePessoa(lancamento);
		}

		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

		return lancamentoRepository.save(lancamentoSalvo);
	}
	
	private void existePessoa(Lancamento lancamento) {
		Optional<Pessoa> pessoa = null;
		if (lancamento.getPessoa().getId() != null) {
			pessoa = pessoaRepository.findById(lancamento.getPessoa().getId());
		}

		if (pessoa.isEmpty() || pessoa.get().isPresent()) {
			throw new IllegalArgumentException();
		}
    }

	
	private Lancamento existeLancamento(Long id) {
		return lancamentoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
    }
	
}
