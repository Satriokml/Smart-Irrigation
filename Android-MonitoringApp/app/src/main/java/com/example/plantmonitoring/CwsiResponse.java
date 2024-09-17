package com.example.plantmonitoring;

public class CwsiResponse {
    private float cwsi;
    private String createdAt;

    public float getCwsi() {
        return cwsi;
    }

    public void setCwsi(float cwsi) {
        this.cwsi = cwsi;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}