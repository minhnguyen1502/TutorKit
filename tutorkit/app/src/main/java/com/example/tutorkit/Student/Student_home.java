package com.example.tutorkit.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tutorkit.Login;
import com.example.tutorkit.Models.Tutor;
import com.example.tutorkit.R;
import com.example.tutorkit.Support;
import com.example.tutorkit.Tutor.Assignment;
import com.example.tutorkit.Tutor.Grade;
import com.example.tutorkit.Tutor.Time_table;
import com.example.tutorkit.Tutor.Tuition_page;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student_home extends AppCompatActivity {

    ConstraintLayout tutor, calendar, grade, assignment, tuition, profile, support, logout;
    FirebaseAuth firebaseAuth;
    String name;
    TextView tv_name;
    ImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        tv_name = findViewById(R.id.tv_student);
        avatar = findViewById(R.id.avatar);

        tutor = (ConstraintLayout) findViewById(R.id.page_tutor);
        calendar = (ConstraintLayout) findViewById(R.id.page_calendar);
        grade = (ConstraintLayout) findViewById(R.id.page_grade);
        assignment = (ConstraintLayout) findViewById(R.id.page_assignment);
        tuition = (ConstraintLayout) findViewById(R.id.page_tuition);
        profile = (ConstraintLayout) findViewById(R.id.page_profile);
        support = (ConstraintLayout) findViewById(R.id.page_support);
        logout = (ConstraintLayout) findViewById(R.id.logout);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String tutorID = firebaseUser.getUid();

        //extracting tutor reference from database for registered tutor
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Student");
        databaseReference.child(tutorID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tutor tutor = snapshot.getValue(Tutor.class);
                if (tutor != null) {
                    name = firebaseUser.getDisplayName();
                    tv_name.setText(name);

                    Glide
                            .with(Student_home.this)
                            .load(tutor.getImg())
                            .centerCrop()
                            .into(avatar);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Student_home.this, "something wrong", Toast.LENGTH_SHORT).show();
            }
        });

        tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tutor_list.class);
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
                Intent intent = new Intent(getApplicationContext(), Tuition_page.class);
                startActivity(intent);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Student_home.this, Student_profile.class));

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
                Toast.makeText(Student_home.this, "Sign Out success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Student_home.this, Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

    }

}