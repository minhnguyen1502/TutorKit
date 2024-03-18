package com.example.tutorkit.Tutor.Exam;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Exam_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_page);

        Button startQuiz = findViewById(R.id.startQuiz);
        Button createQuiz = findViewById(R.id.createQuiz);
        RelativeLayout solvedQuizzes = findViewById(R.id.solvedQuizzes);
        RelativeLayout your_quizzes = findViewById(R.id.your_quizzes);
        EditText quiz_title = findViewById(R.id.quiz_title);
        EditText start_quiz_id = findViewById(R.id.start_quiz_id);
        ImageView signout = findViewById(R.id.signout);

        signout.setOnClickListener(view -> {
            finish();
        });

        createQuiz.setOnClickListener(v -> {
            if (quiz_title.getText().toString().equals("")) {
                quiz_title.setError("Quiz title cannot be empty");
                return;
            }
            Intent i = new Intent(Exam_Page.this, ExamEditor.class);
            i.putExtra("Quiz Title", quiz_title.getText().toString());
            quiz_title.setText("");
            startActivity(i);
        });

        startQuiz.setOnClickListener(v-> {
            if (start_quiz_id.getText().toString().equals("")) {
                start_quiz_id.setError("Quiz title cannot be empty");
                return;
            }
            Intent i = new Intent(Exam_Page.this, ExamList.class);
            i.putExtra("Quiz ID", start_quiz_id.getText().toString());
            start_quiz_id.setText("");
            startActivity(i);
        });

        solvedQuizzes.setOnClickListener(v -> {
            Intent i = new Intent(Exam_Page.this, ListQuizzes.class);
            i.putExtra("Operation", "List Solved Quizzes");
            startActivity(i);
        });

        your_quizzes.setOnClickListener(v -> {
            Intent i = new Intent(Exam_Page.this, ListQuizzes.class);
            i.putExtra("Operation", "List Created Quizzes");
            startActivity(i);
        });

    }
}