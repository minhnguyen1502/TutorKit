package com.example.trails.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.trails.database.DBHelper;
import com.example.trails.database.Hike;
import com.example.trails.R;

import java.util.ArrayList;
import java.util.Calendar;

public class EditHike extends AppCompatActivity {


    public Button btnSave, btnCancel;
    public EditText h_name, h_location, h_date, h_length, h_description;
    public Switch h_parking;
    public Spinner h_level, h_vehicle;
    //
    private DBHelper db;
    private ArrayList<Hike> hikeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_hike);
        db = new DBHelper(this);
        //
        mapping();
        //
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String location = i.getStringExtra("location");
        String date = i.getStringExtra("date");
        double length = i.getDoubleExtra("length", 0.0);
        String level = i.getStringExtra("level");
        String vehicle = i.getStringExtra("vehicle");
        String description = i.getStringExtra("description");
        boolean parkingValue = getIntent().getBooleanExtra("parking", false);
        //
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.level, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        h_level.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.vehicle, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        h_vehicle.setAdapter(adapter1);
        //
        h_name.setText(name);
        h_location.setText(location);
        h_date.setText(date);
        h_level.setAdapter(adapter);
        h_vehicle.setAdapter(adapter1);
        h_length.setText(String.valueOf(length));
        h_description.setText(description);
        h_parking.setChecked(parkingValue);
        if (level != null) {
            int spinnerPosition = adapter.getPosition(level);
            h_level.setSelection(spinnerPosition);
        }

        if (vehicle != null) {
            int spinnerPosition = adapter1.getPosition(vehicle);
            h_vehicle.setSelection(spinnerPosition);
        }
        h_date.setOnClickListener(view -> {
            MyDatePicker dlg = new MyDatePicker();
            dlg.setDateField(h_date);
            dlg.show(getSupportFragmentManager(), "Hike Date!");
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = h_name.getText().toString();
                String location = h_location.getText().toString();
                String date = h_date.getText().toString();
                String lengthStr = h_length.getText().toString();
                String level = h_level.getSelectedItem().toString();
                String vehicle = h_vehicle.getSelectedItem().toString();
                String description = h_description.getText().toString();
                boolean parkingChecked = h_parking.isChecked();
                if (name.isEmpty() || location.isEmpty() || date.isEmpty() || lengthStr.isEmpty() || description.isEmpty()) {
                    Toast.makeText(EditHike.this, "Please complete all information", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        double length = Double.parseDouble(lengthStr);
                        int parkingValue = parkingChecked ? 1 : 0;
                        int id = getIntent().getIntExtra("id", 0);
                        db.editHike(id, name, location, date, level, description,vehicle, length, parkingValue);
                        Toast.makeText(EditHike.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent();
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (NumberFormatException e) {
                        Toast.makeText(EditHike.this, "Invalid length format. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void mapping(){
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        h_name = findViewById(R.id.name);
        h_location = findViewById(R.id.location);
        h_date = findViewById(R.id.date);
        h_length = findViewById(R.id.length);
        h_description = findViewById(R.id.description);
        h_parking = findViewById(R.id.parking);
        h_level = findViewById(R.id.level);
        h_vehicle = findViewById(R.id.vehicle);
    }

    public static class MyDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        public void setDateField(EditText dateField) {
            this.dateField = dateField;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            if (dateField.getText().length() != 0) {
                String date = dateField.getText().toString();
                String[] separated = date.split("/");
                int year = Integer.parseInt(separated[2]);
                int month = Integer.parseInt(separated[1]);
                int day = Integer.parseInt(separated[0]);
                return new DatePickerDialog(getActivity(), this, year, month - 1, day);

            } else {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(getActivity(), this, year, month, day);
            }
            // Create a new instance of DatePickerDialog and return it
        }

        private EditText dateField;

        @Override
        public void onDateSet(DatePicker datePicker, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String dateReturn = selectedDay + "/" + (selectedMonth + 1) + "/"
                    + selectedYear;
            dateField.setText(dateReturn);

        }
    }
}