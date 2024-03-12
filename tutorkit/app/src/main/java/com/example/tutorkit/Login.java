package com.example.tutorkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorkit.Tutor.Tutor_home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    TextView register;

    private EditText edt_email, edt_password;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        register = findViewById(R.id.txt_register);
        edt_password = findViewById(R.id.edt_password);
        edt_email = findViewById(R.id.edt_email);
        firebaseAuth = FirebaseAuth.getInstance();

        // forgot password
        TextView forgotPassword = findViewById(R.id.txt_forgot_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this, "You can reset password", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txt_email = edt_email.getText().toString();
                String txt_password = edt_password.getText().toString();

                if (TextUtils.isEmpty(txt_email)) {
                    Toast.makeText(Login.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    edt_email.setError("Email is required");
                    edt_email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
                    Toast.makeText(Login.this, "Re-enter email", Toast.LENGTH_SHORT).show();
                    edt_email.setError("valid email is required");
                    edt_email.requestFocus();

                } else if (TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(Login.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    edt_password.setError("Email is required");
                    edt_password.requestFocus();

                } else {
                    login(txt_email, txt_password);
                }
            }
        });

    }

    private void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // check tutor or student.

                if (task.isSuccessful()) {
                    //get instant of current tutor
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    //check if email is verified before tutor can access their profile

                    if (firebaseUser.isEmailVerified()) {
                        Log.e("TAG", "onComplete: " + firebaseUser.getUid());
                        FirebaseDatabase.getInstance().getReference("tutors").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Log.e("TAG", "onDataChange: " + snapshot.getKey());
                                if (snapshot.getValue() != null) {
                                    Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();
                                    //open home
                                    Intent intent = new Intent(Login.this, Tutor_home.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();
                                    //open home
                                    Intent intent = new Intent(Login.this, com.example.tutorkit.Student.Student_home.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

//                        Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();
//                        //open home
//                        Intent intent = new Intent(Login.this, Tutor_home.class);
//                        startActivity(intent);
//                        finish();
                    } else {
                        firebaseUser.sendEmailVerification();
                        showAlerDialog();
                    }
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        edt_email.setError("not exists, please register ");
                        edt_email.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        edt_email.setError("Invalid credential, please check and re-enter");
                        edt_email.requestFocus();
                    } catch (Exception e) {
                        Log.e("Login activity", e.getMessage());
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }


    private boolean available(String name) {
        boolean available = true;
        try {
            // check if available
            getPackageManager().getPackageInfo(name, 0);
        } catch (PackageManager.NameNotFoundException e) {
            // if not available set
            // available as false
            available = false;
        }
        return available;
    }

    private void showAlerDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Can not login without email  verification");

        // click continue buttton to open email
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // check mobile have app email
                if (available("com.google.android.gm")) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                    // to email app in new window and not within app
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "Don't have email app", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // create dialog
        AlertDialog alertDialog = builder.create();

        // show dialog
        alertDialog.show();
    }

}