package com.example.plantmonitoring;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("canopy_temperature")
    private double canopyTemperature;

    @SerializedName("air_temperature")
    private double airTemperature;

    @SerializedName("soil_moisture")
    private int soilMoisture;

    @SerializedName("relative_humidity")
    private int relativeHumidity;

    @SerializedName("cwsi")
    private double cwsi;

    @SerializedName("weather_prediction")
    private String weatherPrediction;

    @SerializedName("decision")
    private int decision;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;


    public ApiResponse() {
        // Initialize with default values
        this.id = 0;
        this.canopyTemperature = 0.0;
        this.airTemperature = 0.0;
        this.soilMoisture = 0;
        this.relativeHumidity = 0;
        this.cwsi = 0.0;
        this.weatherPrediction = "";
        this.decision = 0;
        this.createdAt = "";
        this.updatedAt = "";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCanopyTemperature() {
        return canopyTemperature;
    }

    public void setCanopyTemperature(double canopyTemperature) {
        this.canopyTemperature = canopyTemperature;
    }

    public double getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(double airTemperature) {
        this.airTemperature = airTemperature;
    }

    public int getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(int soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public int getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(int relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public double getCwsi() {
        return cwsi;
    }

    public void setCwsi(double cwsi) {
        this.cwsi = cwsi;
    }

    public String getWeatherPrediction() {
        return weatherPrediction;
    }

    public void setWeatherPrediction(String weatherPrediction) {
        this.weatherPrediction = weatherPrediction;
    }

    public int getDecision() {
        return decision;
    }

    public void setDecision(int decision) {
        this.decision = decision;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
