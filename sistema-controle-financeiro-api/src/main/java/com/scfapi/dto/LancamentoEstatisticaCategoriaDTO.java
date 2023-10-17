package com.scfapi.dto;

import java.math.BigDecimal;

import com.scfapi.model.Categoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoEstatisticaCategoriaDTO extends BaseDTO {

	private static final long serialVersionUID = 8693682223246662722L;
	
	private Categoria categoria;
	private BigDecimal total;

}
