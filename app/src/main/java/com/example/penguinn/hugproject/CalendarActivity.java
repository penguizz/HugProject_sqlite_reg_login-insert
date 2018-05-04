package com.example.penguinn.hugproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity {
    private TextView urlBox;
    private Button goButt;
    private WebView webBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

//        urlBox.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                    browse();
//                    return true;
//                }
//                return false;
//            }
//        });

        goButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browse();
            }
        });
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
////        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void init(){
        goButt = (Button)findViewById(R.id.goButt);
//        urlBox = (TextView)findViewById(R.id.urlBox);
        webBrowser = (WebView)findViewById(R.id.webBrowser1);
        webBrowser.setWebViewClient(new WebViewClient());
    }

    public void browse(){
        webBrowser.getSettings().setJavaScriptEnabled(true);
        webBrowser.loadUrl("https://calendar.google.com/calendar/embed?src=i0fd0jhgp77h4of3sjgl9a8mns%40group.calendar.google.com&ctz=Asia%2FBangkok");
    }

}
