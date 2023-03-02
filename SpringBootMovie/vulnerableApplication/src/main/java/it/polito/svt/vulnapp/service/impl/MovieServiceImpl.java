package it.polito.svt.vulnapp.service.impl;

import it.polito.svt.vulnapp.model.Movie;
import it.polito.svt.vulnapp.repository.MovieRepository;
import it.polito.svt.vulnapp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Movie> getAllMovieList() {
        return this.movieRepository.findAll();
    }

    @Override
    public boolean save(Movie movie) {
        this.movieRepository.save(movie);
        return true;
    }


}
