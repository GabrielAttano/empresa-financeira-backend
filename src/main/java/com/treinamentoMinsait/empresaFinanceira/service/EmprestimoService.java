package com.treinamentoMinsait.empresaFinanceira.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treinamentoMinsait.empresaFinanceira.entity.Cliente;
import com.treinamentoMinsait.empresaFinanceira.entity.Emprestimo;
import com.treinamentoMinsait.empresaFinanceira.excecoes.ClienteNotFoundException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.EmprestimoNotFoundException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InsufficientRendaMensalException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidEmprestimoGetException;
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
	
	public Emprestimo cadastraEmprestimo(String cpf, BigDecimal valorInicial, Relacionamento relacionamento) throws ClienteNotFoundException, InvalidValorInicialException, InsufficientRendaMensalException {
		Cliente cliente = this.clienteRepository.findByCPF(cpf)
				.orElseThrow(() -> new ClienteNotFoundException(cpf));
		
		if (!this.valorInicialEhValido(valorInicial)) {
			throw new InvalidValorInicialException(valorInicial);
		}
		
		Emprestimo novoEmprestimo = this.criaEmprestimo(cliente, valorInicial, relacionamento);
		
		if (!this.clientePodeCriarEmprestimo(cliente, novoEmprestimo.getValorInicial())) {
			throw new InsufficientRendaMensalException();
		}
		
		cliente.setEmprestimos(novoEmprestimo);
		
		return emprestimoRepository.save(novoEmprestimo);
	}
	
	public Emprestimo recuperaEmprestimo(String cpf, Long id) throws ClienteNotFoundException, EmprestimoNotFoundException, InvalidEmprestimoGetException {
		Cliente cliente = this.clienteRepository.findByCPF(cpf)
				.orElseThrow(() -> new ClienteNotFoundException(cpf));
		
		Emprestimo emprestimo = this.emprestimoRepository.findById(id)
				.orElseThrow(() -> new EmprestimoNotFoundException(id));
		
		List<Emprestimo> emprestimosCliente = cliente.getEmprestimos();
		if (!emprestimosCliente.contains(emprestimo)) {
			throw new InvalidEmprestimoGetException(cpf, id);
		}
		
		return emprestimo;
	}
	
	public List<Emprestimo> recuperaEmprestimos(String cpf) throws ClienteNotFoundException {
		Cliente cliente = this.clienteRepository.findByCPF(cpf)
				.orElseThrow(() -> new ClienteNotFoundException(cpf));
		
		return cliente.getEmprestimos();
	}
	
	public void deletaEmprestimo(String cpf, Long id) throws ClienteNotFoundException, EmprestimoNotFoundException, InvalidEmprestimoGetException {
		Cliente cliente = this.clienteRepository.findByCPF(cpf)
				.orElseThrow(() -> new ClienteNotFoundException(cpf));
		
		Emprestimo emprestimo = this.emprestimoRepository.findById(id)
				.orElseThrow(() -> new EmprestimoNotFoundException(id));
		
		List<Emprestimo> emprestimosCliente = cliente.getEmprestimos();
		if (!emprestimosCliente.contains(emprestimo)) {
			throw new InvalidEmprestimoGetException(cpf, id);
		}
		
		emprestimosCliente.remove(emprestimo);
		this.emprestimoRepository.delete(emprestimo);
	}

	private Emprestimo criaEmprestimo(Cliente cliente, BigDecimal valorInicial, Relacionamento relacionamento) {
		Emprestimo novoEmprestimo = new Emprestimo();
		
		novoEmprestimo.setCliente(cliente);
		novoEmprestimo.setCPFCliente(cliente.getCPF());
		
		novoEmprestimo.setValorInicial(valorInicial);
		novoEmprestimo.setRelacionamento(relacionamento);
		int quantidadeEmprestimosCliente = cliente.getEmprestimos().size();
		BigDecimal valorFinal = relacionamento.calculaValorFinal(valorInicial, quantidadeEmprestimosCliente);
		novoEmprestimo.setValorFinal(valorFinal);
		
		LocalDate dataAtual = LocalDate.now();
		LocalDate dataFinal = dataAtual.plusDays(30);
		novoEmprestimo.setDataInicial(dataAtual);
		novoEmprestimo.setDataFinal(dataFinal);
		
		return novoEmprestimo;
	}
	
	private boolean clientePodeCriarEmprestimo(Cliente cliente, BigDecimal valorInicialNovoEmprestimo) {
		BigDecimal rendaMensalCliente = cliente.getRendimentoMensal();
		BigDecimal valorMaximoEmprestimos = rendaMensalCliente.multiply(new BigDecimal("10.00"));

		
		BigDecimal valorTotalEmprestimos = new BigDecimal("0.00");
		valorTotalEmprestimos = valorTotalEmprestimos.add(valorInicialNovoEmprestimo);
		for (Emprestimo emprestimo : cliente.getEmprestimos()) { 
			valorTotalEmprestimos = valorTotalEmprestimos.add(emprestimo.getValorInicial());
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
