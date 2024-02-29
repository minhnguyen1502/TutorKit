package com.example.tutorkit.Tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.Tutor;
import com.example.tutorkit.R;
import com.example.tutorkit.Student.Adapter.TutorAdapter;
import com.example.tutorkit.Student.Tutor_list;
import com.example.tutorkit.Student.Your_tutor;
import com.example.tutorkit.Tutor.Adapter.StudentAdapter;
import com.example.tutorkit.UpdateEmail;
import com.example.tutorkit.UpdatePassword;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_list extends AppCompatActivity {

    DatabaseReference databaseReference;
    TextView your_student;

    RecyclerView recyclerView;
    ArrayList<Student> studentArrayList;
    StudentAdapter adapter;
    ArrayList<String> idStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        idStudent = new ArrayList<>();
        your_student = findViewById(R.id.txt_your_student);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentArrayList = new ArrayList<>();

        your_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Student_list.this, Your_tutor.class));
            }
        });

        showListStudents();


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

//        FirebaseDatabase.getInstance().getReference("tutors").child("SzD4Qw7ClDRbm7HuqMTHaa5OcOB2").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.e("TAG", "onDataChange: ");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void showListStudents() {
        databaseReference.child("tutors").child(FirebaseAuth.getInstance().getUid()).child("IdStudent");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    idStudent.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    if (!dataSnapshot.getValue(Boolean.class)){
//                           idStudent.add(dataSnapshot.getKey());
//                    }
                    Log.e("TAG", "test: "+ dataSnapshot.getValue());
                }
                if (idStudent.size()>0){
                    for (int i =0; i<idStudent.size();i++){
                        databaseReference.child("Student").child(idStudent.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                studentArrayList.add(snapshot.getValue(Student.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
                adapter = new StudentAdapter(Student_list.this, studentArrayList);
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
        getMenuInflater().inflate(R.menu.tool_bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent i = new Intent(Student_list.this, Tutor_home.class);
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