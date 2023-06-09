package com.treinamentoMinsait.empresaFinanceira.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.treinamentoMinsait.empresaFinanceira.DTO.ClienteDTO;
import com.treinamentoMinsait.empresaFinanceira.DTO.EnderecoDTO;
import com.treinamentoMinsait.empresaFinanceira.entity.Cliente;
import com.treinamentoMinsait.empresaFinanceira.entity.Endereco;
import com.treinamentoMinsait.empresaFinanceira.excecoes.CPFAlreadyExistsException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.ClienteNotFoundException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidCEPException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidCPFException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidTelefoneException;
import com.treinamentoMinsait.empresaFinanceira.repository.ClienteRepository;

@Service
public class ClienteService {
	private ClienteRepository clienteRepository;
	
	@Autowired
	public ClienteService(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}
	
	
	public Cliente cadastrarCliente(ClienteDTO clienteDTO) throws InvalidCPFException, InvalidTelefoneException, InvalidCEPException, CPFAlreadyExistsException {
		
		String cpf = clienteDTO.getCpf();
		if (!this.cpfEhValido(cpf)) {
			throw new InvalidCPFException(cpf);
		}
		
		if (this.clienteRepository.existsByCpf(cpf)) {
			throw new CPFAlreadyExistsException(cpf);
		}
		
		String telefone = clienteDTO.getTelefone();
		if (!this.telefoneEhValido(telefone)) {
			throw new InvalidTelefoneException(telefone);
		}
		
		String cep = clienteDTO.getEndereco().getCep();
		if(!this.cepEhValido(cep)) {
			throw new InvalidCEPException(cep);
		}
		
		Cliente cliente = ClienteDTO.transformarDTOemCliente(clienteDTO);
		Endereco endereco = EnderecoDTO.transformarDTOemEndereco(clienteDTO.getEndereco());
		
	    endereco.setCliente(cliente);
	    cliente.setEndereco(endereco);
		
		return this.clienteRepository.save(cliente);
	}
	
	public List<Cliente> recuperarClientes() {
		List<Cliente> clientes = this.clienteRepository.findAll();
		
		return clientes;
	}
	
	public Cliente recuperarCliente(String cpf) throws ClienteNotFoundException {
		Cliente cliente = this.clienteRepository.findByCpf(cpf)
	            .orElseThrow(() -> new ClienteNotFoundException(cpf));

	    return cliente;
	}
	
	public void deletaCliente(String cpf) throws ClienteNotFoundException {
		Cliente cliente = this.clienteRepository.findByCpf(cpf)
				.orElseThrow(() -> new ClienteNotFoundException(cpf));
		
		clienteRepository.delete(cliente);
	}
	
	public Cliente alteraCliente(String cpf, ClienteDTO clienteDTO) throws ClienteNotFoundException, InvalidTelefoneException, InvalidCEPException {
		Cliente cliente = this.clienteRepository.findByCpf(cpf)
				.orElseThrow(() -> new ClienteNotFoundException(cpf));
		
		if (clienteDTO.getEndereco() != null) {
			EnderecoDTO novoEndereco = clienteDTO.getEndereco();
			this.alteraEndereco(cliente, novoEndereco);
		}
		
		if (clienteDTO.getNome() != null) {
			String novoNome = clienteDTO.getNome().trim();
			if (!novoNome.isEmpty()) {				
				cliente.setNome(novoNome);
			}
		}
		
		if (clienteDTO.getRendimentoMensal() != null) {
			BigDecimal novaRendaMensal = clienteDTO.getRendimentoMensal();
			if (novaRendaMensal.compareTo(new BigDecimal("0")) >= 0) {				
				cliente.setRendimentoMensal(novaRendaMensal);
			}
		}
		
		if (clienteDTO.getTelefone() != null) {
			String novoTelefone = clienteDTO.getTelefone();
			if (!this.telefoneEhValido(novoTelefone)) {
				throw new InvalidTelefoneException(novoTelefone);
			}
			cliente.setTelefone(novoTelefone);
		}
		
		return this.clienteRepository.save(cliente);
		
	}
	
	private void alteraEndereco(Cliente cliente, EnderecoDTO novoEndereco) throws InvalidCEPException {
		
		String novoCep = novoEndereco.getCep();
		if (novoCep != null) {				
			if (!this.cepEhValido(novoCep)) {
				throw new InvalidCEPException(novoCep);
			}					
			cliente.getEndereco().setCep(novoCep);
		}
		
		int novoNumero = novoEndereco.getNumero();
		if (novoNumero >= 0) {
			cliente.getEndereco().setNumero(novoNumero);				
		}
		
		String novaRua = novoEndereco.getRua().trim();
		if (novaRua != null) {
			if (!novaRua.isEmpty()) {
				cliente.getEndereco().setRua(novaRua);				
			}
		}
	}
	
	private boolean cepEhValido(String cep) {
		// Regex que testa se o CEP está no formato XXXXX-XXX
		String cepRegex = "\\d{5}-\\d{3}";
		Pattern pattern = Pattern.compile(cepRegex);
		Matcher matcher = pattern.matcher(cep);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean cpfEhValido(String cpf) {
		if (cpf.length() != 11) {
			return false;
		}
		
		return true;
	}
	
	private boolean telefoneEhValido(String telefone) {
		// Regex que testa se o telefone está no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX
		String telefoneRegex = "\\(\\d{2}\\)\\d{4,5}-\\d{4}";
		Pattern pattern = Pattern.compile(telefoneRegex);
		Matcher matcher = pattern.matcher(telefone);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
}
