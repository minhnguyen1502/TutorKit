package com.example.trails.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

public class AddHike extends AppCompatActivity {
    public Button btnAdd, btncancel;
    public EditText h_name, h_location, h_date, h_length, h_description;
    public Switch h_parking;
    public Spinner h_level, h_vehicle;
    private DBHelper db;
    private ArrayList<Hike> hikeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_hike);

        db = new DBHelper(this);
        //
        mapping();
        //
        btncancel.setOnClickListener(view -> {
            finish();
        });

        h_date.setOnClickListener(view -> {
            MyDatePicker dlg = new MyDatePicker();
            dlg.setDateField(h_date);
            dlg.show(getSupportFragmentManager(), "Hike Date!");
        });

        btnAdd.setOnClickListener(view -> {
            String nameValue = h_name.getText().toString();
            String locationValue = h_location.getText().toString();
            String dateValue = h_date.getText().toString();
            String selectedLevel = h_level.getSelectedItem().toString();
            String selectedVehicle = h_vehicle.getSelectedItem().toString();
            String descriptionValue = h_description.getText().toString();
            String lengthValue = h_length.getText().toString();
            if (nameValue.isEmpty() || locationValue.isEmpty() ||
                dateValue.isEmpty() || descriptionValue.isEmpty() ||
                    lengthValue.isEmpty()) {
                Toast.makeText(AddHike.this, "Please complete all information", Toast.LENGTH_SHORT).show();
            } else {
                int parkingValue = h_parking.isChecked() ? 1 : 0;
                try {
                    double length = Double.parseDouble(lengthValue);
                    long id = db.addHike(nameValue, locationValue, dateValue, selectedLevel, descriptionValue,selectedVehicle, length, parkingValue);
                    Toast.makeText(AddHike.this, "Create Successfully " + id, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent();
                    setResult(RESULT_OK, i);
                    finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(AddHike.this, "Invalid length format", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void mapping(){
        btnAdd = findViewById(R.id.btn_add);
        btncancel = findViewById(R.id.btn_cancel);
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


