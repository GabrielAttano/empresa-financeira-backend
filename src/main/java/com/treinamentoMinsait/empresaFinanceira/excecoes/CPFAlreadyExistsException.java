package com.treinamentoMinsait.empresaFinanceira.excecoes;

public class CPFAlreadyExistsException extends Exception {
	
	private static final long serialVersionUID = 3L;
	
	public CPFAlreadyExistsException(String CPF) {
		super(String.format("O CPF %s já está registrado.", CPF));
	}
}
