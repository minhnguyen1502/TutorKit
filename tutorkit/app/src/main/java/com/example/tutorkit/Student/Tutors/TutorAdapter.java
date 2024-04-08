package com.example.tutorkit.Student.Tutors;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tutorkit.Models.StatusAdd;
import com.example.tutorkit.Models.Tutor;
import com.example.tutorkit.R;
import com.example.tutorkit.Student.Grade.ViewGrade;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.ViewHolder>{
    Context context;
    ArrayList<Tutor> tutorsArrayList;
    DatabaseReference databaseReference;

    public TutorAdapter(Context context, ArrayList<Tutor> tutorsArrayList) {
        this.context = context;
        this.tutorsArrayList = tutorsArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public TutorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.tutor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorAdapter.ViewHolder holder, int position) {

        Tutor tutors = tutorsArrayList.get(position);

         Glide
                .with(context)
                .load(tutors.getImg())
                .centerCrop()
                .into(holder.avatar);
        holder.txtName.setText(tutors.getName());
        holder.txtSubject.setText(tutors.getSubject());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ViewProfileTutor.class);
                i.putExtra("tutorId", tutors.getId());
                context.startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return tutorsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtSubject;
        ImageView avatar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            txtName = itemView.findViewById(R.id.name);
            txtSubject = itemView.findViewById(R.id.subject);

        }
    }
}
