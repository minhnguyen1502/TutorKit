package com.example.tutorkit.Student.Assignment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorkit.Models.StatusAdd;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.SubmitAssignmentModel;
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
import java.util.Objects;

public class Assignment extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<SubmitAssignmentModel> submitAssignmentModelArrayList;
    ArrayList<Student> studentArrayList;
    AssignmentAdapter adapter;
    ArrayList<String> idStudent;
    String idTutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        submitAssignmentModelArrayList = new ArrayList<>();
        studentArrayList = new ArrayList<>();
        idStudent = new ArrayList<>();
        studentArrayList.add(new Student("Student Name","", "","","","",""));

        idTutor = getIntent().getStringExtra("idTutor");
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
        databaseReference.child("submitAssignment").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                submitAssignmentModelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SubmitAssignmentModel submitAssignmentModel = dataSnapshot.getValue(SubmitAssignmentModel.class);
                    try {
                        if (Objects.equals(submitAssignmentModel.getIdStudent(), FirebaseAuth.getInstance().getUid())){
                            if (Objects.equals(submitAssignmentModel.getIdTutor(), idTutor)){
                                submitAssignmentModelArrayList.add(submitAssignmentModel);

                            }
                        }
                    }catch (Exception e){
                        Log.e("TAG", "onDataChange: "+e.getMessage() );
                    }

                }
                adapter = new AssignmentAdapter(Assignment.this, submitAssignmentModelArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
        if (id == R.id.home) {
            finish();
        } else if (id == R.id.add) {
            Toast.makeText(this, "Can't add new", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}