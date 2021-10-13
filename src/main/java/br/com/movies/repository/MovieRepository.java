package br.com.movies.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.movies.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {	
	
	Long countByGenre(String genre);	
	
}
