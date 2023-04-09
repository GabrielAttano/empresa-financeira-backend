package com.treinamentoMinsait.empresaFinanceira.excecoes;

public class InvalidEmprestimoGetException extends Exception {
	private static final long serialVersionUID = 9L;
	
	public InvalidEmprestimoGetException(String cpf, Long id) {
		super(String.format("O empréstimo com id '%d' não pertence ao cliente com cpf '%s'", id, cpf));
	}
}
