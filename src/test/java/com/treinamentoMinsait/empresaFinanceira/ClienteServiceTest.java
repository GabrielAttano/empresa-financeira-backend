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

import com.treinamentoMinsait.empresaFinanceira.DTO.ClienteDTO;
import com.treinamentoMinsait.empresaFinanceira.DTO.EnderecoDTO;
import com.treinamentoMinsait.empresaFinanceira.entity.Cliente;
import com.treinamentoMinsait.empresaFinanceira.entity.Endereco;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidCEPException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidCPFException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidTelefoneException;
import com.treinamentoMinsait.empresaFinanceira.repository.ClienteRepository;
import com.treinamentoMinsait.empresaFinanceira.service.ClienteService;

@SpringBootTest
public class ClienteServiceTest {
	
	ClienteRepository clienteRepositoryMock;
	ClienteService clienteService;
	
	// Atributos validos para o cliente
	protected String CPFvalido = "00000000000";
	protected String nomeValido = "Nome";
	protected BigDecimal rendimentoMensalValido = new BigDecimal(100);
	protected String telefoneValido = "(00)00000-0000";
	
	protected String cepValido = "00000-000";
	protected int numeroValido = 1;
	protected String ruaValida = "Rua";
	
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
	
	protected Cliente gerarClienteMock() {
		Cliente clienteMock = new Cliente();
		clienteMock.setCpf(this.CPFvalido);
		clienteMock.setNome(this.nomeValido);
		clienteMock.setRendimentoMensal(this.rendimentoMensalValido);
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
		clienteMock.setCpf(cpfInvalido);
		
		Throwable exception = assertThrows(InvalidCPFException.class, () -> {
			this.clienteService.cadastrarCliente(ClienteDTO.transformarClienteEmDto(clienteMock));
	    });
		
		assertInstanceOf(InvalidCPFException.class, exception);
		
	}
	
	@Test
	public void criarUsuarioComTelefoneInvalido() {
		Cliente clienteMock = this.gerarClienteMock();
		String telefoneInvalido = "1333";
		clienteMock.setTelefone(telefoneInvalido);
		
		Throwable exception = assertThrows(InvalidTelefoneException.class, () -> {
			this.clienteService.cadastrarCliente(ClienteDTO.transformarClienteEmDto(clienteMock));
	    });
		
		assertInstanceOf(InvalidTelefoneException.class, exception);
	}
	
	@Test
	public void criarUsuarioComCepInvalido() {
		Cliente clienteMock = this.gerarClienteMock();
		String cepInvalido = "89554630";
		clienteMock.getEndereco().setCep(cepInvalido);
		
		Throwable exception = assertThrows(InvalidCEPException.class, () -> {
			this.clienteService.cadastrarCliente(ClienteDTO.transformarClienteEmDto(clienteMock));
	    });
		
		assertInstanceOf(InvalidCEPException.class, exception);
	}
	
	@Test
	public void criarUsuarioValido() {
		Cliente clienteMock = this.gerarClienteMock();
		try {
			
			Cliente clienteSalvo = this.clienteService.cadastrarCliente(ClienteDTO.transformarClienteEmDto(clienteMock));
			assertNotNull(clienteSalvo);
			assertEquals(this.nomeValido, clienteSalvo.getNome());
			assertEquals(this.CPFvalido, clienteSalvo.getCpf());
			assertEquals(this.rendimentoMensalValido, clienteSalvo.getRendimentoMensal());
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
		when(this.clienteRepositoryMock.findByCpf(CPFvalido)).thenReturn(Optional.of(clienteMock));
		ClienteDTO clienteDtoMock = new ClienteDTO();
		
		String cepInvalido = "10000";
		EnderecoDTO enderecoDtoMock = new EnderecoDTO();
		enderecoDtoMock.setNumero(clienteMock.getEndereco().getNumero());
		enderecoDtoMock.setRua(clienteMock.getEndereco().getRua());
		enderecoDtoMock.setCep(cepInvalido);
		clienteDtoMock.setEndereco(enderecoDtoMock);
		
		Throwable exception = assertThrows(InvalidCEPException.class, () -> {
			this.clienteService.alteraCliente(this.CPFvalido, clienteDtoMock);
		});
		
		assertInstanceOf(InvalidCEPException.class, exception);
	}
	
	@Test
	public void alterarClienteComTelefoneInvalido() {
		Cliente clienteMock = this.gerarClienteMock();
		when(this.clienteRepositoryMock.findByCpf(CPFvalido)).thenReturn(Optional.of(clienteMock));
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
		when(this.clienteRepositoryMock.findByCpf(CPFvalido)).thenReturn(Optional.of(clienteMock));
		ClienteDTO clienteDtoMock = new ClienteDTO();
		String novoNome = "Novo Nome";
		clienteDtoMock.setNome(novoNome);
		
		try {
			Cliente clienteAlterado = this.clienteService.alteraCliente(CPFvalido, clienteDtoMock);
			assertNotNull(clienteAlterado);
			assertEquals(novoNome, clienteAlterado.getNome());
			assertEquals(this.CPFvalido, clienteAlterado.getCpf());
			assertEquals(this.rendimentoMensalValido, clienteAlterado.getRendimentoMensal());
			assertEquals(this.telefoneValido, clienteAlterado.getTelefone());
			assertEquals(this.cepValido, clienteAlterado.getEndereco().getCep());
			assertEquals(this.numeroValido, clienteAlterado.getEndereco().getNumero());
			assertEquals(this.ruaValida, clienteAlterado.getEndereco().getRua());
		} catch (Exception e) {
			fail(String.format("Não deveria ter lançado a exceção '%s'", e.getMessage()));
		}
	}
}
