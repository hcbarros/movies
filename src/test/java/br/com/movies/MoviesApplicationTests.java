package br.com.movies;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.math.BigDecimal;

import br.com.movies.model.Movie;
import br.com.movies.service.auth.JwtResponse;
import br.com.movies.service.auth.LoginRequest;
import br.com.movies.service.auth.SignupRequest;


@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MoviesApplicationTests {
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	
	@Test
	@Order(1)
	public void deveRetornarStatus400EAcessoNaoAutorizado() {
	
		ResponseEntity<LinkedHashMap> resp = this.restTemplate
				.getForEntity("http://localhost:" + port + "/movies/auth", LinkedHashMap.class);
	
		LinkedHashMap<String, Object> map = resp.getBody();
		
		int status = (int) map.get("status");
		String error = (String) map.get("error");
		
		assertEquals(status, 401);
		assertEquals(error, "Unauthorized");
	}
	
	@Test
	@Order(2)
	public void deveRetornarListaDeFilmesAposAutenticacao() {
		
		List<LinkedHashMap<String, Object>> list = getMovies();

		LinkedHashMap<String, Object> map = list.get(0);
		
		assertEquals(list.size(), 2);
		assertEquals(map.get("title"), "...E o Vento Levou");
		assertEquals(map.get("genre"), "Drama");		
	}
	
	@Test
	@Order(3)
	public void deveCadastrarUmNovoFilmeAposAutenticacao () {
		
		HttpHeaders headers = getHeaders();
		
		Movie movie = new Movie("Conan - O Destruidor", LocalDate.parse("1984-06-29"), "Aventura", 
				
				"Conan enfrenta inimigos poderosos para cumprir as exigências de uma "
				+ "rainha sem escrúpulos que lhe promete trazer de volta a sua amada. "
				+ "Na verdade, o que ela planeja é acordar um deus adormecido.", 
				
				"https://github.com/hcbarros/movies/blob/develop/src/images/conan.jpeg?raw=true", BigDecimal.valueOf(6.5));

		ResponseEntity<Movie> responseBody = this.restTemplate.postForEntity("http://localhost:" + port + "/movies/auth", 
				new HttpEntity<>(movie, headers), Movie.class);
		
		Movie reponse = responseBody.getBody();
		
		assertEquals(reponse.getTitle(), "Conan - O Destruidor");
	}
	
	@Test
	@Order(4)
	public void deveAtuaizarFilme() {
		
		HttpHeaders headers = getHeaders();
		
		Movie movieBefore = getMovie(1);

		movieBefore.setRating(new BigDecimal("10.00"));

		this.restTemplate.exchange("http://localhost:" + port + "/movies/auth/1", 
				HttpMethod.PUT, new HttpEntity<>(movieBefore, headers), Movie.class);
		
		Movie movieLater = getMovie(1);
		
		assertEquals(movieBefore.getRating(), movieLater.getRating());
	}
	
	@Test
	@Order(5)
	public void deveExcluirFilme() {
		
		HttpHeaders headers = getHeaders();
		
		List listBefore = getMovies();
		
		this.restTemplate.exchange("http://localhost:" + port + "/movies/auth/1", 
				HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
		
		List listLater = getMovies();
		
		assertNotEquals(listBefore.size(), listLater.size());
	}
	
	
	@Test
	@Order(6)
	public void deveRetornarQuantiaDeFilmesSemAutenticacao() {
	
		ResponseEntity<Long> resp = this.restTemplate
				.getForEntity("http://localhost:" + port + "/movies/stats/count", Long.class);
	
		assertEquals(resp.getBody(), 2L);
	}
	
	@Test
	@Order(7)
	public void deveRetornarQuantiaDeFilmesPorGeneroSemAutenticacao() {
	
		ResponseEntity<Long> resp = this.restTemplate
				.getForEntity("http://localhost:" + port + "/movies/stats/countByGenre/Aventura", Long.class);
	
		assertEquals(resp.getBody(), 1L);
	}
	
	@Test
	@Order(8)
	public void deveCadastrarUmNovoUsuario() {
	
		Set<String> roles = new HashSet<>();
		roles.add("USER");
		
		SignupRequest request = new SignupRequest();
		request.setUsername("FranciscoSilva");
		request.setPassword("Silva123");
		request.setRoles(roles);
		
		ResponseEntity<LinkedHashMap> resp = this.restTemplate
				.postForEntity("http://localhost:" + port + "/auth/signup", 
						request, LinkedHashMap.class);
	
		LinkedHashMap<String, Object> map = resp.getBody();
				
		assertEquals(map.get("response"), "Usuário registrado com sucesso!");
	}
	
	
	
	private Movie getMovie(int id) {
		
		return this.restTemplate.exchange("http://localhost:" + port + "/movies/auth/"+id, 
				HttpMethod.GET, new HttpEntity<>(getHeaders()), Movie.class).getBody();
	}
	
	private List getMovies() {
		
		return this.restTemplate.exchange("http://localhost:" + port + "/movies/auth", 
				HttpMethod.GET, new HttpEntity<>(getHeaders()), List.class).getBody();
	}
	
	private HttpHeaders getHeaders() {
		
		LoginRequest login = new LoginRequest();
		login.setUsername("henriquecbarros");
		login.setPassword("henrique123");
		
		ResponseEntity<JwtResponse> resp = this.restTemplate
				.postForEntity("http://localhost:" + port + "/auth/signin", 
						login, JwtResponse.class);
				
		JwtResponse jwt = resp.getBody();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + jwt.getToken());
		
		return headers;
	}

	
}
	