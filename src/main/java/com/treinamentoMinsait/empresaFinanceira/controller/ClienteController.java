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
	public ResponseEntity<?> cadastrarCliente(@Valid @RequestBody Cliente cliente) {
		Cliente clienteCadastrado;
		
		try {
			clienteCadastrado = clienteService.cadastrarCliente(cliente);
		} catch (CPFAlreadyExistsException e1) { 
			return new ResponseEntity<>(e1.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e2) {
			return new ResponseEntity<>(e2.getMessage(), HttpStatus.BAD_REQUEST);
		}  
		
		return new ResponseEntity<>(new ClienteDTO(clienteCadastrado), HttpStatus.OK);
	}
	
	@GetMapping
	public List<ClienteDTO> recuperarClientes() {
		List<Cliente> clientesRecuperados;
		clientesRecuperados = this.clienteService.recuperarClientes();
		
		return this.clienteService.transformarClientesEmDTO(clientesRecuperados);
	}
	
	@GetMapping("/{cpf}")
	public ResponseEntity<?> recuperarCliente(@PathVariable String cpf) {
		Cliente clienteRecuperado;
		
		try {
			clienteRecuperado = this.clienteService.recuperarCliente(cpf);
		} catch (ClienteNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new ClienteDTO(clienteRecuperado), HttpStatus.OK);
	}
	
	@DeleteMapping("/{cpf}")
	public ResponseEntity<?> deletaCliente(@PathVariable String cpf) {
		try {			
			this.clienteService.deletaCliente(cpf);
		} catch (ClienteNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>("Cliente deletado com sucesso.", HttpStatus.OK);
	}
	//
	@PutMapping("/{cpf}")
	public ResponseEntity<?> alteraCliente(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable String cpf) {
		Cliente clienteAlterado;
		try {
			clienteAlterado = this.clienteService.alteraCliente(cpf, clienteDTO);
		} catch (ClienteNotFoundException e1) {
			return new ResponseEntity<>(e1.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e2) {
			return new ResponseEntity<>(e2.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(new ClienteDTO(clienteAlterado), HttpStatus.OK);
	}
}
