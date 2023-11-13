package com.scfapi.dto;

import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class PessoaDTO extends BaseDTO {
	static final long serialVersionUID = 3140905936957331287L;
	
	private Long id;
	private String nome;
	private EnderecoDTO endereco;
	private Boolean ativo;
	private List<ContatoDTO> contatos;

	/*@JsonIgnore @Transient Para o Hibernat e o Jackson não entender como uma propriedade
	 * para não serealizar ou salvar em Banco
	 */
	@JsonIgnore
	@Transient
	public boolean isPresent() {
		return !this.ativo;
	}
}
