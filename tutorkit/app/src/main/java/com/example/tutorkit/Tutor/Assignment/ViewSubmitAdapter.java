package com.example.tutorkit.Tutor.Assignment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tutorkit.Models.AssignmentModel;
import com.example.tutorkit.R;
import com.example.tutorkit.Student.Assignment.SubmitAdpater;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewSubmitAdapter extends RecyclerView.Adapter<ViewSubmitAdapter.ViewHolder> {

    Context context;
    ArrayList<AssignmentModel> assignmentModelArrayList;
    DatabaseReference databaseReference;

    public ViewSubmitAdapter(Context context, ArrayList<AssignmentModel> assignmentModelArrayList) {
        this.context = context;
        this.assignmentModelArrayList = assignmentModelArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AssignmentModel assignmentModel = assignmentModelArrayList.get(position);

        Glide
                .with(context)
                .load(assignmentModel.getImg())
                .centerCrop()
                .into(holder.img);
        holder.close.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return assignmentModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        ImageView img;
        ImageView close;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            close = itemView.findViewById(R.id.close);
            img = itemView.findViewById(R.id.img);

        }
    }
}
