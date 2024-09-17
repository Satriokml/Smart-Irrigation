package com.example.plantmonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SetLocation extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String BASE_URL = "https://irrigationapi-production.up.railway.app/";

    TextView status;
    EditText latitude, longitude;
    ImageButton btnSetting;
    Button btnConfirm;
    LocationManager locationManager;
    LocationListener locationListener;
    irrigationAPI irrigationAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        // Initialize views
        status = findViewById(R.id.textView3);
        latitude = findViewById(R.id.editText_lat);
        longitude = findViewById(R.id.editText_long);
        btnSetting = findViewById(R.id.btn_location);
        btnConfirm = findViewById(R.id.button_input);

        // Initialize LocationManager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        irrigationAPI = retrofit.create(irrigationAPI.class);

        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        // Set an OnClickListener for btnSetting
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SetLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Request location updates
                    Toast.makeText(SetLocation.this, "Requesting location updates", Toast.LENGTH_SHORT).show();
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();
                            latitude.setText(String.valueOf(lat));
                            longitude.setText(String.valueOf(lon));
                            locationManager.removeUpdates(this);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        @Override
                        public void onProviderEnabled(@NonNull String provider) {
                        }

                        @Override
                        public void onProviderDisabled(@NonNull String provider) {
                        }
                    });
                } else {
                    Toast.makeText(SetLocation.this, "Requesting location permission", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(SetLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                }
            }
        });

        // Set an OnClickListener for btnConfirm
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get text from EditTexts
                String lat = latitude.getText().toString();
                String lon = longitude.getText().toString();

                // Validate the inputs if needed
                if (lat.isEmpty() || lon.isEmpty()) {
                    Toast.makeText(SetLocation.this, "Latitude and Longitude cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Call method to send PATCH request
                sendPatchRequest(lat, lon);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, we can start location updates
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                // Now request location updates since permission is granted
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendPatchRequest(String lat, String lon) {
        LocationData locationData = new LocationData(lat, lon);
        Call<Void> call = irrigationAPI.updateData(locationData);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    status.setText("PATCH request successful");
                    latitude.setText(" ");
                    longitude.setText(" ");
                } else {
                    status.setText("Failed to send PATCH request. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                status.setText( "Failed to send PATCH request: " + t.getMessage());
            }
        });
    }
}
