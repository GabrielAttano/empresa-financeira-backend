package com.treinamentoMinsait.empresaFinanceira.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public List<ClienteDTO> recuperarClientes() {
		List<Cliente> clientes = this.clienteRepository.findAll();
		List<ClienteDTO> clientesDTO = new ArrayList<>();
		for (Cliente cliente : clientes) {
			ClienteDTO clienteDTO = new ClienteDTO(cliente);
			clientesDTO.add(clienteDTO);
		}
		return clientesDTO;
	}
	
	public ClienteDTO recuperarCliente(String cpf) throws ClienteNotFoundException {
		Cliente cliente = this.clienteRepository.findByCPF(cpf)
	            .orElseThrow(() -> new ClienteNotFoundException(cpf));

	    // Transforma o objeto Cliente em ClienteDTO e retorna
	    ClienteDTO clienteDTO = new ClienteDTO(cliente);
	    return clienteDTO;
	}
	
	public void deletaCliente(String cpf) throws ClienteNotFoundException {
		Cliente cliente = this.clienteRepository.findByCPF(cpf)
				.orElseThrow(() -> new ClienteNotFoundException(cpf));
		clienteRepository.delete(cliente);
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
