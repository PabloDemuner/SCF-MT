package com.scfapi.config.swagger.paginacao;

import java.util.List;

import com.scfapi.model.Pessoa;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Pessoas")
public class PageableSwaggerPessoas {
	
	private List<Pessoa> content;

	@ApiModelProperty(example = "0", value = "Número da página(começa em 0)")
	private int page;

	@ApiModelProperty(example = "10", value = "Quantidade de elementos por página")
	private int size;
}
