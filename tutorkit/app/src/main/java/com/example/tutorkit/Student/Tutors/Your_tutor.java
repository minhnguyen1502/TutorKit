package com.example.tutorkit.Student.Tutors;

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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorkit.Models.StatusAdd;
import com.example.tutorkit.Models.Tutor;
import com.example.tutorkit.R;
import com.example.tutorkit.Student.Adapter.TutorAdapter;
import com.example.tutorkit.Student.Student_home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Your_tutor extends AppCompatActivity {

    // moi
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    ArrayList<Tutor> tutorArrayList;
    TutorAdapter adapter;
    ArrayList<String> idTutors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_tutor);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        idTutors = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tutorArrayList = new ArrayList<>();

        adapter = new TutorAdapter(Your_tutor.this, tutorArrayList);
        recyclerView.setAdapter(adapter);

        TextView list_tutor = findViewById(R.id.txt_list_tutor);

        showListStudents();
        list_tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Your_tutor.this, Tutor_list
                        .class));
            }
        });
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    private void showListStudents() {
        FirebaseDatabase.getInstance().getReference("Student")
                .child(FirebaseAuth.getInstance().getUid())
                .child("IdTutors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        idTutors.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            StatusAdd statusAdd = dataSnapshot.getValue(StatusAdd.class);
                            try {
                                if (statusAdd.getStatus()) {
                                    idTutors.add(statusAdd.getIdList());
                                }
                            } catch (Exception e) {
                                Log.e("TAG", "onDataChange: " + e.getMessage());
                            }
                        }
                        if (idTutors.size() > 0) {
                            for (int i = 0; i < idTutors.size(); i++) {
                                databaseReference.child("tutors").child(idTutors.get(i))
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                tutorArrayList.add(snapshot.getValue(Tutor.class));
                                                adapter.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent i = new Intent(Your_tutor.this, Student_home.class);
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