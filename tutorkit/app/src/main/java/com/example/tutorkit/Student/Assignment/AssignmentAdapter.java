package com.example.tutorkit.Student.Assignment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorkit.Models.SubmitAssignmentModel;
import com.example.tutorkit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {

    Context context;
    ArrayList<SubmitAssignmentModel> submitAssignmentModelArrayList;
    DatabaseReference databaseReference;

    public AssignmentAdapter(Context context, ArrayList<SubmitAssignmentModel> submitAssignmentModelArrayList) {
        this.context = context;
        this.submitAssignmentModelArrayList = submitAssignmentModelArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.submit_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SubmitAssignmentModel submitAssignmentModel = submitAssignmentModelArrayList.get(position);

        holder.dateline.setText("Dateline : " + submitAssignmentModel.getDateline());
        holder.title.setText("Title : " + submitAssignmentModel.getTitle());

        holder.name.setVisibility(View.GONE);
        holder.buttonUpdate.setText("Submit");
        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "I submit now", Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonDelete.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return submitAssignmentModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView title;
        TextView dateline;
        Button buttonDelete;
        Button buttonUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_name);
            title = itemView.findViewById(R.id.txt_title);
            dateline = itemView.findViewById(R.id.txt_date);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
        }
    }
}
