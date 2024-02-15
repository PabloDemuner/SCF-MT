package com.scfapi.config.openapi;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.Response;

@Configuration
public class SpringFoxExceptionsConfig {

	public List<Response> globalGetResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder().code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
						.description("Erro interno do Servidor").build(),
				new ResponseBuilder().code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
						.description("Recurso não possui representação que pode ser aceita pelo consumidor").build(),
				new ResponseBuilder().code(String.valueOf(HttpStatus.FORBIDDEN.value()))
						.description("Requisição não autorizada").build());
	}

	public List<Response> globalPostPutResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder().code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
						.description("Requisição inválida (erro do cliente)").build(),
				new ResponseBuilder().code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
						.description("Erro interno no servidor").build(),
				new ResponseBuilder().code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
						.description("Recurso não possui representação que poderia ser aceita pelo consumidor").build(),
				new ResponseBuilder().code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
						.description("Requisição recusada porque o corpo está em um formato não suportado").build(),
				new ResponseBuilder().code(String.valueOf(HttpStatus.FORBIDDEN.value()))
						.description("Requisição não autorizada").build());
	}

	public List<Response> globalDeleteResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder().code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
						.description("Requisição inválida (erro do cliente)").build(),
				new ResponseBuilder().code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
						.description("Erro interno no servidor").build(),
				new ResponseBuilder().code(String.valueOf(HttpStatus.FORBIDDEN.value()))
						.description("Requisição não autorizada").build());
	}
}
