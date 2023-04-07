package com.treinamentoMinsait.empresaFinanceira;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import com.treinamentoMinsait.empresaFinanceira.DTO.ClienteDTO;
import com.treinamentoMinsait.empresaFinanceira.DTO.EnderecoDTO;
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
			this.clienteService.cadastrarCliente(clienteMock);
	    });
		
		assertInstanceOf(InvalidCPFException.class, exception);
		
	}
	
	@Test
	public void criarUsuarioComTelefoneInvalido() {
		Cliente clienteMock = this.gerarClienteMock();
		String telefoneInvalido = "1333";
		clienteMock.setTelefone(telefoneInvalido);
		
		Throwable exception = assertThrows(InvalidTelefoneException.class, () -> {
			this.clienteService.cadastrarCliente(clienteMock);
	    });
		
		assertInstanceOf(InvalidTelefoneException.class, exception);
	}
	
	@Test
	public void criarUsuarioComCepInvalido() {
		Cliente clienteMock = this.gerarClienteMock();
		String cepInvalido = "89554630";
		clienteMock.getEndereco().setCep(cepInvalido);
		
		Throwable exception = assertThrows(InvalidCEPException.class, () -> {
			this.clienteService.cadastrarCliente(clienteMock);
	    });
		
		assertInstanceOf(InvalidCEPException.class, exception);
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
	
	@Test
	public void alterarClienteComCepInvalido() {
		Cliente clienteMock = this.gerarClienteMock();
		when(this.clienteRepositoryMock.findByCPF(CPFvalido)).thenReturn(Optional.of(clienteMock));
		ClienteDTO clienteDtoMock = new ClienteDTO();
		
		String cepInvalido = "10000";
		EnderecoDTO enderecoDtoMock = new EnderecoDTO();
		enderecoDtoMock.setNumero(clienteMock.getEndereco().getNumero());
		enderecoDtoMock.setRua(clienteMock.getEndereco().getRua());
		enderecoDtoMock.setCep(cepInvalido);
		clienteDtoMock.setEnderecoDTO(enderecoDtoMock);
		
		Throwable exception = assertThrows(InvalidCEPException.class, () -> {
			this.clienteService.alteraCliente(this.CPFvalido, clienteDtoMock);
		});
		
		assertInstanceOf(InvalidCEPException.class, exception);
	}
	
	@Test
	public void alterarClienteComTelefoneInvalido() {
		Cliente clienteMock = this.gerarClienteMock();
		when(this.clienteRepositoryMock.findByCPF(CPFvalido)).thenReturn(Optional.of(clienteMock));
		ClienteDTO clienteDtoMock = new ClienteDTO();
		
		String telefoneInvalido = "(11(91503-2235";
		clienteDtoMock.setTelefone(telefoneInvalido);
		
		Throwable exception = assertThrows(InvalidTelefoneException.class, () -> {
			this.clienteService.alteraCliente(this.CPFvalido, clienteDtoMock);
		});
		
		assertInstanceOf(InvalidTelefoneException.class, exception);
	}
	
	@Test
	public void alteraClienteValido() {
		Cliente clienteMock = this.gerarClienteMock();
		when(this.clienteRepositoryMock.findByCPF(CPFvalido)).thenReturn(Optional.of(clienteMock));
		ClienteDTO clienteDtoMock = new ClienteDTO();
		String novoNome = "Novo Nome";
		clienteDtoMock.setNome(novoNome);
		
		try {
			Cliente clienteAlterado = this.clienteService.alteraCliente(CPFvalido, clienteDtoMock);
			assertNotNull(clienteAlterado);
			assertEquals(novoNome, clienteAlterado.getNome());
			assertEquals(this.CPFvalido, clienteAlterado.getCPF());
			assertEquals(this.rendaMensalValida, clienteAlterado.getRendaMensal());
			assertEquals(this.telefoneValido, clienteAlterado.getTelefone());
			assertEquals(this.cepValido, clienteAlterado.getEndereco().getCep());
			assertEquals(this.numeroValido, clienteAlterado.getEndereco().getNumero());
			assertEquals(this.ruaValida, clienteAlterado.getEndereco().getRua());
		} catch (Exception e) {
			fail(String.format("Não deveria ter lançado a exceção '%s'", e.getMessage()));
		}
	}
}
