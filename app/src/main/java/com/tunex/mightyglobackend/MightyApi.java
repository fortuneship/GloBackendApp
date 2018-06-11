package com.tunex.mightyglobackend;


import com.tunex.mightyglobackend.model.Feed;
import com.tunex.mightyglobackend.model.PostRequest;
import com.tunex.mightyglobackend.model.PostResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by MIGHTY5 on 5/31/2018.
 */

public interface MightyApi {

    String BASE_URL = "http://phplaravel-116401-370897.cloudwaysapps.com/";

    @Headers("Content-Type: application/json")
    @GET("api/admin/data_request")
    Call<Feed> getData();

    @POST("api/admin/data_request")
    Call<PostResponse> getTokenAccess(@Body PostRequest postRequest);

}
