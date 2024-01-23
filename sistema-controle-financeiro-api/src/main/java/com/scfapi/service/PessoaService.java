package com.scfapi.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.gson.Gson;
import com.scfapi.dto.EnderecoViaCepDTO;
import com.scfapi.dto.PessoaDTO;
import com.scfapi.mapper.ContatoMapper;
import com.scfapi.mapper.PessoaMapper;
import com.scfapi.model.Contato;
import com.scfapi.model.Pessoa;
import com.scfapi.repository.ContatoRepository;
import com.scfapi.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaMapper pessoaMapper;
	
	@Autowired
	private ContatoMapper contatoMapper;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ContatoRepository contatoRepository;
	
	public PessoaDTO adicionar(PessoaDTO pessoa) {
		Pessoa pessoaEntity = new Pessoa();
		
		pessoaEntity = pessoaMapper.pessoaDTOtoPessoa(pessoa);
		Pessoa pessoaSalva = pessoaRepository.save(pessoaEntity);
		
		List<Contato> contato = contatoMapper.map(pessoa.getContatos());
		contato.forEach(contatos -> contatos.setPessoa(pessoaSalva));
		contatoRepository.saveAll(contato);
		
		return pessoaMapper.pessoaToPessoaDTO(pessoaSalva);
	}

	public PessoaDTO atualizar(@PathVariable Long id, @RequestBody PessoaDTO pessoa) {
		Pessoa pessoaEntity = new Pessoa();
		
		pessoaEntity = pessoaMapper.pessoaDTOtoPessoa(pessoa);
		Pessoa pessoaSalva = pessoaRepository.save(pessoaEntity);
		
		pessoaSalva.getContatos().clear();
		List<Contato> contato = contatoMapper.map(pessoa.getContatos());
		contato.forEach(contatos -> contatos.setPessoa(pessoaSalva));
		contatoRepository.saveAll(contato);
		
		BeanUtils.copyProperties(pessoa, pessoaSalva, "id", "contato");
		return pessoaMapper.pessoaToPessoaDTO(pessoaSalva);
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
	
	public EnderecoViaCepDTO buscaEndereco(String cep) throws Exception {
		
		URL urlApi = new URL("https://viacep.com.br/ws/"+cep+"/json/");
		
		URLConnection connection = urlApi.openConnection();
		InputStream is = connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		
		String cepApi = "";
		StringBuilder jsonCep = new StringBuilder();
		
		while ((cepApi = br.readLine()) != null) {
			jsonCep.append(cepApi);
		}
		
		EnderecoViaCepDTO enderecoApi = new Gson().fromJson(jsonCep.toString(), EnderecoViaCepDTO.class);
		
		return enderecoApi;
	}

}
