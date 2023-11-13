package com.scfapi.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

//Classe de configuração do protocolo HTTPS
@ConfigurationProperties(prefix = "controlefinanceiro")
public class ScfApiProperty {

	private String origemPermitida = "http://localhost:4200";
	
	private final Seguranca seguranca = new Seguranca();
	
	private final Mail mail = new Mail();
	
	private final S3Key s3 = new S3Key();
	
	public Mail getMail() {
		return mail;
	}
	
	public S3Key getS3Key() {
		return s3;
	}

	public String getOrigemPermitida() {
		return origemPermitida;
	}

	public void setOrigemPermitida(String origemPermitida) {
		this.origemPermitida = origemPermitida;
	}

	public Seguranca getSeguranca() {
		return seguranca;
	}

	public static class Seguranca {

		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
	}
	
	@Getter
	@Setter
	public static class Mail {
		private String host;
		private Integer port;
		private String username;
		private String password;
	}
	
	@Getter
	@Setter
	public static class S3Key {
		private String accessKeyId;
		private String secretAccessKey;
	}
}
