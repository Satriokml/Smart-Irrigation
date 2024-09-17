package com.example.plantmonitoring;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.PlantsViewHolder> {

    private ArrayList<ApiResponse> dataList;

    public PlantsAdapter(ArrayList<ApiResponse> dataList) {
        this.dataList = dataList;
    }

    @Override
    public PlantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.plants_card, parent, false);
        return new PlantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlantsViewHolder holder, int position) {
        ApiResponse currentPlant = dataList.get(position);
        holder.txt_tc.setText(Double.toString(currentPlant.getCanopyTemperature()));
        holder.txt_ta.setText(Double.toString(currentPlant.getAirTemperature()));
        holder.txt_soil.setText(Integer.toString(currentPlant.getSoilMoisture()));
        holder.txt_cwsi.setText(Double.toString(currentPlant.getCwsi()));
        holder.txt_rh.setText(Integer.toString(currentPlant.getRelativeHumidity()));
        holder.txt_weather.setText(currentPlant.getWeatherPrediction());
        holder.txt_irigasi.setText(Integer.toString(currentPlant.getDecision()));
        holder.txt_time.setText(currentPlant.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class PlantsViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_tc, txt_ta, txt_soil, txt_cwsi, txt_rh, txt_weather, txt_irigasi, txt_time;

        public PlantsViewHolder(View itemView) {
            super(itemView);
            txt_tc = (TextView) itemView.findViewById(R.id.txt_canopy_temperature);
            txt_ta = (TextView) itemView.findViewById(R.id.txt_air_temperature);
            txt_soil = (TextView) itemView.findViewById(R.id.txt_soil_moisture);
            txt_cwsi = (TextView) itemView.findViewById(R.id.txt_CWSI);
            txt_rh = (TextView) itemView.findViewById(R.id.txt_relative_humidity);
            txt_weather = (TextView) itemView.findViewById(R.id.txt_weather);
            txt_irigasi = (TextView) itemView.findViewById(R.id.txt_irigasi);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
        }
    }
}