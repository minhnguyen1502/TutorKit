package com.example.tutorkit.Student.Account;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorkit.Login;
import com.example.tutorkit.Models.StatusAdd;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Calendar;
import java.util.Locale;

public class Student_register extends AppCompatActivity {
    private EditText edt_re_name, edt_re_email, edt_re_DOB, edt_re_phone,
            edt_re_parent_phone, edt_re_password, edt_re_confirm_password;
    private Spinner address;
    private RadioButton re_gender_selected;
    private RadioGroup group_re_gender;
    private Phonenumber.PhoneNumber swissNumberProto;
    private Phonenumber.PhoneNumber swissNumberProto2;
    private ImageView avatar;
    private Uri imgURI;
    private DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Student");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        TextView login = findViewById(R.id.txt_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Student_register.this, Login.class);
                startActivity(i);
            }
        });
        Toast.makeText(Student_register.this, "Register Student", Toast.LENGTH_SHORT).show();

        avatar = findViewById(R.id.avatar);
        edt_re_name = findViewById(R.id.edt_name);
        address = findViewById(R.id.address);
        edt_re_email = findViewById(R.id.edt_email);
        edt_re_DOB = findViewById(R.id.edt_DOB);
        edt_re_phone = findViewById(R.id.edt_phone);
        edt_re_parent_phone = findViewById(R.id.edt_parent_phone);
        edt_re_password = findViewById(R.id.edt_password);
        edt_re_confirm_password = findViewById(R.id.edt_confirm_password);

        group_re_gender = findViewById(R.id.group_gender);
        group_re_gender.clearCheck();

        // pick date
        edt_re_DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Student_register.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Người dùng đã chọn ngày. Cập nhật trường ngày (edate).
                                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
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

        // upload avatar
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imgURI = data.getData();
                            avatar.setImageURI(imgURI);
                        } else {
                            Toast.makeText(Student_register.this, "no image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photo = new Intent();
                photo.setAction(Intent.ACTION_GET_CONTENT);
                photo.setType("image/*");
                activityResultLauncher.launch(photo);
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
                String txt_address = address.getSelectedItem().toString();
                String txt_DOB = edt_re_DOB.getText().toString();
                String txt_phone = edt_re_phone.getText().toString();
                String txt_email = edt_re_email.getText().toString();
                String txt_phone_parent = edt_re_parent_phone.getText().toString();
                String txt_password = edt_re_password.getText().toString();
                String txt_confirm_password = edt_re_confirm_password.getText().toString();
                String txt_gender;

                // validate phone no.
                boolean isValid = false;
                try {
                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance(Student_register.this);
                    try {
                        swissNumberProto = phoneUtil.parse(txt_phone, Locale.getDefault().getCountry());
                    } catch (NumberParseException e) {
                        System.err.println("NumberParseException was thrown: " + e);
                    }
                    isValid = phoneUtil.isValidNumber(swissNumberProto); // returns true

                } catch (Exception e) {
                    e.printStackTrace();
                }

                boolean isValid2 = false;
                try {
                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance(Student_register.this);
                    try {
                        swissNumberProto2 = phoneUtil.parse(txt_phone_parent, Locale.getDefault().getCountry());
                    } catch (NumberParseException e) {
                        System.err.println("NumberParseException was thrown: " + e);
                    }
                    isValid2 = phoneUtil.isValidNumber(swissNumberProto2); // returns true

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (TextUtils.isEmpty(txt_name)) {
                    Toast.makeText(Student_register.this, "Enter your name", Toast.LENGTH_SHORT).show();
                    edt_re_name.setError("Name is required");
                    edt_re_name.requestFocus();
                }else if (imgURI == null) {
                    Toast.makeText(Student_register.this, "No avatar", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(txt_email)) {
                    Toast.makeText(Student_register.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    edt_re_email.setError("email is required");
                    edt_re_email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
                    Toast.makeText(Student_register.this, "re-enter your email", Toast.LENGTH_SHORT).show();
                    edt_re_email.setError("valid email is required");
                    edt_re_email.requestFocus();
                } else if (TextUtils.isEmpty(txt_DOB)) {
                    Toast.makeText(Student_register.this, "Enter your DOB", Toast.LENGTH_SHORT).show();
                    edt_re_DOB.setError("DOB is required");
                    edt_re_DOB.requestFocus();
                } else if (!isValid) {
                    Toast.makeText(Student_register.this, "re-Enter your phone", Toast.LENGTH_SHORT).show();
                    edt_re_phone.setError("Phone no. is not valid");
                    edt_re_phone.requestFocus();
                } else if (TextUtils.isEmpty(txt_phone)) {
                    Toast.makeText(Student_register.this, "Enter your phone", Toast.LENGTH_SHORT).show();
                    edt_re_phone.setError("phone is required");
                    edt_re_phone.requestFocus();
                } else if (txt_phone.length() != 10) {
                    Toast.makeText(Student_register.this, "Re-enter your phone no.", Toast.LENGTH_SHORT).show();
                    edt_re_phone.setError("Phone no. should be 10 digits");
                    edt_re_phone.requestFocus();
                }else if (!isValid2) {
                    Toast.makeText(Student_register.this, "re-Enter your parent phone", Toast.LENGTH_SHORT).show();
                    edt_re_parent_phone.setError("Phone no. is not valid");
                    edt_re_parent_phone.requestFocus();
                } else if (TextUtils.isEmpty(txt_phone_parent)) {
                    Toast.makeText(Student_register.this, "Enter your parent phone", Toast.LENGTH_SHORT).show();
                    edt_re_parent_phone.setError("phone is required");
                    edt_re_parent_phone.requestFocus();
                } else if (txt_phone_parent.length() != 10) {
                    Toast.makeText(Student_register.this, "Re-enter your parent phone no.", Toast.LENGTH_SHORT).show();
                    edt_re_parent_phone.setError("Phone no. should be 10 digits");
                    edt_re_parent_phone.requestFocus();
                } else if (group_re_gender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(Student_register.this, "select your gender", Toast.LENGTH_SHORT).show();
                    re_gender_selected.setError("gender is required");
                    re_gender_selected.requestFocus();
                } else if (TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(Student_register.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    edt_re_password.setError("password is required");
                    edt_re_password.requestFocus();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(Student_register.this, "password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    edt_re_password.setError("password is too weak");
                    edt_re_password.requestFocus();
                } else if (TextUtils.isEmpty(txt_confirm_password)) {
                    Toast.makeText(Student_register.this, "Enter your confirm password", Toast.LENGTH_SHORT).show();
                    edt_re_confirm_password.setError("confirm-password is required");
                    edt_re_confirm_password.requestFocus();
                } else if (!txt_password.equals(txt_confirm_password)) {
                    Toast.makeText(Student_register.this, "password confirmation must be same the password", Toast.LENGTH_SHORT).show();
                    edt_re_confirm_password.setError("password confirmation is required");
                    edt_re_confirm_password.requestFocus();
                    // clear the enter password
                    edt_re_password.clearComposingText();
                    edt_re_confirm_password.clearComposingText();
                } else {
                    txt_gender = re_gender_selected.getText().toString();
                    registerStudent(txt_name, txt_email, txt_DOB, txt_gender, txt_phone, txt_address, txt_phone_parent, txt_password,imgURI);
                }

            }
        });
    }

    private void registerStudent(String txt_name, String txt_email, String txt_dob, String txt_gender, String txt_phone, String txt_address, String txt_phone_parent, String txt_password, Uri img) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

//        final  StorageReference imgReference  =  storageReference.child(System.currentTimeMillis()+"."+getFilesExtension(img));
        // create profile
        firebaseAuth.createUserWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(Student_register.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            // display name
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(txt_name).build();
                            firebaseUser.updateProfile(profileChangeRequest);
                            updateImageAndCreateStudent(firebaseUser, img);
                        } else {
                            try {
                                throw task.getException();

                            } catch (FirebaseAuthWeakPasswordException e) {
                                edt_re_password.setError("too weak");
                                edt_re_password.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                edt_re_password.setError("Invalid or already");
                                edt_re_password.requestFocus();

                            } catch (FirebaseAuthUserCollisionException e) {
                                edt_re_email.setError("another email");
                                edt_re_email.requestFocus();

                            } catch (Exception e) {
                                Log.e("register avtivity", e.getMessage());
                                Toast.makeText(Student_register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    private void updateImageAndCreateStudent(FirebaseUser firebaseUser, Uri img) {
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference riversRef = storageRef.child("images/" + img.getLastPathSegment());
                        riversRef.putFile(img).continueWithTask(task -> {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return riversRef.getDownloadUrl();
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Log.e("TAG", "updateImage: " + downloadUri);
                                createUser(firebaseUser, downloadUri.toString());
                            } else {
                                Toast.makeText(Student_register.this, "fail", Toast.LENGTH_SHORT).show();                            }
                        });
                    }

                    private void createUser(FirebaseUser firebaseUser, String urlImage) {
                        Student student = new Student(firebaseUser.getUid(),txt_name,txt_dob, txt_gender,txt_address, txt_phone, txt_phone_parent, urlImage,new StatusAdd());

                        referenceProfile.child(firebaseUser.getUid()).setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    // sent verification to mail
                                    firebaseUser.sendEmailVerification();
                                    Toast.makeText(Student_register.this, "Register success. please verify email", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(Student_register.this, "Register Failed. ", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                });
    }


}