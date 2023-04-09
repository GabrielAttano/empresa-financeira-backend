package com.treinamentoMinsait.empresaFinanceira.excecoes;

public class InsufficientRendaMensalException extends Exception {
	private static final long serialVersionUID = 7L;
	
	public InsufficientRendaMensalException() {
		super("O cliente não pode criar um novo empréstimo pois a soma dos valores finais de seus empréstimos supera sua renda mensal multiplicada por dez.");
	}
}
