package com.example.tutorkit.Tutor.Students;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Your_StudentAdpter extends RecyclerView.Adapter<Your_StudentAdpter.ViewHolder> {

    Context context;
    ArrayList<Student> studentArrayList;
    DatabaseReference databaseReference;

    public Your_StudentAdpter(Context context, ArrayList<Student> studentArrayList) {
        this.context = context;
        this.studentArrayList = studentArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public Your_StudentAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.your_student, parent, false);
        return new Your_StudentAdpter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Your_StudentAdpter.ViewHolder holder, int position) {
        Student students = studentArrayList.get(position);

         Glide
                .with(context)
                .load(students.getImg())
                .centerCrop()
                .into(holder.avatar);
        holder.txtName.setText(students.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "I clicked this student", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
       return studentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            txtName = itemView.findViewById(R.id.name);
        }
    }
}
