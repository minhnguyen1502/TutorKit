package com.example.tutorkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorkit.Student.Student_home;
import com.example.tutorkit.Student.Tutors.Your_tutor;

public class Support extends AppCompatActivity {

    LinearLayout call_phone, email, fb, git, insta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        call_phone = findViewById(R.id.call_phone);
        email = findViewById(R.id.email);
        fb = findViewById(R.id.fb);
        git = findViewById(R.id.git);
        insta = findViewById(R.id.insta);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        call_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "0367959940"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // Handle the case where no activity can handle the intent
                    Toast.makeText(Support.this, "No app available to handle this action", Toast.LENGTH_SHORT).show();
                }
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("mailto: minhndgch200119@fpt.edu.vn"));
                if (i.resolveActivity(getPackageManager())!= null) {
                    startActivity(i);
                }else {
                    Toast.makeText(Support.this, "no app", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ndm010502/"));
                startActivity(i);
                if (i.resolveActivity(getPackageManager())!= null) {
                    startActivity(i);
                }else {
                    Toast.makeText(Support.this, "no app", Toast.LENGTH_SHORT).show();
                }
            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/mink_0105/"));
                startActivity(i);
                if (i.resolveActivity(getPackageManager())!= null) {
                    startActivity(i);
                }else {
                    Toast.makeText(Support.this, "no app", Toast.LENGTH_SHORT).show();
                }
            }
        });

        git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/minhnguyen1502/TutorKit"));
                startActivity(i);
                if (i.resolveActivity(getPackageManager())!= null) {
                    startActivity(i);
                }else {
                    Toast.makeText(Support.this, "no app", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            finish();
        } else if (id == R.id.action_search) {
//            Intent i = new Intent(Tutor_profile.this, UpdateEmail.class);
//            startActivity(i);
//            finish();
        } else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}