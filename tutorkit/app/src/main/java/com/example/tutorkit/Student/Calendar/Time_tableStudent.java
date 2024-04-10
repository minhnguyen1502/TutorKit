package com.example.tutorkit.Student.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.tutorkit.Models.TimeTable;
import com.example.tutorkit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Time_tableStudent extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<TimeTable> timeTableArrayList;
    ArrayList<String> idStudent;
    TimeTableStudentAdapter timeTableAdapter;
    private CalendarView calendarView;
    Calendar calendar;
    FloatingActionButton viewAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        timeTableArrayList = new ArrayList<>();
        calendarView = findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        idStudent = new ArrayList<>();
        viewAll = findViewById(R.id.viewAll);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                calendar.set(Calendar.DAY_OF_MONTH,i2);
                calendar.set(Calendar.MONTH,(i1+1) );
                calendar.set(Calendar.YEAR,i);
                String date = "" +calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR);
                Log.e("TAG", "onSelectedDayChange: "+i+i1+i2 );
                readDataForSelectedDate(date);            }
        });
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Time_tableStudent.this, ViewAll.class);
                startActivity(i);
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    private void readDataForSelectedDate(String selectedDate) {
        databaseReference.child("calendar").orderByChild("date").equalTo(selectedDate).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timeTableArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TimeTable time_table = dataSnapshot.getValue(TimeTable.class);
                    try {
                        if (Objects.equals(time_table.getIdStudent(), FirebaseAuth.getInstance().getUid())){
                            timeTableArrayList.add(time_table);
                        }
                    }catch (Exception e){
                        Log.e("TAG", "onDataChange: "+e.getMessage() );
                    }

                }
                timeTableAdapter = new TimeTableStudentAdapter(Time_tableStudent.this, timeTableArrayList);
                recyclerView.setAdapter(timeTableAdapter);
                timeTableAdapter.notifyDataSetChanged();
                Log.e("TAG", "onDataChange: "+timeTableArrayList );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Database error: " + error.getMessage());
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {
            Toast.makeText(this, "can't add", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.home){
            finish();

        }else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}