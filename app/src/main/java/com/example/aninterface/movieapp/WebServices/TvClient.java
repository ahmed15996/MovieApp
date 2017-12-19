package com.example.aninterface.movieapp.WebServices;

import com.example.aninterface.movieapp.Utils.Constanc;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by interface on 12/12/2017.
 */

public class TvClient {


    private static Retrofit airingToDay;
    private static Retrofit onTheAir;
    private static Retrofit popular;
    private static Retrofit topRated;

    private static Retrofit tvSimiler;
    private static Retrofit casttv;
    private static Retrofit trailers;
    private static Retrofit tvDetails;




    public static ApiServices airingToDay(){
        if(airingToDay == null){
            airingToDay = new Retrofit.Builder()
                    .baseUrl(Constanc.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return airingToDay.create(ApiServices.class);
    }

    public static ApiServices onTheAir(){
        if(onTheAir == null){
            onTheAir = new Retrofit.Builder()
                    .baseUrl(Constanc.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return onTheAir.create(ApiServices.class);
    }

    public static ApiServices popular(){
        if(popular == null){
            popular = new Retrofit.Builder()
                    .baseUrl(Constanc.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return popular.create(ApiServices.class);
    }

    public static ApiServices topRated(){
        if(topRated == null){
            topRated = new Retrofit.Builder()
                    .baseUrl(Constanc.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return topRated.create(ApiServices.class);
    }

    public static ApiServices tvSimiler(){
        if(tvSimiler == null){
            tvSimiler = new Retrofit.Builder()
                    .baseUrl(Constanc.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return tvSimiler.create(ApiServices.class);
    }
    public static ApiServices casttv(){
        if(casttv == null){
            casttv = new Retrofit.Builder()
                    .baseUrl(Constanc.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return casttv.create(ApiServices.class);
    }
    public static ApiServices trailers(){
        if(trailers == null){
            trailers = new Retrofit.Builder()
                    .baseUrl(Constanc.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return trailers.create(ApiServices.class);
    }
    public static ApiServices tvDetails(){
        if(tvDetails == null){
            tvDetails = new Retrofit.Builder()
                    .baseUrl(Constanc.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return tvDetails.create(ApiServices.class);
    }
}
