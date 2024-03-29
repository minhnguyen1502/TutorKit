package com.example.tutorkit.Tutor.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tutorkit.Models.StatusAdd;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.TimeTable;
import com.example.tutorkit.R;
import com.google.firebase.auth.FirebaseAuth;
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
    ArrayList<Student> studentArrayList;
    ArrayList<String> idStudent;

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
        studentArrayList = new ArrayList<>();
        idStudent = new ArrayList<>();

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
        showListStudents();
    }
    private void showListStudents() {
        FirebaseDatabase.getInstance().getReference("tutors")
                .child(FirebaseAuth.getInstance().getUid())
                .child("IdStudent").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        idStudent.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            StatusAdd statusAdd = dataSnapshot.getValue(StatusAdd.class);
                            try {
                                if (statusAdd.getStatus()) {
                                    idStudent.add(statusAdd.getIdList());
                                }
                            } catch (Exception e) {
                                Log.e("TAG", "onDataChange: " + e.getMessage());
                            }
                        }
                        if (idStudent.size() > 0) {
                            for (int i = 0; i < idStudent.size(); i++) {
                                databaseReference.child("Student")
                                        .child(idStudent.get(i))
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                                                    Student student = new Student();
                                                Student student = snapshot.getValue(Student.class);
                                                studentArrayList.add(student);
//                                                }
                                                Log.e("TAG", "log test: " + studentArrayList);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                            }

                        } else {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

            Spinner studentName = dialog.findViewById(R.id.spn_name);
            EditText edtTime = dialog.findViewById(R.id.edt_time);

            Button buttonAdd = dialog.findViewById(R.id.buttonAdd);

            //            if (studentArrayList.get(0)!=studentDefault){
//                studentArrayList.add(0,studentDefault);
//            }
            ArrayAdapter<Student> studentList = new ArrayAdapter<Student>(context, android.R.layout.simple_list_item_1,studentArrayList){
//                @Override
//                public boolean isEnabled(int position) {
////                    position = 0 not select
//                    return position != 0;
//                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                    view.setText(studentArrayList.get(position).getName());
//                    if (position == 0) {
//                        view.setTextColor(Color.GRAY);
//                    } else {
//                        view.setTextColor(Color.BLACK);
//                    }
                    return view;
                }

                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setText(studentArrayList.get(position).getName());
                    return view;
                }
            };
            studentName.setAdapter(studentList);
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
            ImageView cancel = dialog.findViewById(R.id.cancel);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Student student = (Student) studentName.getSelectedItem();
                    String idTutor = FirebaseAuth.getInstance().getUid();
                    String id = "tuition" + new Date().getTime();
                    String name = student.getName();

                    String time = edtTime.getText().toString();
                    long date = calendar.getTimeInMillis();

                    if (TextUtils.isEmpty(time)){
                        Toast.makeText(context, "Enter dateline", Toast.LENGTH_SHORT).show();
                        edtTime.setError("required");
                        edtTime.requestFocus();

                    }else {
                        databaseReference.child("calendar").child(id)
                                .setValue(new TimeTable(id,name,time,idTutor,student.getId(),date));
                        Toast.makeText(context, "DONE!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }
            });


            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }
}