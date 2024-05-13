package com.example.trails.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trails.MainObservation;
import com.example.trails.ObservationDetail;
import com.example.trails.R;
import com.example.trails.activity.EditObservation;
import com.example.trails.database.Observation;

import java.util.ArrayList;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {
    public Context context;
    public ArrayList<Observation> obList;
    public MainObservation viewObservationActivity;

    class ObservationViewHolder extends RecyclerView.ViewHolder{
        public TextView o_observation, o_date, o_comment;
        public Button btnUpdate;
        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            this.o_observation = itemView.findViewById(R.id.observation);
            this.o_date = itemView.findViewById(R.id.date);
            this.o_comment = itemView.findViewById(R.id.comment);
            this.btnUpdate = itemView.findViewById(R.id.btn_edit);
        }
    }

    public ObservationAdapter(Context context, ArrayList<Observation> obList, MainObservation viewObservationActivity) {
        this.context = context;
        this.obList = obList;
        this.viewObservationActivity = viewObservationActivity;
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_observation, parent, false);
        ObservationViewHolder observationViewHolder = new ObservationViewHolder(view);
        return observationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        Observation observation = obList.get(position);
        int id = observation.getId();
        String ob = observation.getObservation();
        String date = observation.getDateOfTime();
        String comment = observation.getComment();

        holder.o_observation.setText(ob);
        holder.o_date.setText(date);
        holder.o_comment.setText(comment);

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(viewObservationActivity, EditObservation.class);
                i.putExtra("id", id);
                i.putExtra("observation", ob);
                i.putExtra("date", date);
                i.putExtra("comment", comment);
                viewObservationActivity.startActivityForResult(i, 1);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(viewObservationActivity, ObservationDetail.class);
                i.putExtra("observationID", id);
                viewObservationActivity.startActivityForResult(i, 1);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(obList != null){
            return obList.size();
        }
        return 0;
    }

}
