package com.scfapi.controller.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

//Classe de filtro de Lancamento
public class LancamentoFilter {
	
	private String descricao;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimentoApartir;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimentoAte;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataVencimentoApartir() {
		return dataVencimentoApartir;
	}

	public void setDataVencimentoApartir(LocalDate dataVencimentoApartir) {
		this.dataVencimentoApartir = dataVencimentoApartir;
	}

	public LocalDate getDataVencimentoAte() {
		return dataVencimentoAte;
	}

	public void setDataVencimentoAte(LocalDate dataVencimentoAte) {
		this.dataVencimentoAte = dataVencimentoAte;
	}
	
	
}
