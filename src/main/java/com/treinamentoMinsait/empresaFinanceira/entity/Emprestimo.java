package com.treinamentoMinsait.empresaFinanceira.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Emprestimo {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private Long id;
	
	@Getter @Setter private String CPFCliente;
	@Getter @Setter private BigDecimal valorInicial;
	@Getter @Setter private BigDecimal valorFinal;
	@Getter @Setter @NotNull private LocalDate dataInicial;
	@Getter @Setter @NotNull private LocalDate dataFinal;
	
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	@Getter @Setter @JsonIgnore private Cliente cliente;
}
