package com.treinamentoMinsait.empresaFinanceira.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.treinamentoMinsait.empresaFinanceira.DTO.ClienteDTO;
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
	
	
	public Cliente cadastrarCliente(Cliente cliente) throws InvalidCPFException, InvalidTelefoneException, InvalidCEPException, CPFAlreadyExistsException {
		
		String CPF = cliente.getCPF();
		if (!this.cpfEhValido(CPF)) {
			throw new InvalidCPFException(CPF);
		}
		
		if (this.clienteRepository.existsByCPF(CPF)) {
			throw new CPFAlreadyExistsException(CPF);
		}
		
		String telefone = cliente.getTelefone();
		if (!this.telefoneEhValido(telefone)) {
			throw new InvalidTelefoneException(telefone);
		}
		
		String cep = cliente.getEndereco().getCep();
		if(!this.cepEhValido(cep)) {
			throw new InvalidCEPException(cep);
		}
		
		Endereco endereco = new Endereco();
	    endereco.setRua(cliente.getEndereco().getRua());
	    endereco.setNumero(cliente.getEndereco().getNumero());
	    endereco.setCep(cliente.getEndereco().getCep());
	    endereco.setCliente(cliente);

	    cliente.setEndereco(endereco);
		
		return this.clienteRepository.save(cliente);
	}
	
	public List<Cliente> recuperarClientes() {
		List<Cliente> clientes = this.clienteRepository.findAll();
		
		return clientes;
	}
	
	public Cliente recuperarCliente(String cpf) throws ClienteNotFoundException {
		Cliente cliente = this.clienteRepository.findByCPF(cpf)
	            .orElseThrow(() -> new ClienteNotFoundException(cpf));

	    return cliente;
	}
	
	public List<ClienteDTO> transformarClientesEmDTO(List<Cliente> clientes) {
		List<ClienteDTO> clientesDTO = new ArrayList<>();
		
		for (Cliente cliente : clientes) {
			ClienteDTO clienteDTO = new ClienteDTO(cliente);
			clientesDTO.add(clienteDTO);
		}
		
		return clientesDTO;
	}
	
	public void deletaCliente(String cpf) throws ClienteNotFoundException {
		Cliente cliente = this.clienteRepository.findByCPF(cpf)
				.orElseThrow(() -> new ClienteNotFoundException(cpf));
		clienteRepository.delete(cliente);
	}
	
	public Cliente alteraCliente(String cpf, ClienteDTO clienteDTO) throws ClienteNotFoundException, InvalidTelefoneException, InvalidCEPException {
		Cliente cliente = this.clienteRepository.findByCPF(cpf)
				.orElseThrow(() -> new ClienteNotFoundException(cpf));
		
		if (clienteDTO.getEndereco() != null) {
			Endereco novoEndereco = clienteDTO.getEndereco();
			this.alteraEndereco(cliente, novoEndereco);
		}
		
		if (clienteDTO.getNome() != null) {
			String novoNome = clienteDTO.getNome();
			cliente.setNome(novoNome);
		}
		
		if (clienteDTO.getRendaMensal() != null) {
			BigDecimal novaRendaMensal = clienteDTO.getRendaMensal();
			cliente.setRendaMensal(novaRendaMensal);
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
	
	private void alteraEndereco(Cliente cliente, Endereco novoEndereco) throws InvalidCEPException {
		
		String novoCep = novoEndereco.getCep();
		if (novoCep != null) {				
			if (!this.cepEhValido(novoCep)) {
				throw new InvalidCEPException(novoCep);
			}					
			cliente.getEndereco().setCep(novoCep);
		}
		
		int novoNumero = novoEndereco.getNumero();
		if (novoNumero != 0) {
			cliente.getEndereco().setNumero(novoNumero);				
		}
		
		String novaRua = novoEndereco.getRua();
		if (novaRua != null) {				
			cliente.getEndereco().setRua(novaRua);
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
