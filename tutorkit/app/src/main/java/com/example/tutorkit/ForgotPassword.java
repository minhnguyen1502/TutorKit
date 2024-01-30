package com.example.tutorkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPassword extends AppCompatActivity {

    private Button reset;
    private EditText edt_email;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        reset = findViewById(R.id.reset);
        edt_email = findViewById(R.id.email);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_email.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(ForgotPassword.this, "enter email", Toast.LENGTH_SHORT).show();
                    edt_email.setError("email is required");
                    edt_email.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(ForgotPassword.this, "enter email valid", Toast.LENGTH_SHORT).show();
                    edt_email.setError(" valid email is required");
                    edt_email.requestFocus();
                }else {
                    forgotPassword(email);
                }
            }
        });
    }

    private void forgotPassword(String email) {
        firebaseAuth  = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check inbbox for password reset link", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ForgotPassword.this, Login.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }else {
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthInvalidUserException e){
                        edt_email.setError("not Exist");
                    }catch (Exception e){
                        Log.e("Forgot password activity", e.getMessage());
                        Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
}