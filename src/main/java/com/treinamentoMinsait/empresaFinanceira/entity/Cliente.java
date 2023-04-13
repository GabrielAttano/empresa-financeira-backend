package com.treinamentoMinsait.empresaFinanceira.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Cliente {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private Long id;
	
	@Getter @Setter @NotBlank private String CPF;
	@Getter @Setter @NotBlank private String nome;
	@Getter @Setter @NotBlank private String telefone;
	@Getter @Setter @Min(value=0) @NotNull private BigDecimal rendimentoMensal;
	
	@OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
	@Getter @Setter private Endereco endereco;
	
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
	@Getter private List<Emprestimo> emprestimos = new ArrayList<>();
	
	public void setEmprestimos(Emprestimo emprestimo) {
		this.emprestimos.add(emprestimo);
	}
}
