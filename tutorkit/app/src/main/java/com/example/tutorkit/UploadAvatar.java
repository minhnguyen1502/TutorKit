package com.example.tutorkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PageRange;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadAvatar extends AppCompatActivity {

    private ImageView avatar;
    private FirebaseAuth firebaseAuth;
    private Button takePicture, uploadPicture;
    private StorageReference storageReference;
    FirebaseUser firebaseUser;
    private final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_avatar);

        firebaseAuth = FirebaseAuth.getInstance();
        avatar = findViewById(R.id.choose_avatar);
        takePicture = findViewById(R.id.takePicture);
        uploadPicture = findViewById(R.id.uploadPicture);
        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");
        Uri uri = firebaseUser.getPhotoUrl();

        Picasso.with(UploadAvatar.this).load(uri).into(avatar);

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });



    }

    private void openFile() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST &&
            resultCode == RESULT_OK &&
            data != null &&
            data.getData() != null ){
            uriImg = data.getData();
            avatar.setImageURI(uriImg);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.update_profile) {
            Intent i = new Intent(UploadAvatar.this, Edit_tutor_profile.class);
            startActivity(i);
        } else if (id == R.id.update_email) {
            Intent i = new Intent(UploadAvatar.this, UpdateEmail.class);
            startActivity(i);
        } else if (id == R.id.update_password) {
            Intent i = new Intent(UploadAvatar.this, UpdatePassword.class);
            startActivity(i);
        }
        else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}