package com.example.trails.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trails.database.DBHelper;
import com.example.trails.R;

public class EditObservation extends AppCompatActivity {

    public EditText O_observation, O_date, O_comment;
    private Button btnSave, btnCancel;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_observation);

        mapping();
        db = new DBHelper(this);
        //
        Intent i = getIntent();
        String name = i.getStringExtra("observation");
        String date = i.getStringExtra("date");
        String comment = i.getStringExtra("comment");
        O_observation.setText(name);
        O_date.setText(date);
        O_comment.setText(comment);
        //
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String observation = O_observation.getText().toString().trim();
                String date = O_date.getText().toString().trim();
                String comment = O_comment.getText().toString().trim();
                if (observation.isEmpty() || date.isEmpty() || comment.isEmpty()) {
                    Toast.makeText(EditObservation.this, "Please complete all information", Toast.LENGTH_SHORT).show();
                } else {
                    int id = getIntent().getIntExtra("id", 0);
                    db.eitObservation(id, observation, date, comment);
                    Toast.makeText(EditObservation.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
        //
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void mapping(){
        O_observation = findViewById(R.id.observation);
        O_date = findViewById(R.id.date);
        O_comment = findViewById(R.id.comment);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
    }
}