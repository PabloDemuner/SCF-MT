package com.scfapi.config.swagger.paginacao;

import java.time.LocalDate;
import java.util.List;

import com.scfapi.model.Lancamento;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Lancamentos")
public class PageableSwaggerLancamentos {
	
	private List<Lancamento> content;
	
	@ApiModelProperty(example = "Teste", value = "Descrição do lançamento")
	private String descricao;

	@ApiModelProperty(example = "2023-01-01", value = "Data vencimento inicial")
	private LocalDate dataVencimentoApartir;

	@ApiModelProperty(example = "2023-12-30", value = "Data vencimento final")
	private LocalDate dataVencimentoAte;

	@ApiModelProperty(example = "0", value = "Número da página(começa em 0)")
	private int page;

	@ApiModelProperty(example = "10", value = "Quantidade de elementos por página")
	private int size;

	// @ApiModelProperty(example = "nome,asc", value = "Tipo de ordenação")
	// private List<String> sort;
}
