package com.example.trails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trails.database.DBHelper;
import com.example.trails.database.Hike;

public class HikeDetail extends AppCompatActivity {

    public TextView h_id, h_name, h_location, h_date, h_length, h_level, h_description, h_parking,h_vehicle;
    public DBHelper dbHelper;
    public int id;
    public Button btnDelete, btnBack;
    public Hike hike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hike_detail);
        //
        mapping();
        //
        dbHelper = new DBHelper(this);
        //
        Intent i = getIntent();
        id = i.getIntExtra("hikeID", 0);

        LoadData();
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteHike(id);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("deletedHikeId", id);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void mapping(){
        h_id = findViewById(R.id.tv_id);
        h_name = findViewById(R.id.tv_name);
        h_location = findViewById(R.id.tv_location);
        h_date = findViewById(R.id.tv_date_of_hike);
        h_length = findViewById(R.id.tv_length_the_hike);
        h_level = findViewById(R.id.tv_level_of_difficulty);
        h_vehicle = findViewById(R.id.tv_vehicle);
        h_description = findViewById(R.id.tv_description);
        h_parking = findViewById(R.id.tv_parking_available);
        btnDelete = findViewById(R.id.btn_delete);
        btnBack = findViewById(R.id.btn_back);
    }

    private void LoadData() {
        hike = dbHelper.getHikeById(id);
        if (hike != null) {
            h_id.setText(String.valueOf(hike.getId()));
            h_name.setText(hike.getName());
            h_location.setText(hike.getLocation());
            h_date.setText(hike.getDate());
            h_description.setText(hike.getDescription());
            h_level.setText(hike.getLevel());
            h_vehicle.setText(hike.getVehicle());
            h_length.setText(String.valueOf(hike.getLength()));
            h_parking.setText(hike.isParking() == 1 ? "Yes" : "No");
        }
    }
}