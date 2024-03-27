package com.example.tutorkit.Student;

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
import com.example.tutorkit.Student.Account.Student_profile;
import com.example.tutorkit.Student.Assignment.Assignment;
import com.example.tutorkit.Student.Call.Call_Tutor;
import com.example.tutorkit.Student.Chat.ChatWithTutor;
import com.example.tutorkit.Student.Exam.ExamStudent;
import com.example.tutorkit.Student.Grade.Grade_Page;
import com.example.tutorkit.Student.Payment.Payment;
import com.example.tutorkit.Student.Tutors.Tutor_list;
import com.example.tutorkit.Support;
import com.example.tutorkit.Tutor.Calendar.Time_table;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student_home extends AppCompatActivity {

    ConstraintLayout tutor, calendar, grade, assignment, tuition, profile, support, logout,exam;
    LinearLayout call,chat;
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

        tutor =  findViewById(R.id.page_tutor);
        exam = findViewById(R.id.page_exam);
        calendar =  findViewById(R.id.page_calendar);
        grade =  findViewById(R.id.page_grade);
        assignment =  findViewById(R.id.page_assignment);
        tuition = findViewById(R.id.page_tuition);
        profile = findViewById(R.id.page_profile);
        support = findViewById(R.id.page_support);
        logout = findViewById(R.id.logout);
        call = findViewById(R.id.call);
        chat = findViewById(R.id.chat);

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
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Call_Tutor.class);
                startActivity(intent);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatWithTutor.class);
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
                Intent intent = new Intent(getApplicationContext(), Grade_Page.class);
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
                Intent intent = new Intent(getApplicationContext(), Payment.class);
                startActivity(intent);
            }
        });
        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExamStudent.class);
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