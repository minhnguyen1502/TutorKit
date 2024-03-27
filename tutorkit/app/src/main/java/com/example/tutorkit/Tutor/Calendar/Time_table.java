package com.example.tutorkit.Tutor.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tutorkit.Models.TimeTable;
import com.example.tutorkit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Time_table extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<TimeTable> timeTableArrayList;
    TimeTableAdapter timeTableAdapter;
    private CalendarView calendarView;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        timeTableArrayList = new ArrayList<>();
        calendarView = findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();
        recyclerView = findViewById(R.id.recyclerView);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                calendar.set(Calendar.DAY_OF_MONTH,i2);
                calendar.set(Calendar.MONTH,(i1+1) );
                calendar.set(Calendar.YEAR,i);
                Log.e("TAG", "onSelectedDayChange: "+i+i1+i2 );
//                calendarClicked();
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        readData();

    }

    private void readData() {
        databaseReference.child("calendar").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timeTableArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TimeTable time_table = dataSnapshot.getValue(TimeTable.class);
                    timeTableArrayList.add(time_table);
                    Log.e("TAG", "displayData: " );
                }
                timeTableAdapter = new TimeTableAdapter(Time_table.this, timeTableArrayList);
                recyclerView.setAdapter(timeTableAdapter);
                timeTableAdapter.notifyDataSetChanged();
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
        // update profile
        if (id == R.id.add) {
            ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
            viewDialogAdd.showDialog(Time_table.this);
            Toast.makeText(this, "add new", Toast.LENGTH_SHORT).show();
//            finish();
        } else if (id == R.id.home) {
            finish();

        } else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private class ViewDialogAdd {
        public void showDialog(Context context) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_add_calendar);

            EditText edtName = dialog.findViewById(R.id.edt_name);
            EditText edtTime = dialog.findViewById(R.id.edt_time);

            Button buttonAdd = dialog.findViewById(R.id.buttonAdd);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
            edtTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            Time_table.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    // Do something with the selected time
                                    String time = hourOfDay + ":" + minute;
                                    edtTime.setText(time);
                                    Toast.makeText(Time_table.this, "Selected time: " + time, Toast.LENGTH_SHORT).show();
                                }
                            },
                            hour,
                            minute,
                            false);

                    timePickerDialog.show();
                }
            });

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = "tuition" + new Date().getTime();
                    String name = edtName.getText().toString();
                    String time = edtTime.getText().toString();
                    long date = calendar.getTimeInMillis();


                    databaseReference.child("calendar").child(id)
                            .setValue(new TimeTable(id,name,time,date));
                    Toast.makeText(context, "DONE!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });


            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }
}