package com.arena.gifttobuddy.retrofit;

import com.arena.gifttobuddy.Models.User;
import com.google.android.gms.common.api.Api;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRequest {

    @GET("v2/everything/")
    Call<User> getMovieArticles(
            @Query("q") String query,
            @Query("apikey") String apiKey
    );

    @GET("posts")
    Call<List<User>> getPosts();

    @Headers("Content-Type: application/json")
    @POST("get-user-puzzle-summery")
    Call<User> getPostsValue(@Body String object);

    @Headers("Content-Type: application/json")
    @GET("registration.json")
    Call<User> getUser(@Body JSONObject object);
}


