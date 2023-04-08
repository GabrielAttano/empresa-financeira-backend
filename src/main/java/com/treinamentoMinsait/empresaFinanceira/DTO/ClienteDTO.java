package com.treinamentoMinsait.empresaFinanceira.DTO;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import com.treinamentoMinsait.empresaFinanceira.entity.Cliente;
import com.treinamentoMinsait.empresaFinanceira.entity.Endereco;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteDTO {
	private Long id;
	private String nome;
	private String telefone;
	@Min(value=0) private BigDecimal rendaMensal;
	private EnderecoDTO endereco;
	
	public ClienteDTO() {
		
	}
	
	public ClienteDTO(Cliente cliente) {
		this.id = cliente.getId();
		this.nome = cliente.getNome();
		this.telefone = cliente.getTelefone();
		this.rendaMensal = cliente.getRendaMensal();
		EnderecoDTO enderecoDTO = new EnderecoDTO();
		enderecoDTO.setCep(cliente.getEndereco().getCep());
		enderecoDTO.setNumero(cliente.getEndereco().getNumero());
		enderecoDTO.setRua(cliente.getEndereco().getRua());
		this.endereco = enderecoDTO;
		
	}
}
