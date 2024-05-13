package com.example.trails.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trails.HikeDetail;
import com.example.trails.MainHike;
import com.example.trails.MainObservation;
import com.example.trails.R;
import com.example.trails.activity.EditHike;
import com.example.trails.database.Hike;

import java.util.ArrayList;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {
    public Context context;
    public ArrayList<Hike> hikeList;
    public MainHike mainActivity;


    public HikeAdapter(Context context, ArrayList<Hike> hikeList, MainHike mainActivity) {
        this.context = context;
        this.hikeList = hikeList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hike, parent, false);
        HikeViewHolder hikeViewHolder = new HikeViewHolder(view);
        return hikeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        Hike hike = hikeList.get(position);
        int idH = hike.getId();
        String nameH = hike.getName();
        String descriptionH = hike.getDescription();
        String dateH = hike.getDate();
        String locationH = hike.getLocation();
        String levelH = hike.getLevel();
        String vehicleH = hike.getVehicle();
        double lengthH = hike.getLength();
        int parkingH = hike.isParking();

        holder.h_name.setText(nameH);
        holder.h_location.setText(locationH);
        holder.h_date.setText(dateH);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mainActivity, HikeDetail.class);
                i.putExtra("hikeID", idH);
                mainActivity.startActivityForResult(i, 1);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mainActivity, EditHike.class);
                i.putExtra("id", idH);
                i.putExtra("name", nameH);
                i.putExtra("location", locationH);
                i.putExtra("date", dateH);
                i.putExtra("level", levelH);
                i.putExtra("vehicle", vehicleH);
                i.putExtra("description", descriptionH);
                i.putExtra("length", lengthH);
                i.putExtra("parking", parkingH == 1);
                mainActivity.startActivityForResult(i, 2);
            }
        });

        holder.btnObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mainActivity, MainObservation.class);
                i.putExtra("hikeID", idH);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(hikeList != null){
            return hikeList.size();
        }
        return 0;
    }

    class HikeViewHolder extends RecyclerView.ViewHolder{
        public TextView h_name, h_location, h_date;
        public Switch parking;
        public Button btnEdit, btnObservation;
        public HikeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.h_name = itemView.findViewById(R.id.name);
            this.h_date = itemView.findViewById(R.id.date);
            this.h_location = itemView.findViewById(R.id.location);
            this.parking = itemView.findViewById(R.id.parking);
            this.btnEdit = itemView.findViewById(R.id.btn_edit);
            this.btnObservation = itemView.findViewById(R.id.btn_observation);
        }
    }
}
