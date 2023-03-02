package it.polito.svt.vulnapp.controller;

import it.polito.svt.vulnapp.model.Movie;
import it.polito.svt.vulnapp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.beans.PropertyEditorSupport;
import java.io.File;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class MovieController {

    @InitBinder
    public void binder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            public void setAsText(String value) {
                try {
                    setValue(new SimpleDateFormat("yyyy-mm-dd").parse(value));
                } catch(ParseException e) {
                    setValue(null);
                }
            }

            public String getAsText() {
                return new SimpleDateFormat("yyyy-mm-dd").format((Date) getValue());
            }

        });
    }

    @Value("${imageFilePath}")
    private String imageFilePath;

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public ModelAndView getMovies() {

        /* retrieving movies from H2 database */
        List<Movie> result = movieService.getAllMovieList();

        ModelAndView mv = new ModelAndView("moviesList");
        mv.addObject("movies", result);
        return mv;
    }

    @GetMapping("/new-movie")
    public ModelAndView getNewMovieForm() {
        ModelAndView mv = new ModelAndView("movieForm");
        return mv;
    }

    @PostMapping("/save")
    public Map<String, Object> addMovie(Movie movie, @RequestParam("imageFile") MultipartFile file) throws IOException {

        /* CVE-2022-29001: uploading file without proper sanitization */
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            if (fileName != null) {
                FileUtils.copyInputStreamToFile(file.getInputStream(), new File(imageFilePath + fileName));
                movie.setImageFilePath("upload/static/images/" + fileName);
            }
        }

        /* CVE-2022-28588: saving the movie without proper input sanitization */
        boolean success = movieService.save(movie);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", success);
        return resultMap;
    }
}
