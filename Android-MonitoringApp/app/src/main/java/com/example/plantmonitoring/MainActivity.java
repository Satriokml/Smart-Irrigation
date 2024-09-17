package com.example.plantmonitoring;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.squareup.picasso.Picasso;

import android.graphics.Color;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    TextView tv_cwsi, tv_cwsi_desc, tv_soil, tv_soil_desc, tv_air, tv_canopy, tv_rh, tv_weather_desc;
    TextView tv_latest, tv_lastest_date;
    ImageView iv_weather;
    LineChart LineChartCwsi;
    LinearLayout layout_cwsi, layout_soil;
    ImageButton btn_refresh, btn_setting;
    Button btn_history;

    private Handler handler = new Handler();
    private Runnable refreshRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_latest = findViewById(R.id.textView_latest);

        tv_cwsi = findViewById(R.id.textView_cwsi_value);
        tv_cwsi_desc = findViewById(R.id.textView_cwsi_desc);
        tv_soil = findViewById(R.id.textView_soil_value);
        tv_soil_desc = findViewById(R.id.textView_soil_desc);
        tv_air = findViewById(R.id.textView_air_value);
        tv_canopy = findViewById(R.id.textView_canopy_value);
        tv_rh = findViewById(R.id.textView_rh_value);
        tv_weather_desc = findViewById(R.id.textView_weather_desc);
        tv_lastest_date = findViewById(R.id.textView_date);

        iv_weather = findViewById(R.id.weather_icon);

        layout_cwsi = findViewById(R.id.linearLayout_cwsi);
        layout_soil = findViewById(R.id.linearLayout_soil);

        LineChartCwsi = findViewById(R.id.LineChartcwsi);

        btn_refresh = findViewById(R.id.btn_refresh);

        btn_history = findViewById(R.id.button_history);
        btn_setting = findViewById(R.id.btn_setting);

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, history.class);
                startActivity(intent);
            }
        });

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetLocation.class);
                startActivity(intent);
            }
        });


        // Initial data load
        loadData();

        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                refreshData();
                handler.postDelayed(this, 10000); // 10 seconds
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(refreshRunnable); // Start the runnable when the activity starts
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(refreshRunnable); // Stop the runnable when the activity stops to avoid memory leaks
    }


    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://irrigationapi-production.up.railway.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        irrigationAPI apiService = retrofit.create(irrigationAPI.class);

        Call<ApiResponse> latestCall = apiService.getLastest();
        latestCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        updateUI(apiResponse);
                    } else {
                        tv_cwsi.setText("API call failed with status: " + response.code());
                    }
                } else {
                    tv_cwsi.setText("API call failed with status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                tv_cwsi.setText("API call failed: " + t.getMessage());
            }
        });

        Call<WeatherResponse> weatherCall = apiService.getWeather();
        weatherCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
                        String imgurl = "https:"+weatherResponse.getCondition().getIcon();
                        Picasso.get().load(imgurl).into(iv_weather);
                        tv_weather_desc.setText(weatherResponse.getCondition().getText());
                    } else {
                        iv_weather.setImageResource(R.drawable.error);
                    }
                } else {
                    iv_weather.setImageResource(R.drawable.error);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                iv_weather.setImageResource(R.drawable.error);
            }
        });

        Call<List<CwsiResponse>> historicalCall = apiService.getCwsiValues();
        historicalCall.enqueue(new Callback<List<CwsiResponse>>() {
            @Override
            public void onResponse(Call<List<CwsiResponse>> call, Response<List<CwsiResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CwsiResponse> cwsiResponses = response.body();
                    updateChartData(cwsiResponses);
                } else {
                    Toast.makeText(MainActivity.this, "API call failed with status: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CwsiResponse>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(ApiResponse apiResponse) {
        tv_cwsi.setText(String.valueOf(apiResponse.getCwsi()));

        // Calculate time interval
        LocalDateTime createdAt = LocalDateTime.parse(apiResponse.getCreatedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime now = LocalDateTime.now();
        long minutesDiff = ChronoUnit.MINUTES.between(createdAt, now);
        tv_latest.setText("Terakhir diupdate " + minutesDiff + " menit yang lalu");
        tv_lastest_date.setText(apiResponse.getCreatedAt());
        //tv_lastest_date.setText("2024-05-28 07:28:14");

        // Adjust background and description based on CWSI value
        if (apiResponse.getCwsi() > 0.5) {
            tv_cwsi_desc.setText("Tumbuhan kekurangan air");
            layout_cwsi.setBackgroundResource(R.drawable.gradient_red);
        } else {
            tv_cwsi_desc.setText("Kebutuhan air tercukupi");
            layout_cwsi.setBackgroundResource(R.drawable.gradient_green);
        }

        // Set soil moisture and adjust background and description
        tv_soil.setText(apiResponse.getSoilMoisture() + "%");
        if (apiResponse.getSoilMoisture() < 60) {
            tv_soil_desc.setText("Tanah terlalu kering");
            layout_soil.setBackgroundResource(R.drawable.gradient_red);
        } else if (apiResponse.getSoilMoisture() > 80) {
            tv_soil_desc.setText("Tanah terlalu lembab");
            layout_soil.setBackgroundResource(R.drawable.gradient_red);
        } else {
            tv_soil_desc.setText("Tanah dalam kondisi optimal");
            layout_soil.setBackgroundResource(R.drawable.gradient_green);
        }

        // Set other fields
        tv_air.setText(apiResponse.getAirTemperature() + "°C");
        tv_canopy.setText(apiResponse.getCanopyTemperature() + "°C");
        tv_rh.setText(apiResponse.getRelativeHumidity() + "%rh");



    }

    private void updateChartData(List<CwsiResponse> cwsiResponses) {
        Collections.reverse(cwsiResponses);
        LineDataSet lineDataSet1 = new LineDataSet(dataValues1(cwsiResponses), "CWSI");
        lineDataSet1.setColor(Color.BLUE); // Set color for the dataset
        lineDataSet1.setValueTextColor(Color.BLACK); // Set value text color
        lineDataSet1.setValueTextSize(10f); // Set value text size

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);

        LineData data = new LineData(dataSets);
        LineChartCwsi.getXAxis().setGranularity(1f);
        LineChartCwsi.setData(data);
        LineChartCwsi.invalidate();
    }


    private ArrayList<Entry> dataValues1(List<CwsiResponse> cwsiResponses) {
        ArrayList<Entry> dataVals = new ArrayList<>();
        for (int i = 0; i < cwsiResponses.size(); i++) {
            dataVals.add(new Entry(i, cwsiResponses.get(i).getCwsi()));
        }
        return dataVals;
    }

    public void refreshData() {
        // Clear previous data
        /*tv_cwsi.setText("");
        tv_cwsi_desc.setText("");
        tv_latest.setText("");
        layout_cwsi.setBackgroundResource(android.R.color.white);

        tv_soil.setText("");
        tv_soil_desc.setText("");
        tv_air.setText("");
        tv_canopy.setText("");
        tv_rh.setText("");
        tv_weather_desc.setText("");
        layout_soil.setBackgroundResource(android.R.color.white);*/

        // Reload data
        loadData();
    }

}
