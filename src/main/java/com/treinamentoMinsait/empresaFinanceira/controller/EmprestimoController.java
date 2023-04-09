package com.treinamentoMinsait.empresaFinanceira.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treinamentoMinsait.empresaFinanceira.DTO.EmprestimoDTO;
import com.treinamentoMinsait.empresaFinanceira.DTO.EmprestimoRequest;
import com.treinamentoMinsait.empresaFinanceira.entity.Emprestimo;
import com.treinamentoMinsait.empresaFinanceira.excecoes.ClienteNotFoundException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.EmprestimoNotFoundException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InsufficientRendaMensalException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidEmprestimoGetException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidValorInicialException;
import com.treinamentoMinsait.empresaFinanceira.service.EmprestimoService;

@RestController
@RequestMapping("/api/v1/empresa-financeira/clientes/{cpf}/emprestimos")
public class EmprestimoController {
	private EmprestimoService emprestimoService;
	
	@Autowired
	public EmprestimoController(EmprestimoService emprestimoService) {
		this.emprestimoService = emprestimoService;
	}
	
	@PostMapping
	public ResponseEntity<?> cadastraEmprestimo(@PathVariable String cpf, @RequestBody EmprestimoRequest emprestimoRequest) throws ClienteNotFoundException, InvalidValorInicialException, InsufficientRendaMensalException {
		try {
			Emprestimo novoEmprestimo;
			novoEmprestimo = this.emprestimoService.cadastraEmprestimo(cpf, emprestimoRequest.getValorInicial());
			return new ResponseEntity<>(new EmprestimoDTO(novoEmprestimo), HttpStatus.OK);
			
		} catch (ClienteNotFoundException | InvalidValorInicialException | InsufficientRendaMensalException e) {
			throw e;
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> recuperarEmprestimo(@PathVariable String cpf, @PathVariable Long id) throws ClienteNotFoundException, EmprestimoNotFoundException, InvalidEmprestimoGetException {
		try {
			Emprestimo emprestimoRecuperado;
			emprestimoRecuperado = this.emprestimoService.recuperarEmprestimo(cpf, id);
			return new ResponseEntity<>(new EmprestimoDTO(emprestimoRecuperado), HttpStatus.OK);
		} catch (ClienteNotFoundException | EmprestimoNotFoundException | InvalidEmprestimoGetException e) {
			throw e;
		}
	}
}
