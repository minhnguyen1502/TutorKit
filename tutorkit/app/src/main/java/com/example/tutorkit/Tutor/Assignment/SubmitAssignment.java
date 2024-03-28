package com.example.tutorkit.Tutor.Assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorkit.Models.AssignmentModel;
import com.example.tutorkit.Models.StatusAdd;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.SubmitAssignmentModel;
import com.example.tutorkit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class SubmitAssignment extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<SubmitAssignmentModel> submitAssignmentModelArrayList;
    ArrayList<Student> studentArrayList;
    SubmitAssignmentAdapter adapter;
    ArrayList<String> idStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        submitAssignmentModelArrayList = new ArrayList<>();
        studentArrayList = new ArrayList<>();
        idStudent = new ArrayList<>();
        studentArrayList.add(new Student("Student Name","", "","","","",""));

        readData();
        showListStudents();
    }

    private void showListStudents() {
        FirebaseDatabase.getInstance().getReference("tutors")
                .child(FirebaseAuth.getInstance().getUid())
                .child("IdStudent").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        idStudent.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            StatusAdd statusAdd = dataSnapshot.getValue(StatusAdd.class);
                            try {
                                if (statusAdd.getStatus()) {
                                    idStudent.add(statusAdd.getIdList());
                                }
                            } catch (Exception e) {
                                Log.e("TAG", "onDataChange: " + e.getMessage());
                            }
                        }
                        if (idStudent.size() > 0) {
                            for (int i = 0; i < idStudent.size(); i++) {
                                databaseReference.child("Student")
                                        .child(idStudent.get(i))
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                                                    Student student = new Student();
                                                Student student = snapshot.getValue(Student.class);
                                                studentArrayList.add(student);
//                                                }
                                                Log.e("TAG", "log test: " + studentArrayList);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                            }

                        } else {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void readData() {
        databaseReference.child("submitAssignment").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                submitAssignmentModelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SubmitAssignmentModel submitAssignmentModel = dataSnapshot.getValue(SubmitAssignmentModel.class);
                    try {
                        if (Objects.equals(submitAssignmentModel.getIdTutor(), FirebaseAuth.getInstance().getUid())){
                            submitAssignmentModelArrayList.add(submitAssignmentModel);
                        }
                    }catch (Exception e){
                        Log.e("TAG", "onDataChange: "+e.getMessage() );
                    }

                }
                adapter = new SubmitAssignmentAdapter(SubmitAssignment.this, submitAssignmentModelArrayList);
                adapter.setDataStudent(studentArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
            viewDialogAdd.showDialog(SubmitAssignment.this);
        } else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private class ViewDialogAdd {
        public void showDialog(Context context) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_add_submit_assignment);

            Spinner studentName = dialog.findViewById(R.id.spn_name);
            EditText edtDateline = dialog.findViewById(R.id.edt_date);
            EditText edt_title = dialog.findViewById(R.id.edt_title);
            ArrayAdapter<Student> studentList = new ArrayAdapter<Student>(context, android.R.layout.simple_list_item_1,studentArrayList){
                @Override
                public boolean isEnabled(int position) {
//                    position = 0 not select
                    return position != 0;
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                    view.setText(studentArrayList.get(position).getName());
                    if (position == 0) {
                        view.setTextColor(Color.GRAY);
                    } else {
                        view.setTextColor(Color.BLACK);
                    }
                    return view;
                }

                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setText(studentArrayList.get(position).getName());
                    return view;
                }
            };
            studentName.setAdapter(studentList);

            Button buttonAdd = dialog.findViewById(R.id.buttonAdd);

            edtDateline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    // Người dùng đã chọn ngày. Cập nhật trường ngày (edate).
                                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                    edtDateline.setText(selectedDate);
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
//                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    Student student = (Student) studentName.getSelectedItem();
                    String idTutor = FirebaseAuth.getInstance().getUid();
                    String id = "submitAssignment" + new Date().getTime();
                    String title = edt_title.getText().toString();
                    String name = student.getName();
                    String dateline = edtDateline.getText().toString();

                    if(TextUtils.isEmpty(title)) {
                        Toast.makeText(context, "enter title", Toast.LENGTH_SHORT).show();
                        edt_title.setError("required");
                        edt_title.requestFocus();
                    } else if (TextUtils.isEmpty(dateline)) {
                        Toast.makeText(context, "pick dateline", Toast.LENGTH_SHORT).show();
                        edtDateline.requestFocus();
                        edtDateline.setError("reuired");

                    }else {
                        // Check if the selected date is in the future
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date selectedDate = sdf.parse(dateline);
                            Date currentDate = new Date();

                            if (selectedDate != null && selectedDate.after(currentDate)) {
                                // The selected date is in the future, proceed to add the assignment
                                databaseReference.child("submitAssignment").child(id)
                                        .setValue(new SubmitAssignmentModel(id, title, dateline, idTutor, student.getId(), name));
                                Toast.makeText(context, "DONE!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                // The selected date is not in the future
                                Toast.makeText(context, "Please select a future date", Toast.LENGTH_SHORT).show();
                                edtDateline.requestFocus();
                                edtDateline.setError("Invalid date");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            // Handle parsing exception if needed
                        }
//
                    }

                }
            });


            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }
}