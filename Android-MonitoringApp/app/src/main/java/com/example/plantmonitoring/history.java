package com.example.plantmonitoring;

import android.os.Bundle;



import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import android.view.View;



public class history extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PlantsAdapter adapter;
    private ArrayList<ApiResponse> PlantsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recycler_view);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://irrigationapi-production.up.railway.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        irrigationAPI apiService = retrofit.create(irrigationAPI.class);
        Call<ArrayList<ApiResponse>> call = apiService.getData();
        call.enqueue(new Callback<ArrayList<ApiResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ApiResponse>> call, Response<ArrayList<ApiResponse>> response) {
                if (response.isSuccessful()) {
                    PlantsArrayList = response.body();
                    Collections.reverse(PlantsArrayList);
                    adapter = new PlantsAdapter(PlantsArrayList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(history.this));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ApiResponse>> call, Throwable t) {
                // Handle error
            }
        });
    }
}