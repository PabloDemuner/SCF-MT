package com.scfapi.repository.listener;

import javax.persistence.PostLoad;

import org.springframework.util.StringUtils;

import com.scfapi.SistemaControleFinanceiroApiApplication;
import com.scfapi.config.aws.S3Service;
import com.scfapi.model.Lancamento;

public class LancamentoAnexoListener {

	@PostLoad
	public void criaUrlAnexo(Lancamento lancamento) {
		if (StringUtils.hasText(lancamento.getAnexo())) {
			S3Service s3Service = SistemaControleFinanceiroApiApplication.getBean(S3Service.class);
			lancamento.setUrlAnexo(s3Service.configuraUrl(lancamento.getAnexo()));
		}
	}
}
