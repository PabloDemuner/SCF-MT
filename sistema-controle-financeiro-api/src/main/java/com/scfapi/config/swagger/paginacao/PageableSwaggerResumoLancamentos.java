package com.scfapi.config.swagger.paginacao;

import java.util.List;

import com.scfapi.controller.filter.ResumoLancamento;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Resumo-Lancamentos")
public class PageableSwaggerResumoLancamentos {

	private List<ResumoLancamento> content;
}
