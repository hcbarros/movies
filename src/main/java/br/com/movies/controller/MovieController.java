package br.com.movies.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.movies.model.Movie;
import br.com.movies.service.MovieService;


@RestController
@Validated
@RequestMapping("/movies")
public class MovieController  {

	@Autowired
	private MovieService service;
	
	
	@GetMapping(value = "/auth/{id}")
	public Movie find(@PathVariable Long id) {
		return service.find(id);
	}
	
	@GetMapping(value = "/auth")
	public List<Movie> findAll() {
		return service.findAll();
	}

	@GetMapping(value = "/stats/count")
	public Long count() {
		return service.count();
	}
	
	@GetMapping(value = "/stats/countByGenre/{genre}")
	public Long countByGenre(@PathVariable String genre) {
		return service.countByGenre(genre);
	}
	
	@PostMapping(value = "/auth")
	@ResponseStatus(HttpStatus.CREATED)
	public Movie save(@RequestBody @Valid Movie movie) {
		return service.save(movie);
	}
	
	@PutMapping(value = "/auth/{id}")
	public Movie update(@PathVariable Long id, 
								   @RequestBody @Valid Movie movie) {
		return service.update(id, movie);
	}
	
	@DeleteMapping(value = "/auth/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
	
}
