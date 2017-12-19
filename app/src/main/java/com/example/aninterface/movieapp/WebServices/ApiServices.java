package com.example.aninterface.movieapp.WebServices;

import com.example.aninterface.movieapp.Model.CreditsModel;
import com.example.aninterface.movieapp.Model.MovieDetailsModel;
import com.example.aninterface.movieapp.Model.MovieModel;
import com.example.aninterface.movieapp.Model.SearchResult;
import com.example.aninterface.movieapp.Model.Trailer;
import com.example.aninterface.movieapp.Model.TvDetailsModel;
import com.example.aninterface.movieapp.Model.TvModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by interface on 12/12/2017.
 */

public interface ApiServices {
    // get Movie
    @GET("movie/{type_movie}?api_key=0427aa69e9d4db5185eaf3d77c7728c9")
    Call<MovieModel> getMovies(@Path("type_movie")String type_movie , @Query("page") int page);

    //get Tv
    @GET("tv/{type_tv}?api_key=0427aa69e9d4db5185eaf3d77c7728c9")
    Call<TvModel> getTv(@Path("type_tv")String type_tv, @Query("page")int page);

    @GET("movie/{movie_id}/similar?api_key=0427aa69e9d4db5185eaf3d77c7728c9")
    Call<MovieModel> getSimilarMovies(@Path("movie_id")int movie_id, @Query("page")int page);

    @GET("movie/{movie_id}/credits?api_key=0427aa69e9d4db5185eaf3d77c7728c9")
    Call<CreditsModel>getCreditsMovies(@Path("movie_id")int movie_id);

    @GET("movie/{movie_id}/videos?api_key=0427aa69e9d4db5185eaf3d77c7728c9&language=en-US")
    Call<Trailer> getTrailers(@Path("movie_id")int movie_id);

    @GET("movie/{movie_id}?api_key=0427aa69e9d4db5185eaf3d77c7728c9&language=en-US")
    Call<MovieDetailsModel> getMovieDetail(@Path("movie_id")int movie_id);




    @GET("tv/{tv_id}/similar?api_key=0427aa69e9d4db5185eaf3d77c7728c9")
    Call<TvModel> getSimilarTv(@Path("tv_id")int movie_id, @Query("page")int page);

    @GET("tv/{tv_id}/credits?api_key=0427aa69e9d4db5185eaf3d77c7728c9")
    Call<CreditsModel>getCreditsTv(@Path("tv_id")int movie_id);

    @GET("tv/{tv_id}/videos?api_key=0427aa69e9d4db5185eaf3d77c7728c9&language=en-US")
    Call<Trailer> getTrailersTv(@Path("tv_id")int movie_id);

    @GET("tv/{tv_id}?api_key=0427aa69e9d4db5185eaf3d77c7728c9&language=en-US")
    Call<TvDetailsModel> gettvDetail(@Path("tv_id")int movie_id);


}
