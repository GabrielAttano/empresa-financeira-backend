package com.treinamentoMinsait.empresaFinanceira.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.treinamentoMinsait.empresaFinanceira.tipos.Relacionamento;

import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
public class Emprestimo {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String CPFCliente;
	private BigDecimal valorInicial;
	private Relacionamento relacionamento;
	private BigDecimal valorFinal;
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	@ManyToOne @JoinColumn(name = "cliente_id")
	@JsonIgnore private Cliente cliente;
}
