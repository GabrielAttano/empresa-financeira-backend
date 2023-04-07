package com.treinamentoMinsait.empresaFinanceira.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treinamentoMinsait.empresaFinanceira.DTO.ClienteDTO;
import com.treinamentoMinsait.empresaFinanceira.entity.Cliente;
import com.treinamentoMinsait.empresaFinanceira.excecoes.CPFAlreadyExistsException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.ClienteNotFoundException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidCEPException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidCPFException;
import com.treinamentoMinsait.empresaFinanceira.excecoes.InvalidTelefoneException;
import com.treinamentoMinsait.empresaFinanceira.service.ClienteService;

@RestController
@RequestMapping("/api/v1/empresa-financeira/clientes")
public class ClienteController {
	private ClienteService clienteService;
	
	@Autowired
	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	@PostMapping
	public ResponseEntity<?> cadastrarCliente(@Valid @RequestBody Cliente cliente) throws InvalidCPFException, CPFAlreadyExistsException, InvalidCEPException, InvalidTelefoneException {
		try {
			Cliente clienteCadastrado;
			clienteCadastrado = clienteService.cadastrarCliente(cliente);
			return new ResponseEntity<>(new ClienteDTO(clienteCadastrado), HttpStatus.OK);
		} catch (InvalidCPFException | CPFAlreadyExistsException | InvalidTelefoneException | InvalidCEPException e) { 
			throw e;
		} 
	}
	
	@GetMapping
	public List<ClienteDTO> recuperarClientes() {
		List<Cliente> clientesRecuperados;
		clientesRecuperados = this.clienteService.recuperarClientes();
		return this.clienteService.transformarClientesEmDTO(clientesRecuperados);
	}
	
	@GetMapping("/{cpf}")
	public ResponseEntity<?> recuperarCliente(@PathVariable String cpf) throws ClienteNotFoundException {		
		try {
			Cliente clienteRecuperado;
			clienteRecuperado = this.clienteService.recuperarCliente(cpf);
			return new ResponseEntity<>(new ClienteDTO(clienteRecuperado), HttpStatus.OK);
		} catch (ClienteNotFoundException e) {
			throw e;
		}
		
	}
	
	@DeleteMapping("/{cpf}")
	public ResponseEntity<?> deletaCliente(@PathVariable String cpf) throws ClienteNotFoundException{
		try {			
			this.clienteService.deletaCliente(cpf);
			return new ResponseEntity<>("Cliente deletado com sucesso.", HttpStatus.OK);
		} catch (ClienteNotFoundException e) {
			throw e;
		}
	}
	
	@PutMapping("/{cpf}")
	public ResponseEntity<?> alteraCliente(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable String cpf) throws ClienteNotFoundException, InvalidCEPException, InvalidTelefoneException {
		try {
			Cliente clienteAlterado;
			clienteAlterado = this.clienteService.alteraCliente(cpf, clienteDTO);
			return new ResponseEntity<>(new ClienteDTO(clienteAlterado), HttpStatus.OK);
		} catch (InvalidTelefoneException | InvalidCEPException | ClienteNotFoundException e) { 
			throw e;
		} 
	}
}
