package com.example.penguinn.hugproject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {
    private final AppCompatActivity activity = MapsActivity.this;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "แผนที่พร้อมแล้ว", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is Ready");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

            //แสดงตำแหน่งบ้าน

            String latitude = mCursor.getString(mCursor.getColumnIndex("lat"));
            String longtitude = mCursor.getString(mCursor.getColumnIndex("long"));

            Double lat = Double.parseDouble(latitude);
            Double lng = Double.parseDouble(longtitude);
            LatLng thePosition = new LatLng(lat, lng);
            drawMarker(thePosition, "บ้าน");

            String school_latitude = mCursor.getString(mCursor.getColumnIndex("a"));
            String school_longtitude = mCursor.getString(mCursor.getColumnIndex("n"));

            Double school_lat = Double.parseDouble(school_latitude);
            Double school_lng = Double.parseDouble(school_longtitude);
            LatLng school_thePosition = new LatLng(school_lat, school_lng);
            drawMarker(school_thePosition, "โรงเรียน");
            //mMap.getUiSettings().setMyLocationButtonEnabled(false);  //แุ่มบอกตำแหน่งตัวเองหาย
        }
//
//        LatLng MELBOURNE = new LatLng(user.getLatitude(), user.getLongitude());
//        Marker melbourne = mMap.addMarker(new MarkerOptions()
//                .position(MELBOURNE)
//                .title("Melbourne")
//                .snippet("Population: 4,137,400"));

    }

    private static final String TAG = "MapsActivity";

    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private User user;
    private DatabaseHelper databaseHelper;
    private static final float ZOOM = 15f;
    private static final String[] CLUBS =
            {"Home", "School"};
    private static final String[] CLUB =
            {"บ้าน", "โรงเรียน"};

    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myTooggle;
    private SQLiteDatabase db;

    private LatLng locationStu;

    private LocationManager locationManager;
    private LocationListener listener;
    String TABLE_NAME = "user";
    String COL_PHONE = "stu_phone";
    String finalResult;
    ProgressDialog progressDialog;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    private double[] mTimerHome = new double[10];
    private double[] mTimerSchool = new double[10];
    int i = 0,j=0, count = 0,countH=0, averageHome = 0, averageSchool = 0;
    public String mStatus;
    double SDhome = 0,SDschool = 0;
    boolean twice;

    Button mbutton_strat,mbutton_stop,mbutton_school;
//    String HttpURL = "http://10.81.211.29/android/saveAddData.php";
//    String HttpURL = "http://172.27.104.27/HugProject/ConnectAndroid/UserRegistration.php";
    String HttpURL = "http://172.27.106.43/HugProject/ConnectAndroid/UserRegistrationStatus.php";

    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String st_phone;
    Cursor mCursor;
    Boolean hsStatus;
//    String dbAverageHome;
//    String dbAverageSchool;
//    Double avHome,avSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        chronometer = findViewById(R.id.chronometer);
        //เพิ่มข้อมูลลงดาต้าเบส
        databaseHelper = new DatabaseHelper(MapsActivity.this);

        user = new User();

        Bundle args = getIntent().getExtras();
        user.setPhone(args.getString("phone"));

        getLocationPermission();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mbutton_strat = (Button) findViewById(R.id.button_strat);
        mbutton_stop = (Button) findViewById(R.id.button_stop);
        mbutton_school = (Button) findViewById(R.id.button_school);

        db = databaseHelper.getWritableDatabase();

        mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
                + " WHERE " + COL_PHONE + "=" + user.getPhone(), null);
        mCursor.moveToFirst();

        st_phone = mCursor.getString(mCursor.getColumnIndex("stu_phone"));

    }
//////ทำตัวแปรบันทึกข้อมูลเข้าบ้าน เข้าโรวเรียน หาค่า +-sd
    public void homeChronometer(View v) {
        mbutton_stop.setVisibility(View.VISIBLE);
        mbutton_school.setVisibility(View.GONE);
        mbutton_strat.setVisibility(View.GONE);

        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
        hsStatus = true;

    }

    public void schoolChronometer(View v) {
        mbutton_stop.setVisibility(View.VISIBLE);
        mbutton_strat.setVisibility(View.GONE);
        mbutton_school.setVisibility(View.GONE);
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
        hsStatus =false;
    }

    public void stopChronometer(View v) {
        if (running) {
            chronometer.stop();
            mbutton_strat.setVisibility(View.VISIBLE);
            mbutton_school.setVisibility(View.VISIBLE);
            mbutton_stop.setVisibility(View.GONE);
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            if(hsStatus==true) {
                showElapsedTimeHome();
                i++;

//                Toast.makeText(MapsActivity.this, "บ้าน",
//                        Toast.LENGTH_SHORT).show();
            }else if(hsStatus==false){
                showElapsedTimeSchool();
                j++;

//                Toast.makeText(MapsActivity.this, "โรงเรียน ",
//                        Toast.LENGTH_SHORT).show();

            }
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
            running = false;
        }
    }
    private void showElapsedTimeHome() {
        String dbAverageHome = mCursor.getString(mCursor.getColumnIndex("h_status"));
        double avHome = Double.parseDouble(dbAverageHome);

        if(avHome==0){
            if (i < 10) {
                mTimerHome[i] = pauseOffset;
                Toast.makeText(MapsActivity.this, "แชร์ตำแหน่งบ้านครั้งที่ : " + pauseOffset + " " +i ,
                        Toast.LENGTH_SHORT).show();
            }
            if (i == 9) {
                for (int i = 0; i < 10; ++i) {
                    count += mTimerHome[i];
                }

                averageHome = count / 10;
                user.setH_status(averageHome);
                databaseHelper.updateHomestatus(user);

            }
        }


        if(i>=10 ||  avHome>0){
            int x = 0, y = 0;
            for (int i = 0; i < 10; ++i) {
                x += ((mTimerHome[i] - avHome) * (mTimerHome[i] - avHome));
            }
            y = x / 9;
            SDhome = Math.sqrt(y);
//            Toast.makeText(MapsActivity.this, "SD : " +SDhome + " " + avHome ,
//                    Toast.LENGTH_SHORT).show();
            if (pauseOffset >= Math.abs(SDhome-avHome)  && pauseOffset <= avHome + SDhome) {
                UserStatus("0", st_phone);
                Toast.makeText(MapsActivity.this, "ปลอดถัย: " + pauseOffset,
                        Toast.LENGTH_SHORT).show();
            } else {
                UserStatus("1", st_phone);

                Toast.makeText(MapsActivity.this, "อันตราย " + pauseOffset,
                        Toast.LENGTH_SHORT).show();

            }
        }



    }

    private void showElapsedTimeSchool() {
        String dbAverageSchool = mCursor.getString(mCursor.getColumnIndex("s_status"));
        Double avSchool = Double.parseDouble(dbAverageSchool);

        if(avSchool==0){
            if (j < 10) {
                mTimerSchool[j] = pauseOffset;
                Toast.makeText(MapsActivity.this, "แชร์ตำแหน่งบ้านครั้งที่ : " + pauseOffset + " " +j,
                        Toast.LENGTH_SHORT).show();
            }
            if (j == 9) {
                for (int i = 0; i < 10; ++i) {
                    count += mTimerSchool[i];
                }

                averageSchool = count / 10;
                user.setS_status(averageSchool);
                databaseHelper.updateSchoolstatus(user);

                Toast.makeText(MapsActivity.this, "ค่าเฉลี่ย: " + averageSchool,
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (j>=10 || avSchool>0){
//            Toast.makeText(MapsActivity.this, "เข้"+ avHome,
//                    Toast.LENGTH_SHORT).show();
            int x = 0, y = 0;
            for (int i = 0; i < 10; ++i) {
                x += ((mTimerSchool[i] - avSchool) * (mTimerSchool[i] - avSchool));
            }
            y = x / 9;
            SDschool = Math.sqrt(y);

            if (pauseOffset >= Math.abs(avSchool - SDschool) && pauseOffset <= avSchool + SDschool) {
                UserStatus("0", st_phone);
                Toast.makeText(MapsActivity.this, "ปลอดถัย: " + pauseOffset,
                        Toast.LENGTH_SHORT).show();
            } else {
                UserStatus("1", st_phone);

                Toast.makeText(MapsActivity.this, "อันตราย " + pauseOffset,
                        Toast.LENGTH_SHORT).show();

            }
        }

    }
    public static PointF calculateDerivedPosition(PointF point,
                                                  double range, double bearing)
    {
        double EarthRadius = 6371000; // m

        double latA = Math.toRadians(point.x);
        double lonA = Math.toRadians(point.y);
        double angularDistance = range / EarthRadius;
        double trueCourse = Math.toRadians(bearing);

        double lat = Math.asin(
                Math.sin(latA) * Math.cos(angularDistance) +
                        Math.cos(latA) * Math.sin(angularDistance)
                                * Math.cos(trueCourse));

        double dlon = Math.atan2(
                Math.sin(trueCourse) * Math.sin(angularDistance)
                        * Math.cos(latA),
                Math.cos(angularDistance) - Math.sin(latA) * Math.sin(lat));

        double lon = ((lonA + dlon + Math.PI) % (Math.PI * 2)) - Math.PI;

        lat = Math.toDegrees(lat);
        lon = Math.toDegrees(lon);

        PointF newPoint = new PointF((float) lat, (float) lon);

        return newPoint;

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "click");
        if (twice == true) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            finish();
            System.exit(0);
        }
        twice = true;
        Log.d(TAG, "twice: " + twice);

        Toast.makeText(MapsActivity.this, "กดอีกครั้งเพื่อออก", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
                Log.d(TAG, "twice: " + twice);
            }
        }, 300);

        twice = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maps, menu);
        return true;
    }

//

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(MapsActivity.this, DetailStudent.class);
            intent.putExtra("phone", user.getPhone());
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_calendar) {
            Intent intent = new Intent(MapsActivity.this, CalendarActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getDeviceLocation() {

        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();


                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void drawMarker(LatLng point, String mtitle) {
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        String toolTip = String.format("Your Location Lat=%s, Lon=%s", point.latitude, point.longitude);

        // Setting latitude and longitude for the marker
        markerOptions.position(point)
                .title(mtitle);

        // Adding marker on the Google Map
        mMap.addMarker(markerOptions);
    }

    private void moveCamera(final LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving to camera to: lat " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        locationStu = new LatLng(latLng.latitude, latLng.longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng)
                .title("ตำแหน่งปัจจุบัน")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        // Adding marker on the Google Map
        mMap.addMarker(markerOptions);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {

                ///save marker pu DB sqite
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("บันทึกตำแหน่ง");
                builder.setItems(CLUB, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selected = CLUBS[which];
                        if (selected == "Home") {
                            user.setLatitude(latLng.latitude);
                            user.setLongitude(latLng.longitude);

                            Log.d(TAG, "ละติจูด  " + user.getLatitude() + "ลองติจูด  " + user.getLongitude());

                            databaseHelper.updateData(user);
//                        Toast.makeText(MapsActivity.this,"บันทึกสำเร็จ",Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "บ้าน " +
                                    selected, Toast.LENGTH_SHORT).show();
                        } else if (selected == "School") {
                            user.setSchool_latitude(latLng.latitude);
                            user.setSchool_longitude(latLng.longitude);

                            databaseHelper.updateSchool(user);
                            Toast.makeText(getApplicationContext(), "รร " +
                                    selected, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("ยกเลิก", null);
                builder.create();

                builder.show();

//                AlertDialog.Builder builder =
//                        new AlertDialog.Builder(MapsActivity.this);
//                builder.setMessage("บันทึกตำแหน่ง");
//                builder.setPositiveButton("บ้าน", new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int id) {
//                        user.setLatitude(latLng.latitude);
//                        user.setLongitude(latLng.longitude);
//
//                        Log.d(TAG, "ละติจูด  "+user.getLatitude() + "ลองติจูด  " +user.getLongitude());
//
//                        databaseHelper.updateData(user);
//                        Toast.makeText(MapsActivity.this,"บันทึกสำเร็จ",Toast.LENGTH_SHORT).show();
//
//
//                    }
//                });
//                builder.setPositiveButton("โรงเรียน", new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int id) {
//                        user.setSchool_latitude(latLng.latitude);
//                        user.setSchool_longitude(latLng.longitude);
//
////                        Log.d(TAG, "ละติจูด  "+user.getLatitude() + "ลองติจูด  " +user.getLongitude());
//
//                        databaseHelper.updateSchool(user);
//                        Toast.makeText(MapsActivity.this,"บันทึกสำเร็จ",Toast.LENGTH_SHORT).show();
//
//
//                    }
//                });
//                builder.show();

                return false;

            }
        });
        //        String url = "http://www.thaicreate.com/android/saveADDData.php";
        //        double lat = latLng.latitude;
        //        double lng = latLng.longitude;
        //        String latitude_ = String.valueOf(lat);
        //        String longitude_ = String.valueOf(lng);
        //        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //
        //        params.add(new BasicNameValuePair("stu_latitude", latitude_));
        //        params.add(new BasicNameValuePair("stu_longitude",longitude_  ));

    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permission = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permission,
                        LOCATION_PERMISSION_REQUST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permission,
                    LOCATION_PERMISSION_REQUST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permisssion failed");
                            break;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission OK!");
                    mLocationPermissionGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    public void UserStatus(final String status,final String stu_phone){

        class UserStatusClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

//                progressDialog = ProgressDialog.show(MapsActivity.this,"ส่งสถานะ",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

//                progressDialog.dismiss();
//
//                Toast.makeText(MapsActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("status",params[0]);
                hashMap.put("stu_phone",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserStatusClass userStatusClass = new UserStatusClass();

        userStatusClass.execute(status,stu_phone);
    }
}
