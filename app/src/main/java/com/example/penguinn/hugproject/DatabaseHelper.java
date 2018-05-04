package com.example.penguinn.hugproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.XMLFormatter;

/**
 * Created by delaroy on 3/27/17.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Student7.db";

    public static final String TABLE_USER = "user";

    private static final String COLUMN_USER_STU_FIRSTNAME = "stu_first_name";
    private static final String COLUMN_USER_STU_LASTNAME = "stu_last_name";
    private static final String COLUMN_USER_STU_PHONE = "stu_phone";
    private static final String COLUMN_USER_PASSWORD = "stu_password";

    private static final String COLUMN_USER_PARENT_FIRSTNAME = "perent_first_name";
    private static final String COLUMN_USER_PARENT_LASTNAME = "perent_last_name";
    private static final String COLUMN_USER_PARENT_PHONE = "perent_phone";
    private static final String COLUMN_USER_PARENT_EMAIL = "perent_email";

    private static final String COLUMN_USER_LAT = "lat";
    private static final String COLUMN_USER_LONG= "long";

    String COLUMN_SCHOOL_LAT = "a";
    String COLUMN_SCHOOL_LONG = "n";

    String COLUMN_USER_STATUS_HOME= "h_status";
    String COLUMN_USER_STATUS_SCHOOL= "s_status";
    User user = new User();

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;


    private String[] allColumns = {
            COLUMN_USER_PARENT_PHONE,
            COLUMN_USER_STU_FIRSTNAME,
            COLUMN_USER_STU_LASTNAME,
            COLUMN_USER_PASSWORD,
            COLUMN_USER_PARENT_FIRSTNAME,
            COLUMN_USER_PARENT_LASTNAME,
            COLUMN_USER_PARENT_PHONE,
            COLUMN_USER_PARENT_EMAIL,
            COLUMN_USER_LAT,
            COLUMN_USER_LONG,
            COLUMN_SCHOOL_LAT,
            COLUMN_SCHOOL_LONG,
            COLUMN_USER_STATUS_HOME,
            COLUMN_USER_STATUS_SCHOOL
    };

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_STU_PHONE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_STU_FIRSTNAME + " TEXT,"
            + COLUMN_USER_STU_LASTNAME + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_USER_PARENT_FIRSTNAME + " TEXT,"
            + COLUMN_USER_PARENT_LASTNAME + " TEXT,"
            + COLUMN_USER_PARENT_PHONE + " TEXT,"
            + COLUMN_USER_PARENT_EMAIL + " TEXT,"
            + COLUMN_USER_LAT + " DOUBLE,"
            + COLUMN_USER_LONG + " DOUBLE,"
            + COLUMN_SCHOOL_LAT + " DOUBLE,"
            + COLUMN_SCHOOL_LONG + " DOUBLE,"
            + COLUMN_USER_STATUS_HOME + " TEXT,"
            + COLUMN_USER_STATUS_SCHOOL + " TEXT" + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_TABLE);
//        db.execSQL("INSERT INTO " + TABLE_USER + "(" + COLUMN_USER_STU_PHONE
//                + ", " + COLUMN_USER_STU_FIRSTNAME
//                + ", " + COLUMN_USER_STU_LASTNAME
//                + ", " + COLUMN_USER_PASSWORD
//                + ", " + COLUMN_USER_PARENT_FIRSTNAME
//                + ", " + COLUMN_USER_PARENT_LASTNAME
//                + ", " + COLUMN_USER_PARENT_PHONE
//                + ", " + COLUMN_USER_PARENT_EMAIL
//                + ") VALUES ('0989307068', 'warawut', 'namhard', '1234', 'wara', 'nam', '0999999', 'wara@gmail.com');");
    }

    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    public void open(){
        database = this.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_STU_FIRSTNAME, user.getStu_fname());
        values.put(COLUMN_USER_STU_LASTNAME, user.getStu_lname());
        values.put(COLUMN_USER_STU_PHONE, user.getPhone());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        values.put(COLUMN_USER_PARENT_FIRSTNAME, user.getParent_fname());
        values.put(COLUMN_USER_PARENT_LASTNAME, user.getParent_lname());
        values.put(COLUMN_USER_PARENT_PHONE, user.getParent_phone());
        values.put(COLUMN_USER_PARENT_EMAIL, user.getParent_email());

        values.put(COLUMN_USER_LAT, user.getLatitude());
        values.put(COLUMN_USER_LONG, user.getLongitude());

        values.put(COLUMN_SCHOOL_LAT, user.getSchool_latitude());
        values.put(COLUMN_SCHOOL_LONG, user.getSchool_longitude());

        values.put(COLUMN_USER_STATUS_HOME, user.getH_status());
        values.put(COLUMN_USER_STATUS_SCHOOL, user.getS_status());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_STU_FIRSTNAME, user.getStu_fname());
        values.put(COLUMN_USER_STU_LASTNAME, user.getStu_lname());
        values.put(COLUMN_USER_STU_PHONE, user.getPhone());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        values.put(COLUMN_USER_PARENT_FIRSTNAME, user.getParent_fname());
        values.put(COLUMN_USER_PARENT_LASTNAME, user.getParent_lname());
        values.put(COLUMN_USER_PARENT_PHONE, user.getParent_phone());
        values.put(COLUMN_USER_PARENT_EMAIL, user.getParent_email());

        //   values.put(COLUMN_USER_LATITUDE, user.getLatitude());
        //    values.put(COLUMN_USER_LONGITUDE, user.getLongitude());

        db.update(TABLE_USER, values, COLUMN_USER_STU_PHONE + "=" + user.getPhone(),null);
    }


    public void updateData(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_LAT, user.getLatitude());
        values.put(COLUMN_USER_LONG, user.getLongitude());

        db.update(TABLE_USER, values, COLUMN_USER_STU_PHONE + "=" + " ' " + user.getPhone() +" ' ",null);
        db.close();

    }

    public void updateSchool(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put(COLUMN_USER_STU_FIRSTNAME, user.getStu_fname());
//        values.put(COLUMN_USER_STU_LASTNAME, user.getStu_lname());
//        values.put(COLUMN_USER_STU_PHONE, user.getPhone());
//        values.put(COLUMN_USER_PASSWORD, user.getPassword());
//
//        values.put(COLUMN_USER_PARENT_FIRSTNAME, user.getParent_fname());
//        values.put(COLUMN_USER_PARENT_LASTNAME, user.getParent_lname());
//        values.put(COLUMN_USER_PARENT_PHONE, user.getParent_phone());
//        values.put(COLUMN_USER_PARENT_EMAIL, user.getParent_email());

        values.put(COLUMN_SCHOOL_LAT, user.getSchool_latitude());
        values.put(COLUMN_SCHOOL_LONG, user.getSchool_longitude());
        db.update(TABLE_USER, values, COLUMN_USER_STU_PHONE + "=" + " ' " + user.getPhone() +" ' ",null);

        db.close();

    }

    public void updateHomestatus(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_STATUS_HOME, user.getH_status());

        db.update(TABLE_USER, values, COLUMN_USER_STU_PHONE + "=" + " ' " + user.getPhone() +" ' ",null);
        db.close();

    }

    public void updateSchoolstatus(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_STATUS_SCHOOL, user.getS_status());

        db.update(TABLE_USER, values, COLUMN_USER_STU_PHONE + "=" + " ' " + user.getPhone() +" ' ",null);
        db.close();

    }

    public boolean checkUser(String email){
        String[] columns = {
                COLUMN_USER_STU_PHONE
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_STU_PHONE + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    public boolean checkUser(String email, String password){
        String[] columns = {
                COLUMN_USER_STU_PHONE
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_STU_PHONE + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_USER
                + " WHERE " + COLUMN_USER_STU_PHONE + "=" + user.getPhone(),null);
//        Cursor res = db.rawQuery("select * from "+  TABLE_USER,null);

        mCursor.moveToFirst();

        return mCursor;
    }

    public List<User> getMyMarkers(){
        SQLiteDatabase db = this.getWritableDatabase();

        List<User> markers = new ArrayList<User>();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER
//                + " WHERE " + COLUMN_USER_STU_PHONE + "=" + user.getPhone(),null);
//
//        cursor.moveToFirst();
        Cursor cursor = db.query(TABLE_USER,allColumns,null,null,null,null,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            User comment = cursorToUser(cursor);
            markers.add(comment);
            cursor.moveToNext();
        }
        cursor.close();
        return markers;
    }

    private User cursorToUser(Cursor cursor){
        User user = new User();
        user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_STU_PHONE)));
        user.setStu_fname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_STU_FIRSTNAME)));
        user.setStu_lname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_STU_LASTNAME)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
        user.setParent_fname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PARENT_FIRSTNAME)));
        user.setParent_lname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PARENT_LASTNAME)));
        user.setParent_phone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PARENT_PHONE)));
        user.setParent_email(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PARENT_EMAIL)));

        return user;
    }
}