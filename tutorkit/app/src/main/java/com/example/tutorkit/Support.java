package com.example.tutorkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Support extends AppCompatActivity {

    TextView phone, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);

        phone.setOnClickListener(new View.OnClickListener() {
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
    }
}