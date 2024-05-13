package com.example.trails.database;

public class Hike {
    public static final String TABLE_NAME = "hike";
    public static final String COLUMN_ID = "hike_id";
    public static final String COLUMN_NAME = "hike_name";
    public static final String COLUMN_LOCATION = "hike_location";
    public static final String COLUMN_DATE = "hike_date";
    public static final String COLUMN_LEVEL = "hike_level";
    public static final String COLUMN_DESCRIPTION = "hike_description";
    public static final String COLUMN_LENGTH = "hike_length";
    public static final String COLUMN_PARKING = "hike_parking";
    public static final String COLUMN_VEHICLE = "hike_vehicle";

    private int id;
    private String name;
    private String location;
    private String date;
    private String level;
    private String vehicle;
    private String description;
    private double length;
    private int parking;

    public Hike() {
    }

    public Hike(int id, String name, String location, String date, String level, String description,String vehicle, double length, int parking) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.level = level;
        this.description = description;
        this.vehicle = vehicle;
        this.length = length;
        this.parking = parking;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int isParking() {
        return parking;
    }

    public void setParking(int parking) {
        this.parking = parking;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_LOCATION + " TEXT,"
                    + COLUMN_DATE + " TEXT,"
                    + COLUMN_LEVEL + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_VEHICLE + " TEXT,"
                    + COLUMN_LENGTH + " REAL,"
                    + COLUMN_PARKING + " INTEGER"
                    + ")";

    @Override
    public String toString() {
        return "Hike{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", date='" + date + '\'' +
                ", level='" + level + '\'' +
                ", description='" + description + '\'' +
                ", vehicle='" + vehicle + '\'' +
                ", length=" + length +
                ", parking=" + parking +
                '}';
    }
}
