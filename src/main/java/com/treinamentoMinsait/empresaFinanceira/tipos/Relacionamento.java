package com.treinamentoMinsait.empresaFinanceira.tipos;

import java.math.BigDecimal;

public enum Relacionamento {
	Bronze {
		@Override
		public BigDecimal calculaValorFinal(BigDecimal valorInicial, int quantidadeEmprestimos) {
			return valorInicial.multiply(new BigDecimal("1.80"));
		}
	},
	Prata {
		@Override
		public BigDecimal calculaValorFinal(BigDecimal valorInicial, int quantidadeEmprestimos) {
			if (valorInicial.compareTo(new BigDecimal("5000")) > 0) {
				return valorInicial.multiply(new BigDecimal("1.40"));
			} else {
				return valorInicial.multiply(new BigDecimal("1.60"));
			}
		}
	},
	Ouro {
		@Override
		public BigDecimal calculaValorFinal(BigDecimal valorInicial, int quantidadeEmprestimos) {
			if (quantidadeEmprestimos <= 1) {
				return valorInicial.multiply(new BigDecimal("1.20"));
			} else {
				return valorInicial.multiply(new BigDecimal("1.30"));
			}
		}
	};
	
	public abstract BigDecimal calculaValorFinal(BigDecimal valorInicial, int quantidadeEmprestimos);
}
