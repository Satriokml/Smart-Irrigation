package com.example.plantmonitoring;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;

import java.util.ArrayList;
import java.util.List;


public interface irrigationAPI {

    @GET("data")
    Call<ArrayList<ApiResponse>> getData();

    @GET("lastest") // Update the path based on your actual endpoint
    Call<ApiResponse> getLastest();

    @GET("lastest_cwsi") // Update the path based on your actual endpoint
    Call<List<CwsiResponse>> getCwsiValues();

    @GET("weather") // Update the path based on your actual endpoint
    Call<WeatherResponse> getWeather();

    @PATCH("location") // Adjust the path as per your API endpoint
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<Void> updateData(@Body LocationData locationData); // Use LocationData as request body
}
