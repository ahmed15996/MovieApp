package com.example.aninterface.movieapp.WebServices;

import com.example.aninterface.movieapp.Utils.Constanc;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by interface on 12/12/2017.
 */

public class MovieClient {
    private static Retrofit movieSimiler;
    private static Retrofit castMovies;
    private static Retrofit movieTrailers;
    private static Retrofit movieDetails;

    private static Retrofit playing_now_movies;
    private static Retrofit top_rated_movies;
    private static Retrofit popular_movies;
    private static Retrofit upComing_movies;

    public static ApiServices movieSimiler() {
        if (movieSimiler == null) {
            movieSimiler = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constanc.BASE_URL)
                    .build();
        }
        return movieSimiler.create(ApiServices.class);
    }

    public static ApiServices castMovie() {
        if (castMovies == null) {
            castMovies = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constanc.BASE_URL)
                    .build();
        }
        return castMovies.create(ApiServices.class);
    }

    public static ApiServices movieTrailers() {
        if (movieTrailers == null) {
            movieTrailers = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constanc.BASE_URL)
                    .build();
        }
        return movieTrailers.create(ApiServices.class);
    }

    public static ApiServices movieDetails() {
        if (movieDetails == null) {
            movieDetails = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constanc.BASE_URL)
                    .build();
        }
        return movieDetails.create(ApiServices.class);
    }

    public static ApiServices nowPlaying() {
        if (playing_now_movies == null) {
            playing_now_movies = new Retrofit.Builder()
                    .baseUrl(Constanc.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return playing_now_movies.create(ApiServices.class);
    }

    public static ApiServices movieTopRated() {
        if (top_rated_movies == null) {
            top_rated_movies = new Retrofit.Builder()
                    .baseUrl(Constanc.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return top_rated_movies.create(ApiServices.class);
    }

    public static ApiServices moviePopular() {
        if (popular_movies == null) {
            popular_movies = new Retrofit.Builder()
                    .baseUrl(Constanc.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return popular_movies.create(ApiServices.class);
    }

    public static ApiServices upComing() {
        if (upComing_movies == null) {
            upComing_movies = new Retrofit.Builder()
                    .baseUrl(Constanc.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return upComing_movies.create(ApiServices.class);
    }
}
