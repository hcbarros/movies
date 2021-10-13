package br.com.movies.config;

import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.movies.model.ERole;
import br.com.movies.model.Movie;
import br.com.movies.model.Role;
import br.com.movies.model.User;
import br.com.movies.repository.MovieRepository;
import br.com.movies.repository.RoleRepository;
import br.com.movies.repository.UserRepository;


@Configuration
@Profile("prod")
public class DataLoader {
		
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	@Bean
	CommandLineRunner baseLoad(MovieRepository movieRepository,
							   RoleRepository roleRepository, 
							   UserRepository userRepository) {
		 
		return (args) -> {
			
			Set<Role> roles = new HashSet<>();
			roles.add(new Role(ERole.USER));
			roles.add(new Role(ERole.ADMIN));
			
			User user = new User("henriquecbarros", passwordEncoder.encode("henrique123"));
			user.setRoles(roles);
			
			userRepository.save(user);
			
			
			Movie movie1 = new Movie("...E o Vento Levou", LocalDate.parse("1939-12-15"), "Drama", 
					
					"Scarlett O'Hara é uma jovem mimada que consegue tudo o que quer. "
					+ "No entanto, algo falta em sua vida: o amor de Ashley Wilkes, um "
					+ "nobre sulista que deve se casar com a sua prima Melanie. Tudo "
					+ "muda quando a Guerra Civil americana explode e Scarlett precisa "
					+ "lutar para sobreviver e manter a fazenda da família.",  
					
					"https://github.com/hcbarros/movies/blob/develop/src/images/e_o_vento_levou.jpg?raw=true", 
					BigDecimal.valueOf(7.6));
			
			Movie movie2 = new Movie("Batman Begins", LocalDate.parse("2005-06-17"), "Ação", 
					
					"O jovem Bruce Wayne viaja para o Extremo Oriente, onde recebe "
					+ "treinamento em artes marciais do mestre Henri Ducard, um membro "
					+ "da misteriosa Liga das Sombras. Quando Ducard revela que a "
					+ "verdadeira proposta da Liga é a destruição completa da cidade "
					+ "de Gotham, Wayne retorna à sua cidade com o intuito de livrá-la "
					+ "de criminosos e assassinos. Com a ajuda do mordomo Alfred e "
					+ "do expert Lucius Fox, nasce Batman.",  
					
					"https://github.com/hcbarros/movies/blob/develop/src/images/batman.jpeg?raw=true", 
					BigDecimal.valueOf(8.2));
			
			
			movieRepository.saveAll(Arrays.asList(movie1, movie2));
			
		};
	}

}
