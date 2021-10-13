package br.com.movies.service.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.movies.repository.UserRepository;
import br.com.movies.model.ERole;
import br.com.movies.model.Role;
import br.com.movies.model.User;
import br.com.movies.repository.RoleRepository;
import br.com.movies.security.JwtUtils;
import br.com.movies.security.UserDetailsImpl;


@Service
public class AuthService {

	@Autowired
	AuthenticationManager authenticationManager; 
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtUtils jwtUtils;
	
	
	public void registerUser(String username, String password,
			Set<String> strRoles) {

		if(userRepository.existsByUsername(username)) {
			throw new EntityExistsException("Usuário já existe!");
		}
		
		User user = new User(username, passwordEncoder.encode(password));
		
		Set<Role> roles = new HashSet<>();
		
		if(strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.USER)
					.orElseThrow(() -> new EntityNotFoundException (
							"Role nao encontrada!"));
			roles.add(userRole);
		} else {
			
			strRoles.forEach(role -> {
				switch (role) {
				case "ADMIN":
					Role adminRole = roleRepository.findByName(ERole.ADMIN)
					.orElseThrow(() -> new EntityNotFoundException (
							"Role nao encontrada!"));
					roles.add(adminRole);
					break;
					
				default:
					Role userRole = roleRepository.findByName(ERole.USER)
					.orElseThrow(() -> new EntityNotFoundException (
							"Role nao encontrada!"));
					roles.add(userRole);
				}
			});
		}
		
		user.setRoles(roles);
		userRepository.save(user);
				
	}
	
	
	public JwtResponse authenticateUser(String username, String password) {		
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, password));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);		
		
		UserDetailsImpl userDetails = (UserDetailsImpl) 
				authentication.getPrincipal();		
		
		List<String> roles = userDetails.getAuthorities().stream()
										.map(item -> item.getAuthority())
										.collect(Collectors.toList());
		
		return new JwtResponse(jwt, userDetails.getId(), 
				userDetails.getUsername(), roles);
	}
	
	
}
