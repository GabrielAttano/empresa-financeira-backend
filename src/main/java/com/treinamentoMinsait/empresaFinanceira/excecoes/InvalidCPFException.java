package com.treinamentoMinsait.empresaFinanceira.excecoes;

public class InvalidCPFException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidCPFException(String CPF) {
		super(String.format("CPF '%s' inválido. O CPF deve possuir 11 digitos, sem pontuações.", CPF));
	}
}
