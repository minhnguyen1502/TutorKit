package com.example.tutorkit.Student.Grade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tutorkit.Models.Grade;
import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Grade.GradeAdapter;
import com.example.tutorkit.Tutor.Grade.ManageGrade;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ViewGrade extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Grade> gradeArrayList;
    ViewGradeAdapter adapter;
    String idTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_grade);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        idTutor = getIntent().getStringExtra("idTutor");

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
                    Grade grade = dataSnapshot.getValue(Grade.class);
                    try {
                        if (Objects.equals(grade.getIdTutor(), idTutor)){
                            if (Objects.equals(grade.getIdStudent(), FirebaseAuth.getInstance().getUid())){
                                gradeArrayList.add(grade);
                            }
                        }
                    }catch (Exception e){
                        Log.e("TAG", "onDataChange: "+e.getMessage() );
                    }
                }
                adapter = new ViewGradeAdapter(ViewGrade.this, gradeArrayList);
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
//            ManageGrade.ViewDialogAdd viewDialogAdd = new ManageGrade.ViewDialogAdd();
//            viewDialogAdd.showDialog(ManageGrade.this);
            Toast.makeText(this, "Can't add", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}