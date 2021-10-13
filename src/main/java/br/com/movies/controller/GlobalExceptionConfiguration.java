package br.com.movies.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.movies.controller.response.ResponseError;
import br.com.movies.exception.UsernameNotFoundException;


@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionConfiguration extends ResponseEntityExceptionHandler {

	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .collect(Collectors.toList());
		
		return ResponseEntity.badRequest().body(new ResponseError(errors));
	}
	
	@ExceptionHandler({ UsernameNotFoundException.class, 
						EntityExistsException.class, 
						EntityNotFoundException.class })
	public ResponseEntity<Object> handleCadastroException (
			RuntimeException ex, WebRequest request) {
	    List<String> errors = new ArrayList<String>();
	    
	    errors.add(ex.getMessage());

		return ResponseEntity.badRequest().body(new ResponseError(errors));
	}
	
	
}
