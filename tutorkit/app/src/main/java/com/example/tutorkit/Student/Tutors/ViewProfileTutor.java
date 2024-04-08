package com.example.tutorkit.Student.Tutors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tutorkit.Models.StatusAdd;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.Tutor;
import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Account.Tutor_profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfileTutor extends AppCompatActivity {

    private TextView txt_name, txt_phone, txt_gender, txt_DOB, txt_address, txt_subject, txt_intro;
    private String name, phone, gender, DOB, address, subject, intro;
    private ImageView avatar;
    Button back, choose;
    String tutorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_tutor);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        txt_name = findViewById(R.id.name);
        txt_phone = findViewById(R.id.phone);
        txt_gender = findViewById(R.id.gender);
        txt_DOB = findViewById(R.id.DOB);
        txt_address = findViewById(R.id.address);
        txt_subject = findViewById(R.id.subject);
        txt_intro = findViewById(R.id.intro);
        avatar = findViewById(R.id.avatar);
        back = findViewById(R.id.btn_cancel);
        choose = findViewById(R.id.btn_choose);
        tutorId = getIntent().getStringExtra("tutorId");

        Show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Check if the tutor ID exists in the Student 
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("Student").child(FirebaseAuth.getInstance().getUid());
        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                if (student.getStatusAdd() != null && student.getStatusAdd().getIdList() != null && student.getStatusAdd().getIdList().equals(tutorId)) {
                    choose.setVisibility(View.GONE);
                } else {
                    choose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference("tutors")
                                    .child(tutorId)
                                    .child("IdStudent").child(FirebaseAuth.getInstance().getUid())
                                    .setValue(new StatusAdd(FirebaseAuth.getInstance().getUid(), false));
                            Toast.makeText(ViewProfileTutor.this, "I liked this tutor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewProfileTutor.this, "Failed to load student status", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void Show() {
        if (tutorId != null) {
            DatabaseReference tutorRef = FirebaseDatabase.getInstance().getReference("tutors").child(tutorId);
            tutorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Tutor tutor = snapshot.getValue(Tutor.class);
                    if (tutor != null) {
                        name = tutor.getName();
                        Glide
                                .with(ViewProfileTutor.this)
                                .load(tutor.getImg())
                                .centerCrop()
                                .into(avatar);

                        phone = tutor.getPhone();
                        gender = tutor.getGender();
                        DOB = tutor.getDOB();
                        address = tutor.getAddress();
                        subject = tutor.getSubject();
                        intro = tutor.getIntroduction();

                        txt_name.setText(name);
                        txt_phone.setText(phone);
                        txt_gender.setText(gender);
                        txt_DOB.setText(DOB);
                        txt_address.setText(address);
                        txt_subject.setText(subject);
                        txt_intro.setText(intro);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ViewProfileTutor.this, "Failed to load tutor details", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Tutor ID not found", Toast.LENGTH_SHORT).show();
        }
    }
}