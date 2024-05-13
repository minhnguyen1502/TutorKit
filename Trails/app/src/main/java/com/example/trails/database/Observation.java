package com.example.trails.database;

public class Observation {

    public static final String TABLE_NAME = "observations";
    public static final String COLUMN_ID = "observation_id";
    public static final String COLUMN_NAME = "observation";
    public static final String COLUMN_DATE = "observation_date_time";
    public static final String COLUMN_COMMENT = "observation_comment";
    public static final String COLUMN_FOREIGN_KEY = "hike_id";

    private int id;
    private String observation;
    private String dateOfTime;
    private String comment;
    private int hikeID;

    public Observation(){
    }

    public Observation(int id, String observation, String dateOfTime, String comment, int hikeID) {
        this.id = id;
        this.observation = observation;
        this.dateOfTime = dateOfTime;
        this.comment = comment;
        this.hikeID = hikeID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getDateOfTime() {
        return dateOfTime;
    }

    public void setDateOfTime(String dateOfTime) {
        this.dateOfTime = dateOfTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getHikeID() {
        return hikeID;
    }

    public void setHikeID(int hikeID) {
        this.hikeID = hikeID;
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_DATE + " TEXT,"
                    + COLUMN_COMMENT + " TEXT,"
                    + COLUMN_FOREIGN_KEY + " INTEGER,"
                    + " FOREIGN KEY (" + COLUMN_FOREIGN_KEY + ") REFERENCES hike(hike_id) "
                    + ")";


    @Override
    public String toString() {
        return "Hike{" +
                "id=" + id +
                ", observation='" + observation + '\'' +
                ", dateOfTime='" + dateOfTime + '\'' +
                ", comment='" + comment + '\'' +
                ", hikeID='" + hikeID + '\'' +
                '}';
    }
}
