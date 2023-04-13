package com.treinamentoMinsait.empresaFinanceira.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
	
import com.treinamentoMinsait.empresaFinanceira.entity.Endereco;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EnderecoDTO {
	@NotBlank private String rua;
	@Positive private int numero;
	@NotBlank private String cep;
	
	public EnderecoDTO() {
		
	}
	
	public static Endereco transformarDTOemEndereco(EnderecoDTO enderecoDTO) {
		Endereco enderecoTransformado = new Endereco();
		enderecoTransformado.setRua(enderecoDTO.getRua().trim());
		enderecoTransformado.setNumero(enderecoDTO.getNumero());
		enderecoTransformado.setCep(enderecoDTO.getCep());
		
		return enderecoTransformado;
	}
	
	public static EnderecoDTO transformarEnderecoEmDTO(Endereco endereco) {
		EnderecoDTO enderecoTransformado = new EnderecoDTO();
		enderecoTransformado.setRua(endereco.getRua());
		enderecoTransformado.setNumero(endereco.getNumero());
		enderecoTransformado.setCep(endereco.getCep());
		
		return enderecoTransformado;
	}
	
}
