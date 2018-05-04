package com.example.penguinn.hugproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowdataActivity extends AppCompatActivity {
    //    TextView profile,stu_name,stu_lastname,stu_phone,parent_name,parent_lastname,parent_phone,parent_email;
    private EditText edit_stu_name, edit_stu_lastname, edit_stu_pass, edit_parent_name,
            edit_parent_lastname, edit_parent_phone, edit_parent_email;
    private TextView edit_stu_phone;
    private Button btn_edit, btn_save;
    private DatabaseHelper databaseHelper;
    private User user;
    public String sFirstName,sLastName,sPhone,sPassword,pFirstName,pLastName,pPhone,pEmail;
    String finalResult ;
    String HttpURL = "http://172.27.106.43/HugProject/ConnectAndroid/UserUpdate.php";
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdata);

        databaseHelper = new DatabaseHelper(ShowdataActivity.this);
        user = new User();
        SQLiteDatabase db;
        db = databaseHelper.getWritableDatabase();

        Bundle args = getIntent().getExtras();
        user.setPhone(args.getString("phone"));

        edit_stu_name = (EditText) findViewById(R.id.edit_stu_name);
        edit_stu_lastname = (EditText) findViewById(R.id.edit_stu_lastname);
        edit_stu_phone = (TextView) findViewById(R.id.edit_stu_phone);
        edit_stu_pass = (EditText) findViewById(R.id.edit_stu_pass);
        edit_parent_name = (EditText) findViewById(R.id.edit_parent_name);
        edit_parent_lastname = (EditText) findViewById(R.id.edit_parent_lastname);
        edit_parent_phone = (EditText) findViewById(R.id.edit_parent_phone);
        edit_parent_email = (EditText) findViewById(R.id.edit_parent_email);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_save = (Button) findViewById(R.id.btn_save);

        String TABLE_NAME = "user";
        String COL_PHONE = "stu_phone";

        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
                + " WHERE " + COL_PHONE + "=" + user.getPhone(), null);

        Log.i("Data Count", String.valueOf(mCursor.getCount()));

        mCursor.moveToFirst();

        String st_name = mCursor.getString(mCursor.getColumnIndex("stu_first_name"));
        String st_lastname = mCursor.getString(mCursor.getColumnIndex("stu_last_name"));
        String st_phone = mCursor.getString(mCursor.getColumnIndex("stu_phone"));
        String st_Password = mCursor.getString(mCursor.getColumnIndex("stu_password"));

        String pa_name = mCursor.getString(mCursor.getColumnIndex("perent_first_name"));
        String pa_lastname = mCursor.getString(mCursor.getColumnIndex("perent_last_name"));
        String pa_phone = mCursor.getString(mCursor.getColumnIndex("perent_phone"));
        String pa_email = mCursor.getString(mCursor.getColumnIndex("perent_email"));

//        sFirstName = mCursor.getString(mCursor.getColumnIndex("stu_first_name"));
//        sLastName = mCursor.getString(mCursor.getColumnIndex("stu_last_name"));
//        sPhone = mCursor.getString(mCursor.getColumnIndex("stu_phone"));
//        sPassword = mCursor.getString(mCursor.getColumnIndex("stu_password"));
//        pFirstName = mCursor.getString(mCursor.getColumnIndex("perent_first_name"));
//        pLastName = mCursor.getString(mCursor.getColumnIndex("perent_last_name"));
//        pPhone = mCursor.getString(mCursor.getColumnIndex("perent_phone"));
//        pEmail = mCursor.getString(mCursor.getColumnIndex("perent_email"));

        edit_stu_name.setText("" + st_name);
        edit_stu_lastname.setText("" + st_lastname);
        edit_stu_phone.setText("0" + st_phone);
        edit_stu_pass.setText("" + st_Password);
        edit_parent_name.setText("" + pa_name);
        edit_parent_lastname.setText("" + pa_lastname);
        edit_parent_phone.setText("" + pa_phone);
        edit_parent_email.setText("" + pa_email);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSqlite();
                Intent intent = new Intent(ShowdataActivity.this, DetailStudent.class);
                intent.putExtra("phone", user.getPhone());
                startActivity(intent);

            }
        });
    }


            public void updateSqlite(){
                sFirstName = edit_stu_name.getText().toString();
                sLastName =  edit_stu_lastname.getText().toString();
                sPassword = edit_stu_pass.getText().toString();
                sPhone  = user.getPhone();
                pFirstName = edit_parent_name.getText().toString();
                pLastName = edit_parent_lastname.getText().toString();
                pPhone = edit_parent_phone.getText().toString();
                pEmail = edit_parent_email.getText().toString();

                user.setStu_fname(edit_stu_name.getText().toString().trim());
                user.setStu_lname(edit_stu_lastname.getText().toString().trim());
                user.getPhone();
                user.setPassword(edit_stu_pass.getText().toString().trim());
                user.setParent_fname(edit_parent_name.getText().toString().trim());
                user.setParent_lname(edit_parent_lastname.getText().toString().trim());
                user.setParent_phone(edit_parent_phone.getText().toString().trim());
                user.setParent_email(edit_parent_email.getText().toString().trim());

                databaseHelper.updateUser(user);
                UserRegisterFunction(sPhone,sFirstName,sLastName,sPassword,pFirstName,pLastName,pPhone,pEmail);

            }



    public void UserRegisterFunction(final String stu_phone, final String stu_last_name, final String stu_first_name, final String stu_password,
                                     final String parent_first_name , final String parent_last_name, final String parent_phone, final String parent_email){

        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


//                progressDialog = ProgressDialog.show(ShowdataActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

//                progressDialog.dismiss();
//
//                Toast.makeText(ShowdataActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("stu_phone",params[0]);

                hashMap.put("stu_first_name",params[1]);

                hashMap.put("stu_last_name",params[2]);

                hashMap.put("stu_password",params[3]);

                hashMap.put("parent_first_name",params[4]);

                hashMap.put("parent_last_name",params[5]);

                hashMap.put("parent_phone",params[6]);

                hashMap.put("parent_email",params[7]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();
        userRegisterFunctionClass.execute(sPhone, sFirstName, sLastName, sPassword, pFirstName, pLastName, pPhone, pEmail);
    }


}

