package com.treinamentoMinsait.empresaFinanceira.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treinamentoMinsait.empresaFinanceira.entity.Cliente;
import com.treinamentoMinsait.empresaFinanceira.entity.Emprestimo;
import com.treinamentoMinsait.empresaFinanceira.excecoes.ClienteNotFoundException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InsufficientRendaMensalException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidValorInicialException;
import com.treinamentoMinsait.empresaFinanceira.repository.ClienteRepository;
import com.treinamentoMinsait.empresaFinanceira.repository.EmprestimoRepository;
import com.treinamentoMinsait.empresaFinanceira.tipos.Relacionamento;

@Service
public class EmprestimoService {
	private ClienteRepository clienteRepository;
	private EmprestimoRepository emprestimoRepository;
	
	@Autowired
	public EmprestimoService(ClienteRepository clienteRepository, EmprestimoRepository emprestimoRepository) {
		this.clienteRepository = clienteRepository;
		this.emprestimoRepository = emprestimoRepository;
	}
	
	public Emprestimo cadastraEmprestimo(String cpf, BigDecimal valorInicial) throws ClienteNotFoundException, InvalidValorInicialException, InsufficientRendaMensalException {
		Cliente cliente = this.clienteRepository.findByCPF(cpf)
				.orElseThrow(() -> new ClienteNotFoundException(cpf));
		
		if (!this.valorInicialEhValido(valorInicial)) {
			throw new InvalidValorInicialException(valorInicial);
		}
		
		Emprestimo novoEmprestimo = this.criaEmprestimo(cliente, valorInicial);
		
		if (!this.clientePodeCriarEmprestimo(cliente, novoEmprestimo.getValorFinal())) {
			throw new InsufficientRendaMensalException();
		}
		
		cliente.setEmprestimos(novoEmprestimo);
		
		return emprestimoRepository.save(novoEmprestimo);
	}
	
	private Emprestimo criaEmprestimo(Cliente cliente, BigDecimal valorInicial) {
		Emprestimo novoEmprestimo = new Emprestimo();
		novoEmprestimo.setCliente(cliente);
		novoEmprestimo.setCPFCliente(cliente.getCPF());
		
		novoEmprestimo.setValorInicial(valorInicial);
		Relacionamento relacionamentoCliente = cliente.getRelacionamento();
		int quantidadeEmprestimosCliente = cliente.getEmprestimos().size();
		BigDecimal valorFinal = relacionamentoCliente.calculaValorFinal(valorInicial, quantidadeEmprestimosCliente);
		novoEmprestimo.setValorFinal(valorFinal);
		
		LocalDate dataAtual = LocalDate.now();
		LocalDate dataFinal = dataAtual.plusDays(30);
		novoEmprestimo.setDataInicial(dataAtual);
		novoEmprestimo.setDataFinal(dataFinal);
		
		return novoEmprestimo;
	}
	
	private boolean clientePodeCriarEmprestimo(Cliente cliente, BigDecimal valorFinalNovoEmprestimo) {
		BigDecimal rendaMensalCliente = cliente.getRendaMensal();
		BigDecimal valorMaximoEmprestimos = rendaMensalCliente.multiply(new BigDecimal("10.00"));

		
		BigDecimal valorTotalEmprestimos = new BigDecimal("0.00");
		valorTotalEmprestimos = valorTotalEmprestimos.add(valorFinalNovoEmprestimo);
		for (Emprestimo emprestimo : cliente.getEmprestimos()) { 
			valorTotalEmprestimos = valorTotalEmprestimos.add(emprestimo.getValorFinal());
		}
		
		if (valorTotalEmprestimos.compareTo(valorMaximoEmprestimos) == 1) {
			return false;
		} else {
			return true;
		}
		
	}
	
	private boolean valorInicialEhValido(BigDecimal valorInicial) {
		if (valorInicial.compareTo(new BigDecimal("0.00")) == 1) {
			return true;
		}
		
		return false;
	}
}
