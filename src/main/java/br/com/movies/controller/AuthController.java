package br.com.movies.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.movies.service.auth.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService; 

	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(
			@Valid @RequestBody LoginRequest loginRequest) {
		
		JwtResponse jwtResponse = authService.authenticateUser(
				loginRequest.getUsername(), loginRequest.getPassword());
		
		return ResponseEntity.ok(jwtResponse); 
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(
			@Valid @RequestBody SignupRequest signupRequest) {
		
		authService.registerUser(
				signupRequest.getUsername(), 
				signupRequest.getPassword(),
				signupRequest.getRoles());
		
		return ResponseEntity.ok(new MessageResponse(
				"Usuário registrado com sucesso!")); 
	}
	
	
}
