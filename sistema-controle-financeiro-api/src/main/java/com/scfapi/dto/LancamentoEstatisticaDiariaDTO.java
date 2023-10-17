package com.scfapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.scfapi.enums.TipoLancamento;

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
public class LancamentoEstatisticaDiariaDTO extends BaseDTO {

	private static final long serialVersionUID = -8469418545402856572L;

	private TipoLancamento tipo;
	private LocalDate dia;
	private BigDecimal total;
}
