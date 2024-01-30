package com.example.tutorkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tutorkit.Models.Tutor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Edit_tutor_profile extends AppCompatActivity {


    private EditText edt_name, edt_email, edt_DOB, edt_phone, edt_address,
            edt_subject, edt_class;
    private RadioButton gender_selected;
    private RadioGroup group_gender;
    private String name, email, phone, gender, DOB, address, subject, t_class;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tutor_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_DOB = findViewById(R.id.edt_DOB);
        edt_address = findViewById(R.id.edt_address);
        group_gender = findViewById(R.id.group_gender);
        edt_phone = findViewById(R.id.edt_phone);
        edt_subject = findViewById(R.id.edt_subject);
        edt_class = findViewById(R.id.edt_class);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // show profile
        showProfile(firebaseUser);

        // update profile
        Button done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(firebaseUser);
            }
        });
        // pick date
        edt_DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Edit_tutor_profile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Người dùng đã chọn ngày. Cập nhật trường ngày (edate).
                                String selectedDate =dayOfMonth + "/"+ (month+1)+"/"+year;
                                edt_DOB.setText(selectedDate);
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
    }


    private void updateProfile(FirebaseUser firebaseUser) {
        int selectedGenderID = group_gender.getCheckedRadioButtonId();
        gender_selected = findViewById(selectedGenderID);

        // validate phone no. sung Matcher and Pattern
        String mobileRegex = "^0\\d{2}-\\d{7}$";
        Matcher phoneMatcher;
        Pattern phonePattern = Pattern.compile(mobileRegex);
        phoneMatcher =  phonePattern.matcher(phone);



        if (TextUtils.isEmpty(name)) {
            Toast.makeText(Edit_tutor_profile.this, "Enter your name", Toast.LENGTH_SHORT).show();
            edt_name.setError("Name is required");
            edt_name.requestFocus();
        } else if (TextUtils.isEmpty(DOB)) {
            Toast.makeText(Edit_tutor_profile.this, "Enter your DOB", Toast.LENGTH_SHORT).show();
            edt_DOB.setError("DOB is required");
            edt_DOB.requestFocus();
        } else if (phoneMatcher.find()) {
            Toast.makeText(Edit_tutor_profile.this, "re-Enter your phone", Toast.LENGTH_SHORT).show();
            edt_phone.setError("Phone no. is not valid");
            edt_phone.requestFocus();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(Edit_tutor_profile.this, "Enter your phone", Toast.LENGTH_SHORT).show();
            edt_phone.setError("phone is required");
            edt_phone.requestFocus();
        } else if (phone.length() !=10) {
            Toast.makeText(Edit_tutor_profile.this, "Re-enter your phone no.", Toast.LENGTH_SHORT).show();
            edt_phone.setError("Phone no. should be 10 digits");
            edt_phone.requestFocus();
        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(Edit_tutor_profile.this, "Enter your address", Toast.LENGTH_SHORT).show();
            edt_address.setError("address is required");
            edt_address.requestFocus();
        } else if (TextUtils.isEmpty(subject)) {
            Toast.makeText(Edit_tutor_profile.this, "Enter your subject", Toast.LENGTH_SHORT).show();
            edt_subject.setError("subject is required");
            edt_subject.requestFocus();
        }else if (TextUtils.isEmpty(t_class)) {
            Toast.makeText(Edit_tutor_profile.this, "Enter your class", Toast.LENGTH_SHORT).show();
            edt_class.setError("class is required");
            edt_class.requestFocus();
        } else if (TextUtils.isEmpty(gender_selected.getText())) {
            Toast.makeText(Edit_tutor_profile.this, "select your gender", Toast.LENGTH_SHORT).show();
            gender_selected.setError("gender is required");
            gender_selected.requestFocus();
        }else {
            gender = gender_selected.getText().toString();
            name = edt_name.getText().toString();
            DOB = edt_DOB.getText().toString();
            phone = edt_phone.getText().toString();
            address = edt_address.getText().toString();
            subject = edt_subject.getText().toString();
            t_class = edt_class.getText().toString();
        // enter data into firebase

            Tutor tutor = new Tutor(DOB, address,phone, gender, subject, t_class);
            // extract tutor reference from Database for register tutor
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Tutor");

            String tutorID = firebaseUser.getUid();

            databaseReference.child(tutorID).setValue(tutor).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        // new display name
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        firebaseUser.updateProfile(userProfileChangeRequest);

                        Toast.makeText(Edit_tutor_profile.this, "Update success", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(Edit_tutor_profile.this, Tutor_Profile.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }else {
                        try {
                            throw task.getException();

                        }catch (Exception e){
                            Toast.makeText(Edit_tutor_profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String tutor = firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Tutor");
        reference.child(tutor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tutor tutor = snapshot.getValue(Tutor.class);
                if (tutor != null){
                    name = firebaseUser.getDisplayName();
                    email = tutor.getEmail();
                    phone = tutor.getPhone();
                    gender = tutor.getGender();
                    DOB = tutor.getDOB();
                    address = tutor.getAddress();
                    subject = tutor.getSubject();
                    t_class = tutor.getT_class();

                    edt_name.setText(name);
                    edt_email.setText(email);
                    edt_DOB.setText(DOB);
                    edt_phone.setText(phone);
                    edt_address.setText(address);
                    edt_subject.setText(subject);
                    edt_class.setText(t_class);

                    if (gender.equals("Male")){
                        gender_selected = findViewById(R.id.Rbtn_male);
                    }else {
                        gender_selected = findViewById(R.id.Rbtn_female);
                    } gender_selected.setChecked(true);
                }else {
                    Toast.makeText(Edit_tutor_profile.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Edit_tutor_profile.this, "Something Wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }
}