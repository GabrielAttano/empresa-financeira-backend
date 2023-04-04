package com.treinamentoMinsait.empresaFinanceira.excecoes;

public class InvalidTelefoneException extends Exception {
	
	private static final long serialVersionUID = 2L;
	
	public InvalidTelefoneException(String telefone) {
		super(String.format("Telefone '%s' inválido. O telefone deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX, onde cada X é um dígito.", telefone));
	}
}
