package com.treinamentoMinsait.empresaFinanceira.DTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
	
	public static ClienteDTO transformaClienteEmDto(Cliente cliente) {
		ClienteDTO clienteTransformado = new ClienteDTO();
		clienteTransformado.id = cliente.getId();
		clienteTransformado.cpf = cliente.getCPF();
		clienteTransformado.nome = cliente.getNome();
		clienteTransformado.telefone = cliente.getTelefone();
		clienteTransformado.rendimentoMensal = cliente.getRendimentoMensal();
		EnderecoDTO enderecoDTO = new EnderecoDTO();
		enderecoDTO.setCep(cliente.getEndereco().getCep());
		enderecoDTO.setNumero(cliente.getEndereco().getNumero());
		enderecoDTO.setRua(cliente.getEndereco().getRua());
		clienteTransformado.endereco = enderecoDTO;
		
		return clienteTransformado;
	}
	
	public static List<ClienteDTO> transformarClientesEmDTO(List<Cliente> clientes) {
		List<ClienteDTO> clientesDTO = new ArrayList<>();
		
		for (Cliente cliente : clientes) {
			ClienteDTO clienteDTO = transformaClienteEmDto(cliente);
			clientesDTO.add(clienteDTO);
		}
		
		return clientesDTO;
	}
}
