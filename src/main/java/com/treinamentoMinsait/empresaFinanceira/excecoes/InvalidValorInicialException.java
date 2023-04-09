package com.treinamentoMinsait.empresaFinanceira.excecoes;

import java.math.BigDecimal;

public class InvalidValorInicialException extends Exception {
	
	private static final long serialVersionUID = 6L;
	
	public InvalidValorInicialException(BigDecimal valorInicial) {
		super(String.format("O valor inicial %s é inválido. Deve ser maior que 0,00.", valorInicial.toString()));
	}

}
