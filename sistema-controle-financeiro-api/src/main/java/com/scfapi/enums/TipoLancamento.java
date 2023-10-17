package com.scfapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoLancamento {

	RECEITA("Resceita"), 
	DESPESA("Despesa");
	
	private final String descricao;
	
}
