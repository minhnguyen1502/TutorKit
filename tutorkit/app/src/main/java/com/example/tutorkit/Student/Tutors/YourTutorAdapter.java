package com.example.tutorkit.Student.Tutors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tutorkit.Models.Tutor;
import com.example.tutorkit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class YourTutorAdapter extends RecyclerView.Adapter<YourTutorAdapter.ViewHolder> {

    Context context;
    ArrayList<Tutor> tutorArrayList;
    DatabaseReference databaseReference;

    public YourTutorAdapter(Context context, ArrayList<Tutor> tutorArrayList) {
        this.context = context;
        this.tutorArrayList = tutorArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.your_tutor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tutor tutors = tutorArrayList.get(position);

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
                Toast.makeText(context, "I clicked this tutor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tutorArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtSubject;

        ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            txtName = itemView.findViewById(R.id.name);
            txtSubject = itemView.findViewById(R.id.subject);
        }
    }
}