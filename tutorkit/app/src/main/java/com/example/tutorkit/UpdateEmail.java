package com.example.tutorkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorkit.Tutor.Tutor_Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmail extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView authenticated;
    private String oldEmial, newEmail, password;
    private Button authentication, update;
    private EditText edt_newEmail, edt_password, edt_oldEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        edt_password = findViewById(R.id.edt_password);
        edt_oldEmail = findViewById(R.id.edt_old_email);
        edt_newEmail = findViewById(R.id.edt_new_email);
        authenticated = findViewById(R.id.authenticated);
        authentication = findViewById(R.id.authen);
        update = findViewById(R.id.update);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // disabled in the beginning util authenticated
        update.setEnabled(false);
        edt_newEmail.setEnabled(false);

        oldEmial = firebaseUser.getEmail();
        edt_oldEmail.setText(oldEmial);

        if (firebaseUser.equals("")){
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }else {
            reAuthencate(firebaseUser);
        }
    }

    private void reAuthencate(FirebaseUser firebaseUser) {
        authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = edt_password.getText().toString();

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(UpdateEmail.this, "Enter password to continue", Toast.LENGTH_SHORT).show();
                    edt_password.setError("enter password for authentication");
                    edt_password.requestFocus();
                }else {
                    AuthCredential authCredential = EmailAuthProvider.getCredential(oldEmial, password);
                    firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(UpdateEmail.this, "ok! You can update email", Toast.LENGTH_SHORT).show();
                                authenticated.setText("you can update email");

                                // dissable edt password and button authen
                                // enable edt new email
                                edt_newEmail.setEnabled(true);
                                edt_password.setEnabled(false);
                                authentication.setEnabled(false);
                                update.setEnabled(true);

                                // change color button update

                                update.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmail.this,
                                        R.color.green));
                                update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        newEmail = edt_newEmail.getText().toString();
                                        if (TextUtils.isEmpty(newEmail)){
                                            Toast.makeText(UpdateEmail.this, "new email id required", Toast.LENGTH_SHORT).show();
                                            edt_newEmail.setError("Enter new email");
                                            edt_newEmail.requestFocus();
                                        } else if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                                            Toast.makeText(UpdateEmail.this, "enter valid email ", Toast.LENGTH_SHORT).show();
                                            edt_newEmail.setError("provide valid email");
                                            edt_newEmail.requestFocus();
                                        }else if (oldEmial.matches(newEmail)) {
                                            Toast.makeText(UpdateEmail.this, "new email cannot same ole email ", Toast.LENGTH_SHORT).show();
                                            edt_newEmail.setError("Enter new email");
                                            edt_newEmail.requestFocus();
                                        }else {
                                            updateEmail(firebaseUser);
                                        }
                                    }
                                });
                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(UpdateEmail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
                }
            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {
        firebaseUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    firebaseUser.sendEmailVerification();

                    Toast.makeText(UpdateEmail.this, "updated. verify new email", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UpdateEmail.this, Tutor_Profile.class);
                    startActivity(i);
                    finish();
                }else {
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(UpdateEmail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}