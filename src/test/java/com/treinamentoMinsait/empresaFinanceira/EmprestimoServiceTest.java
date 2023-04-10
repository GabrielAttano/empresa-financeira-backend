package com.treinamentoMinsait.empresaFinanceira;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import com.treinamentoMinsait.empresaFinanceira.entity.Cliente;
import com.treinamentoMinsait.empresaFinanceira.entity.Emprestimo;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InsufficientRendaMensalException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidValorInicialException;
import com.treinamentoMinsait.empresaFinanceira.repository.ClienteRepository;
import com.treinamentoMinsait.empresaFinanceira.repository.EmprestimoRepository;
import com.treinamentoMinsait.empresaFinanceira.service.EmprestimoService;
import com.treinamentoMinsait.empresaFinanceira.tipos.Relacionamento;

@SpringBootTest
public class EmprestimoServiceTest {
	ClienteRepository clienteRepositoryMock;
	EmprestimoRepository emprestimoRepositoryMock;
	EmprestimoService emprestimoService;
	
	ClienteServiceTest clienteServiceTest;
	
	protected Relacionamento relacionamentoValido = Relacionamento.Ouro;
	
	@BeforeEach
	public void setup() {
		clienteRepositoryMock = Mockito.mock(ClienteRepository.class);
		emprestimoRepositoryMock = Mockito.mock(EmprestimoRepository.class);
		clienteServiceTest = new ClienteServiceTest();
		
		emprestimoService = new EmprestimoService(this.clienteRepositoryMock, this.emprestimoRepositoryMock);
		// configurações do mock
		when(emprestimoRepositoryMock.save(Mockito.any(Emprestimo.class))).thenAnswer(new Answer<Emprestimo>() {
			@Override
			public Emprestimo answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				Emprestimo emprestimo = (Emprestimo) args[0];
				emprestimo.setId(1L);
				return emprestimo;
			}
		});
	}
	
	@Test
	public void criarEmprestimoComValorInicialInvalido() {
		Cliente clienteMock = this.clienteServiceTest.gerarClienteMock();
		String cpfCliente = clienteMock.getCPF();
		when(this.clienteRepositoryMock.findByCPF(cpfCliente)).thenReturn(Optional.of(clienteMock));
		
		BigDecimal valorInicialInvalido = new BigDecimal("-10.00");
		
		Throwable exception = assertThrows(InvalidValorInicialException.class, () -> {
			this.emprestimoService.cadastraEmprestimo(cpfCliente, valorInicialInvalido, this.relacionamentoValido);
		});
		
		assertInstanceOf(InvalidValorInicialException.class, exception);
	}
	
	@Test
	public void criarEmprestimoComRendaMensalInsuficiente() {
		Cliente clienteMock = this.clienteServiceTest.gerarClienteMock();
		String cpfCliente = clienteMock.getCPF();
		BigDecimal rendaMensalCliente = clienteMock.getRendaMensal();
		BigDecimal valorInicialInvalido = rendaMensalCliente.multiply(new BigDecimal("10.00")).add(new BigDecimal("1.00"));
		
		when(this.clienteRepositoryMock.findByCPF(cpfCliente)).thenReturn(Optional.of(clienteMock));		
		Throwable exception = assertThrows(InsufficientRendaMensalException.class, () -> {
			this.emprestimoService.cadastraEmprestimo(cpfCliente, valorInicialInvalido, this.relacionamentoValido);
		});
		
		assertInstanceOf(InsufficientRendaMensalException.class, exception);
	}
	
	@Test
	public void criarEmprestimoValido() {
		Cliente clienteMock = this.clienteServiceTest.gerarClienteMock();
		String cpfCliente = clienteMock.getCPF();
		int totalEmprestimosCliente = clienteMock.getEmprestimos().size();
		BigDecimal rendaMensalCliente = clienteMock.getRendaMensal();
		
		BigDecimal valorInicialValido = rendaMensalCliente.multiply(new BigDecimal("5.00"));
		BigDecimal valorFinalValido = this.relacionamentoValido.calculaValorFinal(valorInicialValido, totalEmprestimosCliente);
			
		when(this.clienteRepositoryMock.findByCPF(cpfCliente)).thenReturn(Optional.of(clienteMock));
		try {
			Emprestimo emprestimoSalvo = this.emprestimoService.cadastraEmprestimo(cpfCliente, valorInicialValido, this.relacionamentoValido);
			assertNotNull(emprestimoSalvo); 
			assertEquals(valorInicialValido, emprestimoSalvo.getValorInicial());
			assertEquals(valorFinalValido, emprestimoSalvo.getValorFinal());
			assertEquals(this.relacionamentoValido, emprestimoSalvo.getRelacionamento());
			
			
		} catch (Exception e) {
			fail(String.format("Não deveria ter lançado a exceção '%s'", e.getMessage()));
		}
	}
}
