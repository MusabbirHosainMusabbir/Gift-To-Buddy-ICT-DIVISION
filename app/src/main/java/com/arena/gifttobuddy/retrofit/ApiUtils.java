package com.arena.gifttobuddy.retrofit;

public class ApiUtils {

    public static String tempUrl = "https://jsonplaceholder.typicode.com/";
    public static String tempUrl1 = "http://202.191.121.20/devtest/right_bell/api/v1/";

    public static ApiRequest getAPIService() {

        return RetrofitRequest.getRetrofitInstance(tempUrl).create(ApiRequest.class);
    }
}