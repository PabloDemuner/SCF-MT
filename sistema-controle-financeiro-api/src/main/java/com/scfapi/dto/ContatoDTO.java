package com.scfapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContatoDTO extends BaseDTO {

	private static final long serialVersionUID = 807037002994711424L;
	
	private Long id;
	private String nome;
	private String email;
	private String telefone;
	private PessoaDTO pessoa;
}
