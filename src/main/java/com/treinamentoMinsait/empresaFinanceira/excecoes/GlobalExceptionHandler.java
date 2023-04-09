package com.treinamentoMinsait.empresaFinanceira.excecoes;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ClienteNotFoundException.class)
	public ResponseEntity<String> handleClienteNotFoundException(ClienteNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CPFAlreadyExistsException.class)
	public ResponseEntity<String> handleCPFAlreadyExistsException(CPFAlreadyExistsException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InvalidCEPException.class)
	public ResponseEntity<String> handleInvalidCEPException(InvalidCEPException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidCPFException.class)
	public ResponseEntity<String> handleInvalidCPFException(InvalidCPFException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidTelefoneException.class)
	public ResponseEntity<String> handleInvalidTelefoneException(InvalidTelefoneException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidValorInicialException.class)
	public ResponseEntity<String> handleInvalidValorInicialException(InvalidValorInicialException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InsufficientRendaMensalException.class)
	public ResponseEntity<String> handleInsufficientRendaMensalException(InsufficientRendaMensalException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

}
