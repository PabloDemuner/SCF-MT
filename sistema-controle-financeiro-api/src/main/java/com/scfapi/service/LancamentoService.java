package com.scfapi.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.map.HashedMap;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfapi.dto.LancamentoEstatisticaPessoaDTO;
import com.scfapi.model.Lancamento;
import com.scfapi.model.Pessoa;
import com.scfapi.repository.LancamentoRepository;
import com.scfapi.repository.PessoaRepository;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
	
	public byte[] relatorioLancamentosPessoa(LocalDate dataInicio, LocalDate dataFim) throws Exception {
		
		List<LancamentoEstatisticaPessoaDTO> dadosLancamentoPessoa = lancamentoRepository.porPessoa(dataInicio, dataFim);
		
		Map<String, Object> parametros  = new HashMap<>();
		
		parametros.put("DT_INICIO", Date.valueOf(dataInicio));
		parametros.put("DT_FIM", Date.valueOf(dataFim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentosPorPessoa.jasper");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, 
				new JRBeanCollectionDataSource(dadosLancamentoPessoa));
		
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
}
