package com.example.tutorkit.Student.Assignment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorkit.Models.AssignmentModel;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.SubmitAssignmentModel;
import com.example.tutorkit.R;
import com.example.tutorkit.Student.Account.Student_register;
import com.example.tutorkit.Tutor.Assignment.SubmitAssignment;
import com.example.tutorkit.Tutor.Assignment.SubmitAssignmentAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Submit extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<AssignmentModel> assignmentModelArrayList;
    SubmitAdpater adapter;
    String idSubmit;
    String imageURL;
    Uri uri;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        idSubmit = getIntent().getStringExtra("idSubmit");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        assignmentModelArrayList = new ArrayList<>();
        readData();
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            // Handle image selection
//                            img.setImageURI(uri);
                            Log.e("TAG", "onActivityResult: " + uri);
                            ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
                            viewDialogAdd.showDialog(Submit.this, uri);
                        } else {
                            Toast.makeText(Submit.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void readData() {
        databaseReference.child("Assignment").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assignmentModelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AssignmentModel assignmentModel = dataSnapshot.getValue(AssignmentModel.class);
                    try {
                        if (Objects.equals(assignmentModel.getIdStudent(), FirebaseAuth.getInstance().getUid())) {
                            if (Objects.equals(assignmentModel.getIdSubmit(), idSubmit)) {
                                assignmentModelArrayList.add(assignmentModel);

                            }
                        }
                    } catch (Exception e) {
                        Log.e("TAG", "onDataChange: " + e.getMessage());
                    }

                }
                adapter = new SubmitAdpater(Submit.this, assignmentModelArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            finish();
        } else if (id == R.id.add) {
//
            Intent photo = new Intent();
            photo.setAction(Intent.ACTION_GET_CONTENT);
            photo.setType("image/*");
            activityResultLauncher.launch(photo);
        } else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private class ViewDialogAdd {
        public void showDialog(Context context, Uri data) {
            final Dialog dialog = new Dialog(context);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_add_submit);

            ImageView img = dialog.findViewById(R.id.img);

            Button buttonAdd = dialog.findViewById(R.id.buttonAdd);
            ImageView buttonCancel = dialog.findViewById(R.id.cancel);

            img.setImageURI(data);

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveData();
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


        }

        public void saveData() {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Assignment Images")
                    .child(uri.getLastPathSegment());

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    imageURL = urlImage.toString();
                    uploadData();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }

        public void uploadData() {
            String idStudent = FirebaseAuth.getInstance().getUid();
            String id = "assignment" + new Date().getTime();
            String n_idSubmit = idSubmit;
            AssignmentModel assignmentModel = new AssignmentModel(id, imageURL, n_idSubmit, idStudent);
            FirebaseDatabase.getInstance().getReference("Assignment").child(id)
                    .setValue(assignmentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Submit.this, "Saved", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Submit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}
