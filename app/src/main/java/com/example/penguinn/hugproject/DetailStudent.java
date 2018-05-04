package com.example.penguinn.hugproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class DetailStudent extends AppCompatActivity {

    private TextView txt_st_name,txt_st_lastname,txt_st_phone,txt_pa_name,txt_pa_lastname,txt_pa_phone,txt_pa_email, txt_latitude,txt_longitude;
    private DatabaseHelper databaseHelper;
    private User user;
    private Button btn_edit,btn_back;
//    private WebView mWebView;
    public String sFirstName,sLastName,sPhone,sPassword,pFirstName,pLastName,pPhone,pEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_student);

        databaseHelper = new DatabaseHelper(DetailStudent.this);
        SQLiteDatabase db;
        db = databaseHelper.getWritableDatabase();
        user = new User();

        Bundle args = getIntent().getExtras();
        user.setPhone(args.getString("phone"));

        txt_st_name = (TextView) findViewById(R.id.txt_st_name);
        txt_st_lastname = (TextView) findViewById(R.id.txt_st_lastname);
        txt_st_phone = (TextView) findViewById(R.id.txt_st_phone);
        txt_pa_name = (TextView) findViewById(R.id.txt_pa_name);
        txt_pa_lastname = (TextView) findViewById(R.id.txt_pa_lastname);
        txt_pa_phone = (TextView) findViewById(R.id.txt_pa_phone);
        txt_pa_email = (TextView) findViewById(R.id.txt_pa_email);
//        txt_latitude = (TextView) findViewById(R.id.txt_latitude);
//        txt_longitude = (TextView) findViewById(R.id.txt_longitude);
        btn_edit = (Button)findViewById(R.id.btn_edit);
        btn_back = (Button)findViewById(R.id.btn_back);
//        mWebView = (WebView) findViewById(R.id.web_view);

        String TABLE_NAME = "user";
        String COL_PHONE = "stu_phone";


        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
                + " WHERE " + COL_PHONE + "=" + user.getPhone(),null);

        mCursor.moveToFirst();

//        String st_name = mCursor.getString(mCursor.getColumnIndex("stu_first_name"));
//        String st_lastname = mCursor.getString(mCursor.getColumnIndex("stu_last_name"));
//        String st_phone = mCursor.getString(mCursor.getColumnIndex("stu_phone"));
//        String pa_name = mCursor.getString(mCursor.getColumnIndex("perent_first_name"));
//        String pa_lastname = mCursor.getString(mCursor.getColumnIndex("perent_last_name"));
//        String pa_phone = mCursor.getString(mCursor.getColumnIndex("perent_phone"));
//        String pa_email = mCursor.getString(mCursor.getColumnIndex("perent_email"));

        sFirstName = mCursor.getString(mCursor.getColumnIndex("stu_first_name"));
        sLastName = mCursor.getString(mCursor.getColumnIndex("stu_last_name"));
        sPhone = mCursor.getString(mCursor.getColumnIndex("stu_phone"));
        sPassword = mCursor.getString(mCursor.getColumnIndex("stu_password"));
        pFirstName = mCursor.getString(mCursor.getColumnIndex("perent_first_name"));
        pLastName = mCursor.getString(mCursor.getColumnIndex("perent_last_name"));
        pPhone = mCursor.getString(mCursor.getColumnIndex("perent_phone"));
        pEmail = mCursor.getString(mCursor.getColumnIndex("perent_email"));
//        String latitude = mCursor.getString(mCursor.getColumnIndex("lat"));
//        String longtitude = mCursor.getString(mCursor.getColumnIndex("long"));

        txt_st_name.setText("" + sFirstName);
        txt_st_lastname.setText("" + sLastName);
        txt_st_phone.setText("0" + sPhone);
        txt_pa_name.setText("" + pFirstName);
        txt_pa_lastname.setText("" + pLastName);
        txt_pa_phone.setText("" + pPhone);
        txt_pa_email.setText("" + pEmail);
//        txt_latitude.setText("" + latitude);
//        txt_longitude.setText("" + longtitude);
        //    Log.i("GetData", data);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DetailStudent.this, ShowdataActivity.class);
                intent.putExtra("phone", user.getPhone());
                startActivity(intent);

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DetailStudent.this, MapsActivity.class);
                intent.putExtra("phone", user.getPhone());
                startActivity(intent);
            }
        });

    }

}