package br.com.movies.service.auth;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupRequest {
	
	@NotBlank(message = "O nome de usuário deve possuir algum valor!")
	@Size(min = 6, max = 100, 
	message = "O nome de usuário deve possuir no mínimo 3 e no máximo 100 caracteres!")
	private String username;
	
	@NotBlank(message = "O senha do usuário deve possuir algum valor!")
	@Size(min = 6, max = 100,
	message = "A senha deve possuir no mínimo 6 e no máximo 100 caracteres!")
	private String password;

	private Set<String> roles;
	
	public SignupRequest() {
		
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

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}


}
