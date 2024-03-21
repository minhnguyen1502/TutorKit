package com.example.tutorkit.Student.Payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tutorkit.Models.StatusAdd;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.Tuition;
import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Tuition.TuitionAdapter;
import com.example.tutorkit.Tutor.Tuition.Tuition_page;
import com.example.tutorkit.Tutor.Tutor_home;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Payment extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Tuition> tuitionArrayList;
    ArrayList<Student> studentArrayList;
    PaymentAdapter adapter;
    ArrayList<String> idStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tuitionArrayList = new ArrayList<>();
        studentArrayList = new ArrayList<>();
        idStudent = new ArrayList<>();

        readData();
    }
    private void readData() {
        databaseReference.child("tuition").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                tuitionArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Tuition tuition = dataSnapshot.getValue(Tuition.class);
                    try {
                        if (tuition.getIdStudent() == FirebaseAuth.getInstance().getUid()){
                            tuitionArrayList.add(tuition);
                        }
                    }catch (Exception e){
                        Log.e("TAG", "onDataChange: "+e.getMessage() );
                    }
                }
                adapter = new PaymentAdapter(Payment.this, tuitionArrayList);
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
            finish();
        } else if (id == R.id.action_search) {

        } else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}