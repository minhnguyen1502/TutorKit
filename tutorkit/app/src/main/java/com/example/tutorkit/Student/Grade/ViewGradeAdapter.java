package com.example.tutorkit.Student.Grade;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorkit.Models.Grade;
import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Grade.GradeAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewGradeAdapter extends RecyclerView.Adapter<ViewGradeAdapter.ViewHolder> {

    Context context;
    ArrayList<Grade> gradeArrayList;
    DatabaseReference databaseReference;

    public ViewGradeAdapter(Context context, ArrayList<Grade> gradeArrayList) {
        this.context = context;
        this.gradeArrayList = gradeArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.grade, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Grade grade = gradeArrayList.get(position);

        holder.type.setText(" " + grade.getType());
        holder.title.setText("Title: " + grade.getTitle());
        holder.grade.setText("Garde: " + grade.getGrade());
        holder.date.setText("Date : " + grade.getDate());

        holder.buttonDelete.setVisibility(View.GONE);
        holder.buttonUpdate.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return gradeArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView type;
        TextView title;
        TextView grade;
        TextView date;
        Button buttonDelete;
        Button buttonUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.txt_type);
            title = itemView.findViewById(R.id.txt_title);
            grade = itemView.findViewById(R.id.txt_grade);
            date = itemView.findViewById(R.id.txt_date);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
        }
    }

}