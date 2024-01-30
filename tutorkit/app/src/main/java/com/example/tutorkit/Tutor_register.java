package com.example.tutorkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorkit.Models.Tutor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tutor_register extends AppCompatActivity {

    private EditText edt_re_name, edt_re_email, edt_re_DOB, edt_re_phone, edt_re_address,
                    edt_re_subject, edt_re_class, edt_re_password, edt_re_confirm_password;
    private RadioButton re_gender_selected;
    private RadioGroup group_re_gender;
    private Phonenumber.PhoneNumber swissNumberProto;


//    private DatePickerDialog datePickerDialog;

    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_register);

        login = (TextView) findViewById(R.id.txt_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Tutor_register.this, Login.class);
                startActivity(i);
            }
        });

//        getSupportActionBar().setTitle("Register");

        Toast.makeText(Tutor_register.this, "Register Tutor", Toast.LENGTH_SHORT).show();

        edt_re_name = findViewById(R.id.edt_name);
        edt_re_address = findViewById(R.id.edt_address);
        edt_re_email = findViewById(R.id.edt_email);
        edt_re_DOB = findViewById(R.id.edt_DOB);
        edt_re_phone = findViewById(R.id.edt_phone);
        edt_re_class = findViewById(R.id.edt_class);
        edt_re_subject = findViewById(R.id.edt_subject);
        edt_re_password = findViewById(R.id.edt_password);
        edt_re_confirm_password = findViewById(R.id.edt_confirm_password);

        group_re_gender = findViewById(R.id.group_gender);
        group_re_gender.clearCheck();
        edt_re_DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Tutor_register.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Người dùng đã chọn ngày. Cập nhật trường ngày (edate).
                                String selectedDate =dayOfMonth + "/"+ (month+1)+"/"+year;
                                edt_re_DOB.setText(selectedDate);
                            }
                        },
                        // Truyền ngày hiện tại làm ngày mặc định cho DatePickerDialog.
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );

                datePickerDialog.show();
            }
        });

        Button register = findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedGender = group_re_gender.getCheckedRadioButtonId();
                re_gender_selected = findViewById(selectedGender);

                // obtain the entered data
                String txt_name = edt_re_name.getText().toString();
                String txt_address = edt_re_address.getText().toString();
                String txt_DOB = edt_re_DOB.getText().toString();
                String txt_phone = edt_re_phone.getText().toString();
                String txt_email = edt_re_email.getText().toString();
                String txt_subject = edt_re_subject.getText().toString();
                String txt_class = edt_re_class.getText().toString();
                String txt_password = edt_re_password.getText().toString();
                String txt_confirm_password = edt_re_confirm_password.getText().toString();
                String txt_gender;

                // validate phone no. sung Matcher and Pattern
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                try {
                    swissNumberProto = phoneUtil.parse(txt_phone, "VN");
                } catch (NumberParseException e) {
                    System.err.println("NumberParseException was thrown: " + e.toString());
                }
                boolean isValid = phoneUtil.isValidNumber(swissNumberProto); // returns true


                // pick date


                if (TextUtils.isEmpty(txt_name)) {
                    Toast.makeText(Tutor_register.this, "Enter your name", Toast.LENGTH_SHORT).show();
                    edt_re_name.setError("Name is required");
                    edt_re_name.requestFocus();
                } else if (TextUtils.isEmpty(txt_email)) {
                    Toast.makeText(Tutor_register.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    edt_re_email.setError("email is required");
                    edt_re_email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
                    Toast.makeText(Tutor_register.this, "re-enter your email", Toast.LENGTH_SHORT).show();
                    edt_re_email.setError("valid email is required");
                    edt_re_email.requestFocus();
                } else if (TextUtils.isEmpty(txt_DOB)) {
                    Toast.makeText(Tutor_register.this, "Enter your DOB", Toast.LENGTH_SHORT).show();
                    edt_re_DOB.setError("DOB is required");
                    edt_re_DOB.requestFocus();
                } else if (!isValid ) {
                    Toast.makeText(Tutor_register.this, "re-Enter your phone", Toast.LENGTH_SHORT).show();
                    edt_re_phone.setError("Phone no. is not valid");
                    edt_re_phone.requestFocus();
                } else if (TextUtils.isEmpty(txt_phone)) {
                    Toast.makeText(Tutor_register.this, "Enter your phone", Toast.LENGTH_SHORT).show();
                    edt_re_phone.setError("phone is required");
                    edt_re_phone.requestFocus();
                } else if (txt_phone.length() !=10) {
                    Toast.makeText(Tutor_register.this, "Re-enter your phone no.", Toast.LENGTH_SHORT).show();
                    edt_re_phone.setError("Phone no. should be 10 digits");
                    edt_re_phone.requestFocus();
                } else if (TextUtils.isEmpty(txt_address)) {
                    Toast.makeText(Tutor_register.this, "Enter your address", Toast.LENGTH_SHORT).show();
                    edt_re_address.setError("address is required");
                    edt_re_address.requestFocus();
                } else if (TextUtils.isEmpty(txt_subject)) {
                    Toast.makeText(Tutor_register.this, "Enter your subject", Toast.LENGTH_SHORT).show();
                    edt_re_subject.setError("subject is required");
                    edt_re_subject.requestFocus();
                }else if (TextUtils.isEmpty(txt_class)) {
                    Toast.makeText(Tutor_register.this, "Enter your class", Toast.LENGTH_SHORT).show();
                    edt_re_class.setError("class is required");
                    edt_re_class.requestFocus();
                } else if (group_re_gender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(Tutor_register.this, "select your gender", Toast.LENGTH_SHORT).show();
                    re_gender_selected.setError("gender is required");
                    re_gender_selected.requestFocus();
                }else if (TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(Tutor_register.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    edt_re_password.setError("password is required");
                    edt_re_password.requestFocus();
                }else if (txt_password.length() < 6) {
                    Toast.makeText(Tutor_register.this, "password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    edt_re_password.setError("password is too weak");
                    edt_re_password.requestFocus();
                }else if (TextUtils.isEmpty(txt_confirm_password)) {
                    Toast.makeText(Tutor_register.this, "Enter your confirm password", Toast.LENGTH_SHORT).show();
                    edt_re_confirm_password.setError("confirm-password is required");
                    edt_re_confirm_password.requestFocus();
                }else if (!txt_password.equals(txt_confirm_password)) {
                    Toast.makeText(Tutor_register.this, "password confirmation must be same the password", Toast.LENGTH_SHORT).show();
                    edt_re_confirm_password.setError("password confirmation is required");
                    edt_re_confirm_password.requestFocus();
                    // clear the enter password
                    edt_re_password.clearComposingText();
                    edt_re_confirm_password.clearComposingText();
                }else {
                    txt_gender = re_gender_selected.getText().toString();
                    registerTutor(txt_name, txt_email,txt_DOB,txt_gender, txt_phone,txt_address, txt_subject, txt_class, txt_password);
                }

            }
        });
    }

    private void registerTutor(String txt_name, String txt_email, String txt_dob, String txt_phone, String txt_gender, String txt_address, String txt_subject, String txt_class, String txt_password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // create profile
        firebaseAuth.createUserWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(Tutor_register.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            // display name
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(txt_name).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            Tutor tutor = new Tutor( txt_address, txt_dob, txt_email, txt_gender,txt_phone,txt_subject, txt_class);

                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Tutor");
                            referenceProfile.child(firebaseUser.getUid()).setValue(tutor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        // sent verification to mail
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(Tutor_register.this, "Register success. please verify email", Toast.LENGTH_SHORT).show();

//                                        //open profile after register success
//                                        Intent i = new Intent(Tutor_register.this, Tutor_Profile.class);
//                                        // to prevent tutor from returning back to Register activity on pressing back button after registration
//                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(i);
//                                        finish(); // close register
                                    }else {
                                        Toast.makeText(Tutor_register.this, "Register Failed. ", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }else {
                            try {
                                throw task.getException();

                            }catch (FirebaseAuthWeakPasswordException e){
                                edt_re_password.setError("too weak");
                                edt_re_password.requestFocus();
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                edt_re_password.setError("Invalid or already");
                                edt_re_password.requestFocus();

                            }catch (FirebaseAuthUserCollisionException e){
                                edt_re_password.setError("aother email");
                                edt_re_password.requestFocus();

                            }catch (Exception e){
                                Log.e("register avtivity", e.getMessage());
                                Toast.makeText(Tutor_register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}