package com.example.tutorkit.Student.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.TimeTable;
import com.example.tutorkit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TimeTableStudentAdapter extends RecyclerView.Adapter<TimeTableStudentAdapter.ViewHolder> {
    Context context;
    ArrayList<TimeTable> timeTables;
    DatabaseReference databaseReference;

    public TimeTableStudentAdapter(Context context, ArrayList<TimeTable> timeTables) {
        this.context = context;
        this.timeTables = timeTables;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.calendar, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TimeTable timeTable = timeTables.get(position);
        DatabaseReference tutorRef = FirebaseDatabase.getInstance().getReference("tutors")
                .child(timeTable.getIdTutor());
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
        holder.time.setText("Time : " + timeTable.getTime());
        holder.date.setText("Date : " + timeTable.getDate());


        holder.update.setVisibility(View.GONE);

        holder.delete.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return timeTables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView time;
        TextView date;
        ImageView delete;
        ImageView update;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_name);
            time = itemView.findViewById(R.id.txt_time);
            date = itemView.findViewById(R.id.txt_date);

            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
        }
    }
    }

