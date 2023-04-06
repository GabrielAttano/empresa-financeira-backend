package com.treinamentoMinsait.empresaFinanceira.DTO;

import java.math.BigDecimal;

import com.treinamentoMinsait.empresaFinanceira.entity.Cliente;
import com.treinamentoMinsait.empresaFinanceira.entity.Endereco;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteDTO {
	private Long id;
	private String nome;
	private String telefone;
	private BigDecimal rendaMensal;
	private Endereco endereco;
	
	public ClienteDTO(Cliente cliente) {
		this.id = cliente.getId();
		this.nome = cliente.getNome();
		this.telefone = cliente.getTelefone();
		this.rendaMensal = cliente.getRendaMensal();
		this.endereco = cliente.getEndereco();
	}
}
