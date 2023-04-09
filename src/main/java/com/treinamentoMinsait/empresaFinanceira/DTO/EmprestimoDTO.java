package com.treinamentoMinsait.empresaFinanceira.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.treinamentoMinsait.empresaFinanceira.entity.Emprestimo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmprestimoDTO {
	private Long id;
	private BigDecimal valorInicial;
	private BigDecimal valorFinal;
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	
	public EmprestimoDTO(Emprestimo emprestimo) {
		this.id = emprestimo.getId();
		this.valorInicial = emprestimo.getValorInicial();
		this.valorFinal = emprestimo.getValorFinal();
		this.dataInicial = emprestimo.getDataInicial();
		this.dataFinal = emprestimo.getDataFinal();
	}
}