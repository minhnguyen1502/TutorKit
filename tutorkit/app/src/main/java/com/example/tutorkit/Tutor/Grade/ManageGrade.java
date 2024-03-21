package com.example.tutorkit.Tutor.Grade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tutorkit.Models.Grade;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.Tuition;
import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Tuition.TuitionAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ManageGrade extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Grade> gradeArrayList;
    GradeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_grade);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gradeArrayList = new ArrayList<>();

        readData();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    private void readData() {

        databaseReference.child("grades").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gradeArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Grade users = dataSnapshot.getValue(Grade.class);
                    gradeArrayList.add(users);
                }
                adapter = new GradeAdapter(ManageGrade.this, gradeArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class ViewDialogAdd {
        public void showDialog(Context context) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_add_grade);

            Spinner spn_type = dialog.findViewById(R.id.spn_type);
            EditText edt_title = dialog.findViewById(R.id.edt_title);
            EditText edt_grade = dialog.findViewById(R.id.edt_grade);
            EditText edt_date = dialog.findViewById(R.id.edt_date);

            Button buttonAdd = dialog.findViewById(R.id.buttonAdd);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            edt_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    // Người dùng đã chọn ngày. Cập nhật trường ngày (edate).
                                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                    edt_date.setText(selectedDate);
                                }
                            },
                            // Truyền ngày hiện tại làm ngày mặc định cho DatePickerDialog.
                            Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    );

                    datePickerDialog.show();
                }
            });

            buttonAdd.setText("ADD");
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = "grade" + new Date().getTime();
                    String type = spn_type.getSelectedItem().toString();
                    String title = edt_title.getText().toString();
                    int grade = Integer.parseInt(edt_grade.getText().toString());
                    String date = edt_date.getText().toString();

//                    if (type.isEmpty() || title.isEmpty() || grade.isEmpty() || date.isEmpty()) {
//                        Toast.makeText(context, "Please Enter All data...", Toast.LENGTH_SHORT).show();
//                    } else {
                        databaseReference.child("grades").child(id).setValue(new Grade(id, type, title, date,grade));
                        Toast.makeText(context, "DONE!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
//                    }
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            finish();
        } else if (id == R.id.add) {
            ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
            viewDialogAdd.showDialog(ManageGrade.this);
            Toast.makeText(this, "add new", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}