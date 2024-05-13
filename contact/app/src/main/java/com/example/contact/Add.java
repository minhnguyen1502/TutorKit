package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.contact.db.DBHelper;

import java.util.Calendar;

public class Add extends AppCompatActivity  {

    EditText ename;

    EditText edate;
    EditText eEmail;

    Button save, view;

    private ImageView selectedImage; // Thêm biến ImageView

    private Uri selectedImageUri = null;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        view = findViewById(R.id.view);
        save = findViewById(R.id.save);
        ename = findViewById(R.id.ename);
        edate = findViewById(R.id.edate);
        eEmail = findViewById(R.id.eEmail);
        selectedImage = findViewById(R.id.selectedImage);

        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); // Chỉ chọn các tệp hình ảnh.
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Add.this, MainActivity.class));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ename.getText().toString().trim();
                String date = edate.getText().toString().trim();
                String email = eEmail.getText().toString().trim();


                if (name.isEmpty() || date.isEmpty() ||
                        email.isEmpty()) {
                    Toast.makeText(Add.this, "Please complete all information", Toast.LENGTH_SHORT).show();
                }else {

                    DBHelper dbHelper = new DBHelper(Add.this);

                    if (selectedImageUri != null) {
                        // Nếu có hình ảnh đã được chọn
                        String imagePath = selectedImageUri.toString();

                        User user = new User(name, date, email, imagePath);

                        long result = dbHelper.add(user);

                        if (result > 0) {
                            Toast.makeText(Add.this, "Saved " , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Add.this, "Failed " , Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Nếu không có hình ảnh, bạn có thể xử lý nó ở đây (ví dụ: hiển thị thông báo lỗi).
                        Toast.makeText(Add.this, "Don't have image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Người dùng đã chọn ngày. Cập nhật trường ngày (edate).
                        String selectedDate =dayOfMonth + "/"+ (month+1)+"/"+year;
                        edate.setText(selectedDate);
                    }
                },
                // Truyền ngày hiện tại làm ngày mặc định cho DatePickerDialog.
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            selectedImage.setImageURI(selectedImageUri);
        }
    }
}