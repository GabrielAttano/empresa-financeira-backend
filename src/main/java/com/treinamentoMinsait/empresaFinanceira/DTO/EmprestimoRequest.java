package com.treinamentoMinsait.empresaFinanceira.DTO;

import java.math.BigDecimal;

import com.treinamentoMinsait.empresaFinanceira.tipos.Relacionamento;

import lombok.Getter;
import lombok.Setter;

public class EmprestimoRequest {
	@Getter @Setter BigDecimal valorInicial;
	@Getter @Setter Relacionamento relacionamento;
}
