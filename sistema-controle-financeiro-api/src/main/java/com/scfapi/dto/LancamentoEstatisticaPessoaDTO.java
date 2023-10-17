package com.scfapi.dto;

import java.math.BigDecimal;

import com.scfapi.enums.TipoLancamento;
import com.scfapi.model.Pessoa;

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
public class LancamentoEstatisticaPessoaDTO extends BaseDTO {

	private static final long serialVersionUID = -2829253446241978292L;
	
	private TipoLancamento tipo;
	private Pessoa pessoa;
	private BigDecimal total;
}
