package com.example.tutorkit.Tutor.Tuition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tutorkit.Models.StatusAdd;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.Tuition;
import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Adapter.TuitionAdapter;
import com.example.tutorkit.Tutor.Tutor_home;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Tuition_page extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Tuition> tuitionArrayList;
    ArrayList<Student> studentArrayList;
    TuitionAdapter adapter;
    ArrayList<String> idStudent;

    FloatingActionButton btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuition);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true); // work offline
//        Objects.requireNonNull(getSupportActionBar()).hide();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tuitionArrayList = new ArrayList<>();
        studentArrayList = new ArrayList<>();
        idStudent = new ArrayList<>();


        btn_add = findViewById(R.id.add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
                viewDialogAdd.showDialog(Tuition_page.this);
            }
        });

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
                                                studentArrayList.add(snapshot.getValue(Student.class));
                                                adapter.notifyDataSetChanged();
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
        databaseReference.child("tuition").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tuitionArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Tuition tuition = dataSnapshot.getValue(Tuition.class);
                    tuitionArrayList.add(tuition);
                }
                adapter = new TuitionAdapter(Tuition_page.this, tuitionArrayList);
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
        getMenuInflater().inflate(R.menu.tool_bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent i = new Intent(Tuition_page.this, Tutor_home.class);
            startActivity(i);
            finish();
        } else if (id == R.id.action_search) {
//            Intent i = new Intent(Tutor_profile.this, UpdateEmail.class);
//            startActivity(i);
//            finish();
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
            dialog.setContentView(R.layout.dialog_add_tuition);

            EditText edtName = dialog.findViewById(R.id.edt_name);
            EditText edtAmount = dialog.findViewById(R.id.edt_amount);
            EditText edtPrice = dialog.findViewById(R.id.edt_price);
            EditText edtDateline = dialog.findViewById(R.id.edt_date);
            EditText txtTotal = dialog.findViewById(R.id.txt_total);

            Button buttonAdd = dialog.findViewById(R.id.buttonAdd);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    String id = FirebaseAuth.getInstance().getUid();
                    String name = edtName.getText().toString();
                    int amount = Integer.parseInt(edtAmount.getText().toString());
                    int price = Integer.parseInt(edtPrice.getText().toString());
                    String dateline = edtDateline.getText().toString();

                    int total = Integer.parseInt(txtTotal.getText().toString());

                        databaseReference.child("tuition").child("tuition" + new Date().getTime())
                                .setValue(new Tuition(id,name,dateline, amount,price,total));
                        Toast.makeText(context, "DONE!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                }
            });


            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }
}