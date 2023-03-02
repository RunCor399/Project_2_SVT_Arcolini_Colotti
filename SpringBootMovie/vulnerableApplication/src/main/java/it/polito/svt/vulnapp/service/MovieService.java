package it.polito.svt.vulnapp.service;

import it.polito.svt.vulnapp.model.Movie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieService {

    List<Movie> getAllMovieList();

    boolean save(Movie movie);

}
