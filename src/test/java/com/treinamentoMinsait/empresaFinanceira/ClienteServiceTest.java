package com.treinamentoMinsait.empresaFinanceira;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import com.treinamentoMinsait.empresaFinanceira.entity.Cliente;
import com.treinamentoMinsait.empresaFinanceira.entity.Endereco;
import com.treinamentoMinsait.empresaFinanceira.excecoes.CPFAlreadyExistsException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidCEPException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidCPFException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidTelefoneException;
import com.treinamentoMinsait.empresaFinanceira.repository.ClienteRepository;
import com.treinamentoMinsait.empresaFinanceira.service.ClienteService;

@SpringBootTest
public class ClienteServiceTest {
	
	ClienteRepository clienteRepositoryMock;
	ClienteService clienteService;
	
	private String CPFvalido = "00000000000";
	private String nomeValido = "Nome";
	private BigDecimal rendaMensalValida = new BigDecimal(100);
	private String telefoneValido = "(00)00000-0000";
	
	private String cepValido = "00000-000";
	private int numeroValido = 1;
	private String ruaValida = "Rua";
	
	@BeforeEach
	public void setup() {
		clienteRepositoryMock = Mockito.mock(ClienteRepository.class);
		clienteService = new ClienteService(clienteRepositoryMock);
		
		// configuração do mock
		when(clienteRepositoryMock.save(Mockito.any(Cliente.class))).thenAnswer(new Answer<Cliente>() {
			@Override
			public Cliente answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				Cliente cliente = (Cliente) args[0];
				cliente.setId(1L);
				return cliente;
			}
		});
	}
	
	private Cliente gerarClienteMock() {
		Cliente clienteMock = new Cliente();
		clienteMock.setCPF(this.CPFvalido);
		clienteMock.setNome(this.nomeValido);
		clienteMock.setRendaMensal(this.rendaMensalValida);
		clienteMock.setTelefone(this.telefoneValido);
		
		Endereco endereco = new Endereco();
		endereco.setCep(this.cepValido);
		endereco.setNumero(this.numeroValido);
		endereco.setRua(this.ruaValida);
		endereco.setCliente(clienteMock);
		
		clienteMock.setEndereco(endereco);
		
		return clienteMock;
	}
	
	@Test
	public void criarUsuarioComCpfInvalido() {
		Cliente clienteMock = this.gerarClienteMock();
		String cpfInvalido = "123";
		clienteMock.setCPF(cpfInvalido);
		
		Throwable exception = assertThrows(InvalidCPFException.class, () -> {
			clienteService.cadastrarCliente(clienteMock);
	    });
		assertEquals(String.format("CPF '%s' inválido. O CPF deve possuir 11 digitos, sem pontuações.", cpfInvalido), exception.getMessage());
		
	}
	
	@Test
	public void criarUsuarioComTelefoneInvalido() {
		Cliente clienteMock = this.gerarClienteMock();
		String telefoneInvalido = "1333";
		clienteMock.setTelefone(telefoneInvalido);
		
		Throwable exception = assertThrows(InvalidTelefoneException.class, () -> {
			clienteService.cadastrarCliente(clienteMock);
	    });
		
		assertEquals(String.format("Telefone '%s' inválido. O telefone deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX, onde cada X é um dígito.", telefoneInvalido), exception.getMessage());
	}
	
	@Test
	public void criarUsuarioComCepInvalido() {
		Cliente clienteMock = this.gerarClienteMock();
		String cepInvalido = "89554630";
		clienteMock.getEndereco().setCep(cepInvalido);
		
		Throwable exception = assertThrows(InvalidCEPException.class, () -> {
			clienteService.cadastrarCliente(clienteMock);
	    });
		
		assertEquals(String.format("CEP '%s' inválido. O CEP deve estar no formato 'XXXXX-XXX', onde cada X é um digito.", cepInvalido), exception.getMessage());
	}
	
	@Test
	public void criarUsuarioValido() {
		Cliente clienteMock = this.gerarClienteMock();
		try {
			
			Cliente clienteSalvo = this.clienteService.cadastrarCliente(clienteMock);
			assertNotNull(clienteSalvo);
			assertEquals(this.nomeValido, clienteSalvo.getNome());
			assertEquals(this.CPFvalido, clienteSalvo.getCPF());
			assertEquals(this.rendaMensalValida, clienteSalvo.getRendaMensal());
			assertEquals(this.telefoneValido, clienteSalvo.getTelefone());
			
			assertEquals(this.cepValido, clienteSalvo.getEndereco().getCep());
			assertEquals(this.numeroValido, clienteSalvo.getEndereco().getNumero());
			assertEquals(this.ruaValida, clienteSalvo.getEndereco().getRua());
		} catch (Exception e) {
			fail(String.format("Não deveria ter lançado a exceção '%s'", e.getMessage()));
		}
	}
}
