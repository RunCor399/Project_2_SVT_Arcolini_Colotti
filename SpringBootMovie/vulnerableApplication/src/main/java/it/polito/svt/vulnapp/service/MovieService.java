package it.polito.svt.vulnapp.service;

import it.polito.svt.vulnapp.model.Movie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieService {

    /**
     * Retrieve the list of movies from the H2 database
     * @return a list of movies
     */
    List<Movie> getAllMovieList();

    /**
     * Store a new movie into the H2 database
     * @param movie the new movie to be stored
     * @return true in case of success, false otherwise
     */
    boolean save(Movie movie);

}
