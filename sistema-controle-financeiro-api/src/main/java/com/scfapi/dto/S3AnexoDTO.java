package com.scfapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class S3AnexoDTO extends BaseDTO{
	
	private static final long serialVersionUID = -8417706557506190443L;
	
	private String nome;
	private String url;
}
