package com.example.tutorkit.Student.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Tutor_home;
import com.example.tutorkit.UpdateEmail;
import com.example.tutorkit.UpdatePassword;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student_profile extends AppCompatActivity {

    private TextView txt_name, txt_email, txt_phone, txt_gender, txt_DOB, txt_address, txt_phone_parent;
    private String name, email, phone, gender, DOB, address, phone_parent;
    private ImageView avatar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        txt_name = findViewById(R.id.name);
        txt_email = findViewById(R.id.email);
        txt_phone = findViewById(R.id.phone);
        txt_gender = findViewById(R.id.gender);
        txt_DOB = findViewById(R.id.DOB);
        txt_address = findViewById(R.id.address);
        txt_phone_parent = findViewById(R.id.phone_parent);
        avatar = findViewById(R.id.avatar);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(Student_profile.this, "Somthing wrong", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Student_profile.this);
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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Student");
        databaseReference.child(tutorID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Student student = snapshot.getValue(Student.class);
                if (student != null) {
                    name = student.getName();
                    email = firebaseUser.getEmail();
                    Glide
                            .with(Student_profile.this)
                            .load(student.getImg())
                            .centerCrop()
                            .into(avatar);

                    phone = student.getPhone();
                    gender = student.getGender();
                    DOB = student.getDOB();
                    address = student.getAddress();
                    phone_parent = student.getParent_phone();

                    txt_name.setText(name);
                    txt_email.setText(email);
                    txt_phone.setText(phone);
                    txt_gender.setText(gender);
                    txt_DOB.setText(DOB);
                    txt_address.setText(address);
                    txt_phone_parent.setText(phone_parent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Student_profile.this, "something wrong", Toast.LENGTH_SHORT).show();
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
            Intent i = new Intent(Student_profile.this, Edit_student_profile.class);
            startActivity(i);
            finish();
        } else if (id == R.id.update_email) {
            Intent i = new Intent(Student_profile.this, UpdateEmail.class);
            startActivity(i);
            finish();
        } else if (id == R.id.update_password) {
            Intent i = new Intent(Student_profile.this, UpdatePassword.class);
            startActivity(i);
            finish();
        } else if (id == R.id.home) {
            Intent i = new Intent(Student_profile.this, Tutor_home.class);
            startActivity(i);
            finish();
        }else if (id == R.id.exit) {
            finish();
        } else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
