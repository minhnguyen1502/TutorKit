package com.example.tutorkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class UpdatePassword extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView authenticated;
    private String oldPassword, newPassword, confirmNewPassword;
    private Button authentication, update;
    private EditText edt_newPassword, edt_confirmNewPassword, edt_oldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        edt_newPassword = findViewById(R.id.edt_new_password);
        edt_confirmNewPassword = findViewById(R.id.edt_confirm_new_password);
        edt_oldPassword = findViewById(R.id.edt_old_password);
        authenticated = findViewById(R.id.authenticated);
        authentication = findViewById(R.id.authen);
        update = findViewById(R.id.update);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // disable
        edt_confirmNewPassword.setEnabled(false);
        edt_newPassword.setEnabled(false);
        update.setEnabled(false);

        if (firebaseUser.equals("")){
            Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UpdatePassword.this, Tutor_Profile.class));
            finish();
        } else {
            reAuthenticate(firebaseUser);
        }
    }

    private void reAuthenticate(FirebaseUser firebaseUser) {
        authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPassword = edt_oldPassword.getText().toString();
                if (TextUtils.isEmpty(oldPassword)){
                    Toast.makeText(UpdatePassword.this, "Enter password to continue", Toast.LENGTH_SHORT).show();
                    edt_oldPassword.setError("enter password for authentication");
                    edt_oldPassword.requestFocus();
                } else {
                    AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), oldPassword);
                    firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(UpdatePassword.this, "ok! You can update password", Toast.LENGTH_SHORT).show();
                                authenticated.setText("you can update password");

                                // dissable edt password and button authen
                                // enable edt new email
                                edt_confirmNewPassword.setEnabled(true);
                                edt_newPassword.setEnabled(true);
                                edt_oldPassword.setEnabled(false);
                                authentication.setEnabled(false);
                                update.setEnabled(true);

                                // change color button update

                                update.setBackgroundTintList(ContextCompat.getColorStateList(UpdatePassword.this,
                                        R.color.green));
                                update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                            updatePassword(firebaseUser);
                                    }
                                });
                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(UpdatePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
                }
            }
        });
    }

    private void updatePassword(FirebaseUser firebaseUser) {
        newPassword = edt_newPassword.getText().toString();
        confirmNewPassword = edt_confirmNewPassword.getText().toString();
        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(UpdatePassword.this, "Enter your password", Toast.LENGTH_SHORT).show();
            edt_newPassword.setError("password is required");
            edt_newPassword.requestFocus();
        }else if (newPassword.length() < 6) {
            Toast.makeText(UpdatePassword.this, "password should be at least 6 digits", Toast.LENGTH_SHORT).show();
            edt_newPassword.setError("password is too weak");
            edt_newPassword.requestFocus();
        }else if (TextUtils.isEmpty(confirmNewPassword)) {
            Toast.makeText(UpdatePassword.this, "Enter your confirm password", Toast.LENGTH_SHORT).show();
            edt_confirmNewPassword.setError("confirm-password is required");
            edt_confirmNewPassword.requestFocus();
        }else if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(UpdatePassword.this, "password confirmation must be same the password", Toast.LENGTH_SHORT).show();
            edt_confirmNewPassword.setError("password confirmation is required");
            edt_confirmNewPassword.requestFocus();

        }else if (oldPassword.equals(newPassword)) {
            Toast.makeText(UpdatePassword.this, "new password can not be same as old password", Toast.LENGTH_SHORT).show();
            edt_newPassword.setError("enter new password");
            edt_newPassword.requestFocus();
        }else {
            firebaseUser.updateEmail(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(UpdatePassword.this, "updated password", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdatePassword.this, Tutor_Profile.class);
                        startActivity(i);
                        finish();
                    }else {
                        try {
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(UpdatePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}