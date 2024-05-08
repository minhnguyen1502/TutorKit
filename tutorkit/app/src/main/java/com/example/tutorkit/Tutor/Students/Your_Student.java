package com.example.tutorkit.Tutor.Students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorkit.Models.StatusAdd;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Tutor_home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Your_Student extends AppCompatActivity {

    DatabaseReference databaseReference;
    TextView student_list;
    RecyclerView recyclerView;
    ArrayList<Student> studentArrayList;
    Your_StudentAdpter adapter;
    ArrayList<String> idStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_student);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        idStudent = new ArrayList<>();
        student_list = findViewById(R.id.txt_list_student);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentArrayList = new ArrayList<>();

        adapter = new Your_StudentAdpter(Your_Student.this, studentArrayList);
        recyclerView.setAdapter(adapter);

        student_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Your_Student.this, Student_list.class));
                finish();
            }
        });

        showListStudents();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

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
                                studentArrayList.add(snapshot.getValue(Student.class));
                                adapter.notifyDataSetChanged();
                                Log.e("TAG", "onDataChange: " + studentArrayList);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent i = new Intent(Your_Student.this, Tutor_home.class);
            startActivity(i);
            finish();
        } else if (id == R.id.action_search) {
//            Intent i = new Intent(Tutor_profile.this, UpdateEmail.class);
//            startActivity(i);
//            finish();
        } else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}