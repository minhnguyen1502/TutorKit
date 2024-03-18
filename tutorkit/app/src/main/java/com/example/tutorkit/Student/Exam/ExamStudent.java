package com.example.tutorkit.Student.Exam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Exam.Answer.ExamList;
import com.example.tutorkit.Tutor.Exam.ExamEditor;
import com.example.tutorkit.Tutor.Exam.Exam_Page;
import com.example.tutorkit.Tutor.Exam.ListQuizzes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExamStudent extends AppCompatActivity {
//    String UID;
//    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tutors");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_student);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Button startQuiz = findViewById(R.id.startQuiz);
        RelativeLayout solvedQuizzes = findViewById(R.id.solvedQuizzes);
        EditText start_quiz_id = findViewById(R.id.start_quiz_id);
        ImageView signout = findViewById(R.id.signout);

//        UID = String.valueOf(databaseReference.child("id"));
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                DataSnapshot usersRef = snapshot.child("tutors").child(UID);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ExamStudent.this, "Can't connect", Toast.LENGTH_SHORT).show();

            }
        });

        signout.setOnClickListener(view -> {
            finish();
        });

        startQuiz.setOnClickListener(v-> {
            if (start_quiz_id.getText().toString().equals("")) {
                start_quiz_id.setError("Quiz title cannot be empty");
                return;
            }
            Intent i = new Intent(ExamStudent.this, ExamList.class);
            i.putExtra("Quiz ID", start_quiz_id.getText().toString());
            start_quiz_id.setText("");
            startActivity(i);
        });

        solvedQuizzes.setOnClickListener(v -> {
            Intent i = new Intent(ExamStudent.this, ListQuizzes.class);
            i.putExtra("Operation", "List Solved Quizzes");
            startActivity(i);
        });

    }
}