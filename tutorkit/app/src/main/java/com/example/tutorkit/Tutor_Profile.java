package com.example.tutorkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    private TextView txt_name, txt_email, txt_phone, txt_gender, txt_DOB, txt_address, txt_subject, txt_class;
    private String name, email, phone, gender, DOB, address, subject, t_class;
    private ImageView avatar;
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
        avatar = findViewById(R.id.avatar);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Tutor_Profile.this, UploadAvatar.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(Tutor_Profile.this, "Somthing wrong", Toast.LENGTH_SHORT).show();
        } else {
            checkIfEmailVerified(firebaseUser);
            showProfile(firebaseUser);
        }


    }

    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()) {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Tutor_Profile.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Can not login without email  verification");

        // click continue buttton to open email
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                // to email app in new window and not within app
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        // create dialog
        AlertDialog alertDialog = builder.create();

        // show dialog
        alertDialog.show();
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String tutorID = firebaseUser.getUid();

        //extracting tutor reference from database for registered tutor
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Tutor");
        databaseReference.child(tutorID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tutor tutor = snapshot.getValue(Tutor.class);
                if (tutor != null) {
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
                Toast.makeText(Tutor_Profile.this, "something wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.update_profile) {
            Intent i = new Intent(Tutor_Profile.this, Edit_tutor_profile.class);
            startActivity(i);
        } else if (id == R.id.update_email) {
            Intent i = new Intent(Tutor_Profile.this, UpdateEmail.class);
            startActivity(i);
        } else if (id == R.id.update_password) {
            Intent i = new Intent(Tutor_Profile.this, UpdatePassword.class);
            startActivity(i);
        }
        else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
            return super.onOptionsItemSelected(item);
        }
    }
