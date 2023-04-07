package com.treinamentoMinsait.empresaFinanceira.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Endereco {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Getter @Setter @NotBlank private String rua;
	@Getter @Setter @NotBlank private int numero;
	@Getter @Setter @NotBlank private String cep;
	@OneToOne @JsonIgnore @Getter @Setter private Cliente cliente;
}
