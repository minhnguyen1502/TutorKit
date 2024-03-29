package com.example.tutorkit.Tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tutorkit.Login;
import com.example.tutorkit.Models.Tutor;
import com.example.tutorkit.R;
import com.example.tutorkit.Support;
import com.example.tutorkit.Tutor.Account.Tutor_profile;
import com.example.tutorkit.Tutor.Assignment.SubmitAssignment;
import com.example.tutorkit.Tutor.Calendar.Time_table;
import com.example.tutorkit.Tutor.Call.Call_Student;
import com.example.tutorkit.Tutor.Chat.ChatWithStudent;
import com.example.tutorkit.Tutor.Exam.Exam_Page;
import com.example.tutorkit.Tutor.Grade.GradePage;
import com.example.tutorkit.Tutor.Students.Student_list;
import com.example.tutorkit.Tutor.Tuition.Tuition_page;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tutor_home extends AppCompatActivity {

    ConstraintLayout student, calendar, grade, assignment, tuition, profile, support, logout, exam;
    LinearLayout call, chat;
    FirebaseAuth firebaseAuth;
    String name;
    TextView tv_name;
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);

        tv_name = findViewById(R.id.tv_name);
        avatar = findViewById(R.id.avatar);

        student = findViewById(R.id.student);
        calendar =  findViewById(R.id.calendar);
        exam =  findViewById(R.id.exam);
        grade =  findViewById(R.id.grade);
        assignment = findViewById(R.id.assignment);
        tuition =  findViewById(R.id.tuition);
        profile = findViewById(R.id.profile);
        support =  findViewById(R.id.support);
        logout =  findViewById(R.id.logout);
        call = findViewById(R.id.call);
        chat = findViewById(R.id.chat);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String tutorID = firebaseUser.getUid();

        //extracting tutor reference from database for registered tutor
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tutors");
        databaseReference.child(tutorID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tutor tutor = snapshot.getValue(Tutor.class);
                if (tutor != null) {
                    name = tutor.getName();
                    tv_name.setText(name);

                    Glide
                            .with(Tutor_home.this)
                            .load(tutor.getImg())
                            .centerCrop()
                            .into(avatar);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Tutor_home.this, "something wrong", Toast.LENGTH_SHORT).show();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Call_Student.class));
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChatWithStudent.class));
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Student_list.class);
                startActivity(intent);

            }
        });
        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Exam_Page.class);
                startActivity(intent);

            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Time_table.class);
                startActivity(intent);

            }
        });

        grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GradePage.class);
                startActivity(intent);

            }
        });

        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SubmitAssignment.class);
                startActivity(intent);

            }
        });

        tuition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tuition_page.class);
                startActivity(intent);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tutor_profile.class);
                startActivity(intent);

            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Support.class);
                startActivity(intent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Toast.makeText(Tutor_home.this, "Sign Out success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Tutor_home.this, Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                           | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

    }

}