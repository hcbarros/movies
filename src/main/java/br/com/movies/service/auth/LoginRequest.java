package br.com.movies.service.auth;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

	@NotBlank(message = "O nome de usuário deve possuir algum valor!")
	private String username;
	
	@NotBlank(message = "A senha do usuário deve possuir algum valor!")
	private String password;
	
	public LoginRequest() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
