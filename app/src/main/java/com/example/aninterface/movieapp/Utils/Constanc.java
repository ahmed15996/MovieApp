package com.example.aninterface.movieapp.Utils;

public class Constanc {

    public static  final String QUERY_SEARCH = "query";

    public static final String API = "0427aa69e9d4db5185eaf3d77c7728c9";
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String YOUTUBE_URL_WATCH = "https://www.youtube.com/watch?v=";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    public static final String NOW_PLAYING_PATH = "now_playing";
    public static final String TOP_RATED_PATH = "top_rated";
    public static final String POPULAR_PATH = "popular";
    public static final String UPCOMING_PATH = "upcoming";
    public static final String AIRING_TO_DAY_PATH = "airing_today";
    public static final String ON_THE_AIR_PATH = "on_the_air";

    public static final String TYPE_MOVIE = "type_movies";
    public static final int NOW_PLAYING = 1;
    public static final int TOP_RATED = 2;
    public static final int POPULAR = 3;
    public static final int UPCOMING = 4;
    public static final int ON_THE_AIR = 5;
    public static final int AIRING_TO_DAY = 6;
    public static final int POPULAR_TV = 7;
    public static final int TOP_RATED_TV = 8;

    public static final String OPENT_DETAILS_KEY = "movie_id";
    public static final String OPEN_DETALIS_TV_KEY = "tv_id";

    public static final String DB_NAME = "favorite.db";
    public static final String MOVIE_TABLE = "movie_table";
    public static final String TV_TABLE = "tv_table";

    public static final String getTrailerImage(String key) {
        return "http://img.youtube.com/vi/" + key + "/hqdefault.jpg";
    }
}
