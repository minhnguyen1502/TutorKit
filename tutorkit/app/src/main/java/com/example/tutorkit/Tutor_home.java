package com.example.tutorkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Tutor_home extends AppCompatActivity {

    ConstraintLayout student, calendar, grade, assignment, tuition, profile, support, logout;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);

        student = (ConstraintLayout) findViewById(R.id.student);
        calendar = (ConstraintLayout) findViewById(R.id.calendar);
        grade = (ConstraintLayout) findViewById(R.id.grade);
        assignment = (ConstraintLayout) findViewById(R.id.assignment);
        tuition = (ConstraintLayout) findViewById(R.id.tuition);
        profile = (ConstraintLayout) findViewById(R.id.profile);
        support = (ConstraintLayout) findViewById(R.id.support);
        logout = (ConstraintLayout) findViewById(R.id.logout);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Student.class);
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
                Intent intent = new Intent(getApplicationContext(), Grade.class);
                startActivity(intent);

            }
        });

        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Assignment.class);
                startActivity(intent);

            }
        });

        tuition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tuition.class);
                startActivity(intent);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tutor_Profile.class);
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