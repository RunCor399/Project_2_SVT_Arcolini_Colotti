package it.polito.svt.vulnapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;
    private String director;
    private Integer overallRating;

    private Date publishDate;

    private String description;

    private String imageFilePath;

    public Movie() {
    }

    public Movie(String title, String director, Integer overallRating, Date publishDate, String description) {
        this.title = title;
        this.director = director;
        this.overallRating = overallRating;
        this.publishDate = publishDate;
        this.description = description;
    }

    public Movie(String title, String director, Integer overallRating, Date publishDate, String description, String imageFilePath) {
        this.title = title;
        this.director = director;
        this.overallRating = overallRating;
        this.publishDate = publishDate;
        this.description = description;
        this.imageFilePath = imageFilePath;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(Integer overallRating) {
        this.overallRating = overallRating;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", overallRating=" + overallRating +
                ", publishDate=" + publishDate +
                ", description='" + description + '\'' +
                '}';
    }
}
