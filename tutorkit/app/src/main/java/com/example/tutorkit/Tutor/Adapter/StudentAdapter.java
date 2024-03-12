package com.example.tutorkit.Tutor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorkit.Models.StatusAdd;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StudentAdapter  extends RecyclerView.Adapter<StudentAdapter.ViewHolder>{
    Context context;
    ArrayList<Student> studentArrayList;
    DatabaseReference databaseReference;

    public StudentAdapter(Context context, ArrayList<Student> studentArrayList) {
        this.context = context;
        this.studentArrayList = studentArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student students = studentArrayList.get(position);

//         Glide
//                .with(TutorAdapter.this)
//                .load(students.getImg())
//                .centerCrop()
//                .into(holder.avatar);
        holder.txtName.setText(students.getName());

        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("tutors")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("IdStudent").child(students.getId())
                        .setValue(new StatusAdd(students.getId(), true));
                
                Toast.makeText(context, "I choose this student", Toast.LENGTH_SHORT).show();

            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("tutors")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("IdStudent").child(students.getId())
                        .removeValue();
                Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
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
        Button confirm, cancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            txtName = itemView.findViewById(R.id.name);

            confirm = itemView.findViewById(R.id.btn_confirm);
            cancel = itemView.findViewById(R.id.btn_cancel);
        }
    }
}
