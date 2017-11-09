package com.proprog.model;

/**
 * Created by Muhammad on 17-Jun-17.
 */

public class MovieProvider {
    String title,director;
    int poster_resource;

    public MovieProvider(String title, String director, int poster_resource) {
        this.title = title;
        this.director = director;
        this.poster_resource = poster_resource;
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

    public int getPoster_resource() {
        return poster_resource;
    }

    public void setPoster_resource(int poster_resource) {
        this.poster_resource = poster_resource;
    }
}
