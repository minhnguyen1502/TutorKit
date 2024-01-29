package com.example.tutorkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorkit.Models.Tutor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tutor_Profile extends AppCompatActivity {

    private TextView txt_name, txt_email,txt_phone, txt_gender, txt_DOB, txt_address, txt_subject, txt_class;
    private String name, email, phone, gender, DOB, address, subject, t_class;
    private ImageView avata;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        txt_name = findViewById(R.id.name);
        txt_email = findViewById(R.id.email);
        txt_phone = findViewById(R.id.phone);
        txt_gender = findViewById(R.id.gender);
        txt_DOB = findViewById(R.id.DOB);
        txt_address = findViewById(R.id.address);
        txt_subject = findViewById(R.id.subject);
        txt_class = findViewById(R.id.t_class);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(Tutor_Profile.this, "Somthing wrong", Toast.LENGTH_SHORT).show();
        } else {
            showProfile(firebaseUser);
        }


    }

    private void showProfile(FirebaseUser firebaseUser) {
        String tutorID = firebaseUser.getUid();

        //extracting tutor reference from database for registered tutor
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Tutor");
        databaseReference.child(tutorID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tutor tutor = snapshot.getValue(Tutor.class);
                if (tutor != null ){
                    name = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    phone = tutor.getPhone();
                    gender = tutor.getGender();
                    DOB = tutor.getDOB();
                    address = tutor.getAddress();
                    subject = tutor.getSubject();
                    t_class = tutor.getT_class();

                    txt_name.setText(name);
                    txt_email.setText(email);
                    txt_phone.setText(phone);
                    txt_gender.setText(gender);
                    txt_DOB.setText(gender);
                    txt_address.setText(address);
                    txt_subject.setText(subject);
                    txt_class.setText(t_class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }
}