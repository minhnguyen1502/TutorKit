package com.example.trails.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_TABLE = "hike_db";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_TABLE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Hike.CREATE_TABLE);
        sqLiteDatabase.execSQL(Observation.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Hike.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Observation.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // Data of Hike
    public long addHike(String name, String location, String date, String level, String description,String vehicle, double length, int parking){
        SQLiteDatabase db = this.getWritableDatabase();
        //Class object to save db
        ContentValues cv = new ContentValues();
        cv.put(Hike.COLUMN_NAME, name);
        cv.put(Hike.COLUMN_LOCATION, location);
        cv.put(Hike.COLUMN_DATE, date);
        cv.put(Hike.COLUMN_LEVEL, level);
        cv.put(Hike.COLUMN_DESCRIPTION, description);
        cv.put(Hike.COLUMN_VEHICLE, vehicle);
        cv.put(Hike.COLUMN_LENGTH, length);
        cv.put(Hike.COLUMN_PARKING, parking);
        //Thêm db vào row
        long id = db.insert(Hike.TABLE_NAME, null, cv);
        //Đóng db
        db.close();
        //Trả về id
        return id;
    }

    public void editHike(int id, String name, String location, String date, String level, String description,String vehicle, double length, int parking){
        //Ghi dữ liệu vào trong db
        SQLiteDatabase db = this.getWritableDatabase();
        //Class object to save db
        ContentValues cv = new ContentValues();
        cv.put(Hike.COLUMN_NAME, name);
        cv.put(Hike.COLUMN_LOCATION, location);
        cv.put(Hike.COLUMN_DATE, date);
        cv.put(Hike.COLUMN_LEVEL, level);
        cv.put(Hike.COLUMN_DESCRIPTION, description);
        cv.put(Hike.COLUMN_VEHICLE, vehicle);
        cv.put(Hike.COLUMN_LENGTH, length);
        cv.put(Hike.COLUMN_PARKING, parking);
        db.update(Hike.TABLE_NAME, cv, Hike.COLUMN_ID + "=?",
                new String[]{String.valueOf((id))});
        //Đóng db
        db.close();
    }

    public void deleteHike(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Hike.TABLE_NAME, Hike.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllHike(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Hike.TABLE_NAME);
        db.close();
    }

    public ArrayList<Hike> getAllHike(){
        //Create array list
        ArrayList<Hike> hikes = new ArrayList<>();
        //get data
        String selectQuery = " SELECT * FROM " + Hike.TABLE_NAME + " ORDER BY " +
                Hike.COLUMN_ID + " DESC ";
        //get read db
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                Hike hike = new Hike();
                hike.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COLUMN_ID)));
                hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_NAME)));
                hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_LOCATION)));
                hike.setDate(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_DATE)));
                hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_DESCRIPTION)));
                hike.setLevel(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_LEVEL)));
                hike.setVehicle(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_VEHICLE)));
                hike.setLength(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_LENGTH))));
                int parkingValue = cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COLUMN_PARKING));
                hike.setParking(parkingValue);
                hikes.add(hike);
            }while (cursor.moveToNext());
        }
        db.close();
        return hikes;
    }

    public ArrayList<Hike> getSearchHike(String query){
        ArrayList<Hike> hikeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryToSearch = "SELECT * FROM " + Hike.TABLE_NAME + " WHERE " +
                Hike.COLUMN_NAME + " LIKE '%" + query + "%'";
        Cursor cursor = db.rawQuery(queryToSearch, null);
        if(cursor.moveToFirst()){
            do {
                Hike hike = new Hike();
                hike.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COLUMN_ID)));
                hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_NAME)));
                hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_LOCATION)));
                hike.setDate(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_DATE)));
                hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_DESCRIPTION)));
                hike.setLevel(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_LEVEL)));
                hike.setVehicle(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_VEHICLE)));
                hike.setLength(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_LENGTH))));
                int parkingValue = cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COLUMN_PARKING));
                hike.setParking(parkingValue);
                hikeList.add(hike);
            }while (cursor.moveToNext());
        }
        db.close();
        return hikeList;
    }

    public Hike getHikeById(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + Hike.TABLE_NAME + " WHERE " + Hike.COLUMN_ID + " = " + id;
        Cursor cursor = database.rawQuery(query, null);

        Hike hike = null;

        if (cursor.moveToFirst()) {
            hike = new Hike();
            hike.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COLUMN_ID)));
            hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_NAME)));
            hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_LOCATION)));
            hike.setDate(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_DATE)));
            hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_DESCRIPTION)));
            hike.setLevel(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_LEVEL)));
            hike.setVehicle(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_VEHICLE)));
            hike.setLength(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_LENGTH))));
            int parkingValue = cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COLUMN_PARKING));
            hike.setParking(parkingValue);
        }

        cursor.close();
        database.close();
        return hike;
    }

    //Data of Observation
    public long addObservation(String observation, String dateOfTime, String comment, int hikeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Observation.COLUMN_NAME, observation);
        cv.put(Observation.COLUMN_DATE, dateOfTime);
        cv.put(Observation.COLUMN_COMMENT, comment);
        cv.put(Observation.COLUMN_FOREIGN_KEY, hikeID);

        long id = db.insert(Observation.TABLE_NAME, null, cv);
        db.close();
        return id;
    }

    public void eitObservation(int id, String observation, String dateOfTime, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Observation.COLUMN_NAME, observation);
        cv.put(Observation.COLUMN_DATE, dateOfTime);
        cv.put(Observation.COLUMN_COMMENT, comment);

        db.update(Observation.TABLE_NAME, cv, Observation.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteObservation(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Observation.TABLE_NAME, Observation.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public ArrayList<Observation> getObservationsForHike(int hikeID) {
        ArrayList<Observation> observations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + Observation.TABLE_NAME + " WHERE " +
                Observation.COLUMN_FOREIGN_KEY + " = " + hikeID;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Observation observation = new Observation();
                observation.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Observation.COLUMN_ID)));
                observation.setObservation(cursor.getString(cursor.getColumnIndexOrThrow(Observation.COLUMN_NAME)));
                observation.setDateOfTime(cursor.getString(cursor.getColumnIndexOrThrow(Observation.COLUMN_DATE)));
                observation.setComment(cursor.getString(cursor.getColumnIndexOrThrow(Observation.COLUMN_COMMENT)));
                observation.setHikeID(cursor.getInt(cursor.getColumnIndexOrThrow(Observation.COLUMN_FOREIGN_KEY)));
                observations.add(observation);
            } while (cursor.moveToNext());
        }
        db.close();
        return observations;
    }

    public Observation getObservationById(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + Observation.TABLE_NAME + " WHERE " + Hike.COLUMN_ID + " = " + id;
        Cursor cursor = database.rawQuery(query, null);

        Observation observation = null;

        if (cursor.moveToFirst()) {
            observation = new Observation();
            observation.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Observation.COLUMN_ID)));
            observation.setObservation(cursor.getString(cursor.getColumnIndexOrThrow(Observation.COLUMN_NAME)));
            observation.setDateOfTime(cursor.getString(cursor.getColumnIndexOrThrow(Observation.COLUMN_DATE)));
            observation.setComment(cursor.getString(cursor.getColumnIndexOrThrow(Observation.COLUMN_COMMENT)));
            observation.setHikeID(cursor.getInt(cursor.getColumnIndexOrThrow(Observation.COLUMN_FOREIGN_KEY)));
        }

        cursor.close();
        database.close();
        return observation;
    }

}
