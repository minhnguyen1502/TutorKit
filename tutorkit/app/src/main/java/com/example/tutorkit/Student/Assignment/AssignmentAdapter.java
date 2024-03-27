package com.example.tutorkit.Student.Assignment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        DatabaseReference tutorRef = FirebaseDatabase.getInstance().getReference("tutors")
                .child(submitAssignmentModel.getIdTutor());
        tutorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tutorName = dataSnapshot.child("name").getValue(String.class);
                    if (tutorName != null) {
                        holder.name.setText("Tutor: " + tutorName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database read error
                Log.e("AssignmentAdapter", "Failed to read tutor data", databaseError.toException());
            }
        });

        holder.buttonDelete.setVisibility(View.GONE);
        holder.buttonUpdate.setText("Submit");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date selectedDate = sdf.parse(submitAssignmentModel.getDateline());
            Date currentDate = new Date();

            if (selectedDate != null) {
                if (!selectedDate.after(currentDate)) {
                    // The selected date is in the future
                    holder.buttonUpdate.setEnabled(false);
                    holder.buttonUpdate.setText("Submission deadline");
                } else {
                    // The selected date is not in the future
                    holder.buttonUpdate.setEnabled(true);
                    holder.buttonUpdate.setVisibility(View.VISIBLE);
                    holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Handle submission logic here
                            Toast.makeText(context, "Submitting...", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context, Submit.class);
                            i.putExtra("idSubmit", submitAssignmentModel.getId());
                            context.startActivity(i);

                        }
                    });
                }
            } else {
                // Handle case where parsing failed
                Toast.makeText(context, "Invalid date format", Toast.LENGTH_SHORT).show();
                holder.buttonUpdate.setEnabled(false);
                holder.buttonUpdate.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parsing exception if needed
            Toast.makeText(context, "Invalid date format", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "testDateline: " );
            // Since the date format is invalid, you might want to disable the button
            holder.buttonUpdate.setEnabled(false);
            holder.buttonUpdate.setVisibility(View.GONE);
        }
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
