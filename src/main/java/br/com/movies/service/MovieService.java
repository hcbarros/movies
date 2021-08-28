package br.com.movies.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.movies.model.Movie;
import br.com.movies.repository.MovieRepository;


@Service
public class MovieService {

	
	@Autowired
	private MovieRepository repo;
	
	
	public Movie save(Movie movie) {
		
		return repo.save(movie);
	}	
	
	
	public Movie update(Long id, Movie movie) {		
		
		Movie m = find(id);
		
		m.setCoverImage(movie.getCoverImage());
		m.setGenre(movie.getGenre());
		m.setOverview(movie.getOverview());
		m.setRating(movie.getRating());
		m.setReleaseDate(movie.getReleaseDate());
		m.setTitle(movie.getTitle());
		
		return repo.save(m);
	}	
	
	public List<Movie> findAll() {
		return repo.findAll();
	}
	
	public Movie find(Long id) {
		return repo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Filme nao encontrado!"));
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}
	
	public Long count() {
		return repo.count();
	}
	
	public Long countByGenre(String genre) {
		return repo.countByGenre(genre);
	}
	
}