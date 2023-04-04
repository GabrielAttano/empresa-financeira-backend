package com.treinamentoMinsait.empresaFinanceira.excecoes;

public class InvalidCEPException extends Exception {
	
	private static final long serialVersionUID = 4L;
	
	public InvalidCEPException(String CEP) {
		super(String.format("CEP '%s' inválido. O CEP deve estar no formato 'XXXXX-XXX', onde cada X é um digito.", CEP));
	}
}
