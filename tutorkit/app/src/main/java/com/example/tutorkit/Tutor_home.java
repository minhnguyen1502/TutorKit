package com.example.tutorkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Tutor_home extends AppCompatActivity {

    ConstraintLayout student, calendar, grade, assignment, tuition, profile, support, logout;

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
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);

            }
        });

    }
}