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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tutorkit.Models.Grade;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.Tuition;
import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Tuition.TuitionAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ManageGrade extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Grade> gradeArrayList;
    GradeAdapter adapter;
    String idStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_grade);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gradeArrayList = new ArrayList<>();
        idStudent = getIntent().getStringExtra("idStudent");
        readData();
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    private void readData() {

        databaseReference.child("grades").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gradeArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Grade grades = dataSnapshot.getValue(Grade.class);
                    try {
                        if (Objects.equals(grades.getIdStudent(), idStudent)){
                            if (Objects.equals(grades.getIdTutor(), FirebaseAuth.getInstance().getUid())){
                                gradeArrayList.add(grades);
                            }
                        }
                    }catch (Exception e){
                        Log.e("TAG", "onDataChange: "+e.getMessage() );
                    }
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
            ImageView cancel = dialog.findViewById(R.id.cancel);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = "grade" + new Date().getTime();
                    String idTutor = FirebaseAuth.getInstance().getUid();
                    String n_idStudent = idStudent;
                    String type = spn_type.getSelectedItem().toString();
                    String title = edt_title.getText().toString();
                    String date = edt_date.getText().toString();

                    int grade =0;
                    if (!TextUtils.isEmpty(edt_grade.getText().toString())){
                        grade = Integer.parseInt(edt_grade.getText().toString());
                    }

                  if (TextUtils.isEmpty(title)) {
                    Toast.makeText(ManageGrade.this, "Enter your title", Toast.LENGTH_SHORT).show();
                    edt_title.setError("Title is required");
                    edt_title.requestFocus();
                }
                  else if (TextUtils.isEmpty(edt_grade.getText().toString())) {
                      Toast.makeText(ManageGrade.this, "Enter grade ", Toast.LENGTH_SHORT).show();
                      edt_grade.setError("Grade is required");
                      edt_grade.requestFocus();
                  }
                  else if (grade <0 || grade > 10) {
                      Toast.makeText(ManageGrade.this, "grade must be less than 10 and great than 0 ", Toast.LENGTH_SHORT).show();
                      edt_grade.setError("Title is required");
                      edt_grade.requestFocus();
                  }
                  else if (TextUtils.isEmpty(date)) {
                      Toast.makeText(ManageGrade.this, "Enter date ", Toast.LENGTH_SHORT).show();
                      edt_date.setError("Grade is required");
                      edt_date.requestFocus();
                  } else {
                    databaseReference.child("grades").child(id).setValue(new Grade(id, type, title, date,idTutor,n_idStudent, grade));
                    Toast.makeText(context, "DONE!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    }
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