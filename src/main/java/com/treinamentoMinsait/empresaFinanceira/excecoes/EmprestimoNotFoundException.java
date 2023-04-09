package com.treinamentoMinsait.empresaFinanceira.excecoes;

public class EmprestimoNotFoundException extends Exception {
	private static final long serialVersionUID = 8L;
	
	public EmprestimoNotFoundException(Long id) {
		super(String.format("Não foi possível encontrar um empréstimo com o id '%d'. ", id));
	}
}
