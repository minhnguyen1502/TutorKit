package com.example.tutorkit.Tutor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tutorkit.Models.Tutor;
import com.example.tutorkit.R;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class Edit_tutor_profile extends AppCompatActivity {


    private EditText edt_name, edt_DOB, edt_phone,
            edt_intro;
    private Spinner edit_subject, edit_address;
    private RadioButton gender_selected;
    private RadioGroup group_gender;
    private String name, phone, gender, DOB, address, subject, intro;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ImageView avatar;
    private Uri imgURI;
    private String img;
    private Phonenumber.PhoneNumber swissNumberProto;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tutors");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tutor_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        edt_name = findViewById(R.id.edt_name);
        edt_DOB = findViewById(R.id.edt_DOB);
        edit_address = findViewById(R.id.address);
        group_gender = findViewById(R.id.group_gender);
        edt_phone = findViewById(R.id.edt_phone);
        edit_subject = findViewById(R.id.subject);
        edt_intro = findViewById(R.id.edt_intro);
        avatar = findViewById(R.id.avatar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // show profile
        showProfile(firebaseUser);
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
                            Toast.makeText(Edit_tutor_profile.this, "no image", Toast.LENGTH_SHORT).show();
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


        boolean isValid = false;
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance(Edit_tutor_profile.this);
            try {
                swissNumberProto = phoneUtil.parse(phone, Locale.getDefault().getCountry());
            } catch (NumberParseException e) {
                System.err.println("NumberParseException was thrown: " + e.toString());
            }
            isValid = phoneUtil.isValidNumber(swissNumberProto); // returns true

        }catch (Exception e){
            e.printStackTrace();
        }


        gender = gender_selected.getText().toString();
        name = edt_name.getText().toString();
        DOB = edt_DOB.getText().toString();
        phone = edt_phone.getText().toString();
        address = edit_address.getSelectedItem().toString();
        subject = edit_subject.getSelectedItem().toString();
        intro = edt_intro.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(Edit_tutor_profile.this, "Enter your name", Toast.LENGTH_SHORT).show();
            edt_name.setError("Name is required");
            edt_name.requestFocus();
        } else if (TextUtils.isEmpty(DOB)) {
            Toast.makeText(Edit_tutor_profile.this, "Enter your DOB", Toast.LENGTH_SHORT).show();
            edt_DOB.setError("DOB is required");
            edt_DOB.requestFocus();
        } else if (!isValid) {
            Toast.makeText(Edit_tutor_profile.this, "re-Enter your phone", Toast.LENGTH_SHORT).show();
            edt_phone.setError("Phone no. is not valid");
            edt_phone.requestFocus();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(Edit_tutor_profile.this, "Enter your phone", Toast.LENGTH_SHORT).show();
            edt_phone.setError("phone is required");
            edt_phone.requestFocus();
        } else if (phone.length() != 10) {
            Toast.makeText(Edit_tutor_profile.this, "Re-enter your phone no.", Toast.LENGTH_SHORT).show();
            edt_phone.setError("Phone no. should be 10 digits");
            edt_phone.requestFocus();
        } else if (TextUtils.isEmpty(intro)) {
            Toast.makeText(Edit_tutor_profile.this, "Enter your introduction", Toast.LENGTH_SHORT).show();
            edt_intro.setError("class is required");
            edt_intro.requestFocus();
        } else if (TextUtils.isEmpty(gender_selected.getText())) {
            Toast.makeText(Edit_tutor_profile.this, "select your gender", Toast.LENGTH_SHORT).show();
            gender_selected.setError("gender is required");
            gender_selected.requestFocus();
        }else {

//        // enter data into firebase
//
//            Tutor tutor = new Tutor(DOB, address,phone, gender, subject, intro, imgURI);
//            // extract tutor reference from Database for register tutor
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tutors");
//
//            String tutorID = firebaseUser.getUid();
//
//            databaseReference.child(tutorID).setValue(tutor).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()){
//
//                        // new display name
//                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
//                        firebaseUser.updateProfile(userProfileChangeRequest);
//
//                        Toast.makeText(Edit_tutor_profile.this, "Update success", Toast.LENGTH_SHORT).show();
//
//                        Intent i = new Intent(Edit_tutor_profile.this, Tutor_profile.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
//                                Intent.FLAG_ACTIVITY_CLEAR_TASK|
//                                Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(i);
//                        finish();
//                    }else {
//                        try {
//                            throw task.getException();
//
//                        }catch (Exception e){
//                            Toast.makeText(Edit_tutor_profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            });
            updateTutor(firebaseUser, imgURI);

        }
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String tutor = firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tutors");
        reference.child(tutor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tutor tutor = snapshot.getValue(Tutor.class);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Edit_tutor_profile.this, R.array.address, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                edit_address.setAdapter(adapter);

                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(Edit_tutor_profile.this, R.array.subject, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                edit_subject.setAdapter(adapter1);
                
                if (tutor != null){
                    name = firebaseUser.getDisplayName();
                    phone = tutor.getPhone();
                    gender = tutor.getGender();
                    DOB = tutor.getDOB();
                    Glide
                            .with(Edit_tutor_profile.this)
                            .load(tutor.getImg())
                            .centerCrop()
                            .into(avatar);
                    address = tutor.getAddress();
                    subject = tutor.getSubject();
                    intro = tutor.getIntro();

                    edt_name.setText(name);
                    edt_DOB.setText(DOB);
                    edt_phone.setText(phone);
                    edit_address.setAdapter(adapter);
                    edit_subject.setAdapter(adapter1);
                    edt_intro.setText(intro);

                    String[] arrSubject = getResources().getStringArray(R.array.subject);
                    String[] arrAddress = getResources().getStringArray(R.array.address);

                    edit_subject.setSelection(Arrays.asList(arrSubject).indexOf(subject));
                    edit_address.setSelection(Arrays.asList(arrAddress).indexOf(address));

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
    private void updateTutor(FirebaseUser firebaseUser, Uri img) {
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
//                createUser(firebaseUser, downloadUri.toString());
                Tutor tutor = new Tutor(DOB, address,phone, gender, subject, intro, downloadUri.toString());

                databaseReference.child(firebaseUser.getUid()).setValue(tutor).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                        // new display name
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        firebaseUser.updateProfile(userProfileChangeRequest);

                        Toast.makeText(Edit_tutor_profile.this, "Update success", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(Edit_tutor_profile.this, Tutor_profile.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
//                        finish();
                    }else {
                        try {
                            throw task.getException();

                        }catch (Exception e){
                            Toast.makeText(Edit_tutor_profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                });
            } else {
                Toast.makeText(Edit_tutor_profile.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        // update profile
        if (id == R.id.done) {
            updateProfile(firebaseUser);
//            finish();
        }
        else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
