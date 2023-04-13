package com.treinamentoMinsait.empresaFinanceira.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
			novoEmprestimo = this.emprestimoService.cadastraEmprestimo(cpf, emprestimoRequest.getValorInicial(), emprestimoRequest.getRelacionamento());
			return new ResponseEntity<>(EmprestimoDTO.transformaEmprestimoEmDTO(novoEmprestimo), HttpStatus.OK);
			
		} catch (ClienteNotFoundException | InvalidValorInicialException | InsufficientRendaMensalException e) {
			throw e;
		}
	}
	
	@GetMapping
	public List<EmprestimoDTO> recuperaEmprestimos(@PathVariable String cpf) throws ClienteNotFoundException {
		try {
			List<Emprestimo> emprestimosRecuperados;
			emprestimosRecuperados = this.emprestimoService.recuperaEmprestimos(cpf);
			return EmprestimoDTO.transformarEmprestimosEmDTO(emprestimosRecuperados);
		} catch (ClienteNotFoundException e) {
			throw e;
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> recuperaEmprestimo(@PathVariable String cpf, @PathVariable Long id) throws ClienteNotFoundException, EmprestimoNotFoundException, InvalidEmprestimoGetException {
		try {
			Emprestimo emprestimoRecuperado;
			emprestimoRecuperado = this.emprestimoService.recuperaEmprestimo(cpf, id);
			return new ResponseEntity<>(EmprestimoDTO.transformaEmprestimoEmDTO(emprestimoRecuperado), HttpStatus.OK);
		} catch (ClienteNotFoundException | EmprestimoNotFoundException | InvalidEmprestimoGetException e) {
			throw e;
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletaEmprestimo(@PathVariable String cpf, @PathVariable Long id) throws ClienteNotFoundException, EmprestimoNotFoundException, InvalidEmprestimoGetException {
		try {
			this.emprestimoService.deletaEmprestimo(cpf, id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ClienteNotFoundException | EmprestimoNotFoundException | InvalidEmprestimoGetException e) {
			throw e;
		}
	}
}
