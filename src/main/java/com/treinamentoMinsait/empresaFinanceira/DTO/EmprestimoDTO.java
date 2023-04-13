package com.treinamentoMinsait.empresaFinanceira.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.treinamentoMinsait.empresaFinanceira.entity.Emprestimo;
import com.treinamentoMinsait.empresaFinanceira.tipos.Relacionamento;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmprestimoDTO {
	private Long id;
	private BigDecimal valorInicial;
	private BigDecimal valorFinal;
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	private Relacionamento relacionamento;
	
	public EmprestimoDTO() {
		
	}
	
	public static EmprestimoDTO transformaEmprestimoEmDTO(Emprestimo emprestimo) {
		EmprestimoDTO emprestimoTransformado = new EmprestimoDTO();
		emprestimoTransformado.id = emprestimo.getId();
		emprestimoTransformado.valorInicial = emprestimo.getValorInicial();
		emprestimoTransformado.valorFinal = emprestimo.getValorFinal();
		emprestimoTransformado.dataInicial = emprestimo.getDataInicial();
		emprestimoTransformado.dataFinal = emprestimo.getDataFinal();
		emprestimoTransformado.relacionamento = emprestimo.getRelacionamento();
		
		return emprestimoTransformado;
	}
	
	public static List<EmprestimoDTO> transformarEmprestimosEmDTO(List<Emprestimo> emprestimos) {
		List<EmprestimoDTO> emprestimosDTO = new ArrayList<>();
		
		for (Emprestimo emprestimo : emprestimos) {
			EmprestimoDTO emprestimoDTO = transformaEmprestimoEmDTO(emprestimo);
			emprestimosDTO.add(emprestimoDTO);
		}
		
		return emprestimosDTO;
	}
}
