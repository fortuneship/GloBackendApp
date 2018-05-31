package com.tunex.mightyglobackend;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MIGHTY5 on 5/31/2018.
 */

public class ApiClient {


    public static final String BASE_URL = "http://phplaravel-116401-370897.cloudwaysapps.com/";

    public static Retrofit retrofit = null;

    public static Retrofit getApiClient(){

        if (retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit;
    }

}
