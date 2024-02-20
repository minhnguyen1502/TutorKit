package com.example.tutorkit.Tutor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
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
import com.example.tutorkit.Models.Tutor;
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

public class Tutor_register extends AppCompatActivity {

    private EditText edt_re_name, edt_re_email, edt_re_DOB, edt_re_phone,
            edt_re_intro, edt_re_password, edt_re_confirm_password;
    private Spinner address;
    private Spinner subject;
    private RadioButton re_gender_selected;
    private RadioGroup group_re_gender;
    private Phonenumber.PhoneNumber swissNumberProto;
    private ImageView avatar;
    private Uri imgURI;
    private final DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Tutor");
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

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

        Toast.makeText(Tutor_register.this, "Register Tutor", Toast.LENGTH_SHORT).show();

        avatar = findViewById(R.id.avatar);
        edt_re_name = findViewById(R.id.edt_name);
        address = findViewById(R.id.address);
        edt_re_email = findViewById(R.id.edt_email);
        edt_re_DOB = findViewById(R.id.edt_DOB);
        edt_re_phone = findViewById(R.id.edt_phone);
        edt_re_intro = findViewById(R.id.edt_intro);
        subject = findViewById(R.id.subject);
        avatar = findViewById(R.id.avatar);
        edt_re_password = findViewById(R.id.edt_password);
        edt_re_confirm_password = findViewById(R.id.edt_confirm_password);

        group_re_gender = findViewById(R.id.group_gender);
        group_re_gender.clearCheck();
        // pick date
        edt_re_DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Tutor_register.this,
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
                            Toast.makeText(Tutor_register.this, "no image", Toast.LENGTH_SHORT).show();
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
                String txt_subject = subject.getSelectedItem().toString();
                String txt_intro = edt_re_intro.getText().toString();
                String txt_password = edt_re_password.getText().toString();
                String txt_confirm_password = edt_re_confirm_password.getText().toString();
                String txt_gender;

                // validate phone no.
                boolean isValid = false;
                try {
                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance(Tutor_register.this);
                    try {
                        swissNumberProto = phoneUtil.parse(txt_phone, Locale.getDefault().getCountry());
                    } catch (NumberParseException e) {
                        System.err.println("NumberParseException was thrown: " + e);
                    }
                    isValid = phoneUtil.isValidNumber(swissNumberProto); // returns true

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(txt_name)) {
                    Toast.makeText(Tutor_register.this, "Enter your name", Toast.LENGTH_SHORT).show();
                    edt_re_name.setError("Name is required");
                    edt_re_name.requestFocus();
                } else if (imgURI == null) {
                    Toast.makeText(Tutor_register.this, "No avatar", Toast.LENGTH_SHORT).show();
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
                } else if (!isValid) {
                    Toast.makeText(Tutor_register.this, "re-Enter your phone", Toast.LENGTH_SHORT).show();
                    edt_re_phone.setError("Phone no. is not valid");
                    edt_re_phone.requestFocus();
                } else if (TextUtils.isEmpty(txt_phone)) {
                    Toast.makeText(Tutor_register.this, "Enter your phone", Toast.LENGTH_SHORT).show();
                    edt_re_phone.setError("phone is required");
                    edt_re_phone.requestFocus();
                } else if (txt_phone.length() != 10) {
                    Toast.makeText(Tutor_register.this, "Re-enter your phone no.", Toast.LENGTH_SHORT).show();
                    edt_re_phone.setError("Phone no. should be 10 digits");
                    edt_re_phone.requestFocus();
                } else if (TextUtils.isEmpty(txt_intro)) {
                    Toast.makeText(Tutor_register.this, "Enter Introduction yourself", Toast.LENGTH_SHORT).show();
                    edt_re_intro.setError(" required");
                    edt_re_intro.requestFocus();
                } else if (group_re_gender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(Tutor_register.this, "select your gender", Toast.LENGTH_SHORT).show();
                    re_gender_selected.setError("gender is required");
                    re_gender_selected.requestFocus();
                } else if (TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(Tutor_register.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    edt_re_password.setError("password is required");
                    edt_re_password.requestFocus();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(Tutor_register.this, "password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    edt_re_password.setError("password is too weak");
                    edt_re_password.requestFocus();
                } else if (TextUtils.isEmpty(txt_confirm_password)) {
                    Toast.makeText(Tutor_register.this, "Enter your confirm password", Toast.LENGTH_SHORT).show();
                    edt_re_confirm_password.setError("confirm-password is required");
                    edt_re_confirm_password.requestFocus();
                } else if (!txt_password.equals(txt_confirm_password)) {
                    Toast.makeText(Tutor_register.this, "password confirmation must be same the password", Toast.LENGTH_SHORT).show();
                    edt_re_confirm_password.setError("password confirmation is required");
                    edt_re_confirm_password.requestFocus();
                    // clear the enter password
                    edt_re_password.clearComposingText();
                    edt_re_confirm_password.clearComposingText();
                } else {
                    txt_gender = re_gender_selected.getText().toString();
                    registerTutor(txt_name, txt_email, txt_DOB, txt_gender, txt_phone, txt_address, txt_subject, txt_intro, txt_password, imgURI);
                }

            }
        });
    }

    // register Tutor
    private void registerTutor(String txt_name, String txt_email, String txt_dob, String txt_phone, String txt_gender, String txt_address, String txt_subject, String txt_intro, String txt_password, Uri img) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

//        final  StorageReference imgReference  =  storageReference.child(System.currentTimeMillis()+"."+getFilesExtension(img));
        // create profile
        firebaseAuth.createUserWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(Tutor_register.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            // display name
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(txt_name).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            Tutor tutor = new Tutor(txt_address, txt_dob, txt_email, txt_gender, txt_phone, txt_subject, txt_intro, img.toString());

                            referenceProfile.child(firebaseUser.getUid()).setValue(tutor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        // sent verification to mail
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(Tutor_register.this, "Register success. please verify email", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(Tutor_register.this, "Register Failed. ", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

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
                                Toast.makeText(Tutor_register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

//    private String getFilesExtension(Uri img) {
//        ContentResolver contentResolver = getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(contentResolver.getType(img));
//    }

}