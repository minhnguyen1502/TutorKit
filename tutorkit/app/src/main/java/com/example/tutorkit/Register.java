package com.example.tutorkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tutorkit.Student.Account.Student_register;
import com.example.tutorkit.Tutor.Account.Tutor_register;

public class Register extends AppCompatActivity {

    private TextView tutor, student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tutor = findViewById(R.id.tutor);
        student = findViewById(R.id.student);

        tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Tutor_register.class);
                startActivity(i);
                finish();
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Student_register.class));
                finish();
            }
        });
    }
}