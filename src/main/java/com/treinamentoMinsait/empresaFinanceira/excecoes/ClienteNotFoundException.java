package com.treinamentoMinsait.empresaFinanceira.excecoes;

public class ClienteNotFoundException extends Exception {
	private static final long serialVersionUID = 5L;
	
	public ClienteNotFoundException(String cpf) {
		super(String.format("Não foi possível encontrar um cliente com o CPF %s.", cpf));
	}
}
