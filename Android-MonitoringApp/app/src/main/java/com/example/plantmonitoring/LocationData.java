package com.example.plantmonitoring;

public class LocationData {
    private String latitude;
    private String longitude;

    public LocationData(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters (if needed)
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

