package com.treinamentoMinsait.empresaFinanceira.DTO;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import com.treinamentoMinsait.empresaFinanceira.entity.Cliente;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteDTO {
	private Long id;
	private String cpf;
	private String nome;
	private String telefone;
	@Min(value=0) private BigDecimal rendimentoMensal;
	private EnderecoDTO endereco;
	
	public ClienteDTO() {
		
	}
	
	public ClienteDTO(Cliente cliente) {
		this.id = cliente.getId();
		this.cpf = cliente.getCPF();
		this.nome = cliente.getNome();
		this.telefone = cliente.getTelefone();
		this.rendimentoMensal = cliente.getRendimentoMensal();
		EnderecoDTO enderecoDTO = new EnderecoDTO();
		enderecoDTO.setCep(cliente.getEndereco().getCep());
		enderecoDTO.setNumero(cliente.getEndereco().getNumero());
		enderecoDTO.setRua(cliente.getEndereco().getRua());
		this.endereco = enderecoDTO;
	}
}
