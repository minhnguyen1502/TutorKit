package com.example.trails.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trails.database.DBHelper;
import com.example.trails.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddObservation extends AppCompatActivity {

    public EditText o_observation, o_dateOfTime, o_comment;
    public Button btnAdd, btnCancel;
    public DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_observation);
        //
        mapping();
        Date currentTime = new Date();

        // Create a date format to format the time as a string
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedTime = sdf.format(currentTime);

        // Now you can display the current time in your UI, e.g., in a TextView
        o_dateOfTime.setText("" + formattedTime);
        //
        db = new DBHelper(this);
        Intent intent = getIntent();
        int hikeID = intent.getIntExtra("hikeID", -1);
        //
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String observationText = o_observation.getText().toString().trim();
                String dateOfTimeText = o_dateOfTime.getText().toString().trim();
                String commentText = o_comment.getText().toString().trim();

                if (observationText.isEmpty() || dateOfTimeText.isEmpty() || commentText.isEmpty()) {
                    Toast.makeText(AddObservation.this, "Please complete all information", Toast.LENGTH_SHORT).show();
                } else {
                    long id = db.addObservation(
                            observationText,
                            dateOfTimeText,
                            commentText,
                            hikeID
                    );
                    Toast.makeText(AddObservation.this, "Create Successfully " + id, Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

    }
    public void mapping(){
        o_observation = findViewById(R.id.observation);
        o_dateOfTime = findViewById(R.id.date);
        o_comment = findViewById(R.id.comment);
        btnAdd = findViewById(R.id.btn_add);
        btnCancel = findViewById(R.id.btn_cancel);
    }
}