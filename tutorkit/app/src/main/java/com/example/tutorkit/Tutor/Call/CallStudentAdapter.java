package com.example.tutorkit.Tutor.Call;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CallStudentAdapter extends RecyclerView.Adapter<CallStudentAdapter.ViewHolder>{
    Context context;
    ArrayList<Student> studentArrayList;
    DatabaseReference databaseReference;
    public CallStudentAdapter(Context context, ArrayList<Student> studentArrayList) {
        this.context = context;
        this.studentArrayList = studentArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    @NonNull
    @Override
    public CallStudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.call_student, parent, false);
        return new CallStudentAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull CallStudentAdapter.ViewHolder holder, int position) {
        Student students = studentArrayList.get(position);

         Glide
                .with(context)
                .load(students.getImg())
                .centerCrop()
                .into(holder.avatar);
        holder.txtName.setText(students.getName());
        holder.txtName.setText(students.getName());

        holder.call_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + students.getParent_phone()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    // Handle the case where no activity can handle the intent
                    Toast.makeText(context, "No app available to handle this action", Toast.LENGTH_SHORT).show();
                }
            }

        });
        holder.call_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + students.getPhone()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    // Handle the case where no activity can handle the intent
                    Toast.makeText(context, "No app available to handle this action", Toast.LENGTH_SHORT).show();
                }
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
        Button call_parent, call_student;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            txtName = itemView.findViewById(R.id.name);

            call_parent = itemView.findViewById(R.id.btn_parent);
            call_student = itemView.findViewById(R.id.btn_student);
        }
    }
}
