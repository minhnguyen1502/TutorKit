package com.example.contact.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.contact.User;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper( Context context) {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT , date TEXT, email TEXT, image TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user");
        onCreate(sqLiteDatabase);
    }

    // Thêm dữ liệu
    public long add(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();// tạo đối tượng chứa dữ liệu
        // Đưa dữ liệu vào đối tượng
        cv.put("name", user.getName());
        cv.put("date", user.getDate());
        cv.put("email", user.getEmail());
        cv.put("image", user.getImage());

        return db.insert("user",null,cv);
    }
    // Hiển thị
    public ArrayList<User> getAll(){
        ArrayList<User> users = new ArrayList<>();// tạo danh sách rỗng
        SQLiteDatabase db = getReadableDatabase();
        // Tạo con trỏ đọc dữ liệu
        Cursor cursor = db.rawQuery("SELECT * FROM user", null);

        if (cursor.moveToFirst()){  // Con trỏ ở vị trí đầu tiên
            do {
                int id = cursor.getInt(0); //  Đọc dữ liệu từ id
                String name = cursor.getString(1);
                String date = cursor.getString(2);
                String email = cursor.getString(3);
                String image = cursor.getString(4);

                User user = new User(id,name,date,email,image);
                users.add(user);
            }while (cursor.moveToNext());
        }
        return users;
    }

    public int delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("user", "id=?", new String[]{String.valueOf(id)});

    }
}
