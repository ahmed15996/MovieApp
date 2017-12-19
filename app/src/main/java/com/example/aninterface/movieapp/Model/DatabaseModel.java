package com.example.aninterface.movieapp.Model;

/**
 * Created by interface on 09/12/2017.
 */

public class DatabaseModel {
    private String id, movie_id, movie_name, poster_path;

    public DatabaseModel() {
    }

    public DatabaseModel(String id, String movie_id, String movie_name, String poster_path) {
        this.id = id;
        this.movie_id = movie_id;
        this.movie_name = movie_name;
        this.poster_path = poster_path;
    }

    public DatabaseModel(String movie_id, String movie_name, String poster_path) {
        this.movie_id = movie_id;
        this.movie_name = movie_name;
        this.poster_path = poster_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
