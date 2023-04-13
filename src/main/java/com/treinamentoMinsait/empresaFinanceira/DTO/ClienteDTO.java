package com.treinamentoMinsait.empresaFinanceira.DTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.treinamentoMinsait.empresaFinanceira.entity.Cliente;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteDTO {
	private Long id;
	@NotBlank private String cpf;
	@NotBlank private String nome;
	@NotBlank private String telefone;
	@NotNull @Positive private BigDecimal rendimentoMensal;
	@NotNull @Valid private EnderecoDTO endereco;
	
	public ClienteDTO() {
		
	}
	
	public static Cliente transformarDTOemCliente(ClienteDTO clienteDTO) {
		Cliente clienteTransformado = new Cliente();
		clienteTransformado.setCpf(clienteDTO.getCpf());
		clienteTransformado.setNome(clienteDTO.getNome());
		clienteTransformado.setTelefone(clienteDTO.getTelefone());
		clienteTransformado.setRendimentoMensal(clienteDTO.getRendimentoMensal());
		
		return clienteTransformado;
	}
	
	public static ClienteDTO transformarClienteEmDto(Cliente cliente) {
		ClienteDTO clienteTransformado = new ClienteDTO();
		clienteTransformado.id = cliente.getId();
		clienteTransformado.cpf = cliente.getCpf();
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
			ClienteDTO clienteDTO = transformarClienteEmDto(cliente);
			clientesDTO.add(clienteDTO);
		}
		
		return clientesDTO;
	}
}
