package com.example.tutorkit.Student.Tutors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorkit.Models.Tutor;
import com.example.tutorkit.R;
import com.example.tutorkit.Student.Adapter.TutorAdapter;
import com.example.tutorkit.Student.Student_home;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tutor_list extends AppCompatActivity {

    DatabaseReference databaseReference;
    TextView  your_tutor;

    RecyclerView recyclerView;
    ArrayList<Tutor> tutorsArrayList;
    TutorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_list);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        your_tutor = findViewById(R.id.txt_your_tutor);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tutorsArrayList = new ArrayList<>();

        your_tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Tutor_list.this, Your_tutor.class));
            }
        });
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        
        showListTutors();
    }

    private void showListTutors() {
        databaseReference.child("tutors").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tutorsArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Tutor tutor = dataSnapshot.getValue(Tutor.class);
                    tutorsArrayList.add(tutor);
                }
                adapter = new TutorAdapter(Tutor_list.this, tutorsArrayList);
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
            Intent i = new Intent(Tutor_list.this, Student_home.class);
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
}