package com.blinky.peestash.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

public class SlideActivity extends FragmentActivity {

    private ViewPager _mViewPager;
    private ViewPagerAdapter _adapter;
    private Button _btn1,_btn2, _btn3, _btn4;
    Context appContext;
    SessionManager session;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.slide);
        setUpView();
        setTab();
/*
        SharedPreferences prefs = getSharedPreferences("CONNEXION",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        boolean isFirst = session.isLoggedIn();
        if(isFirst) {
            Intent intent = new Intent(this, MainActivity.class);
            editor.putBoolean(KEY_IS_FIRST_TIME, false);
            editor.commit();
            startActivity(intent);
        }*/
        final Button btn = (Button) findViewById(R.id.btn);



        // Call isNetworkAvailable class

            final View.OnClickListener listnr = new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (!isNetworkAvailable()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                SlideActivity.this);
                        builder.setMessage("Connexion internet impossible ! :( ").setCancelable(false).setPositiveButton(
                                "Ressayer", new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {
                                        btn.setVisibility(View.GONE);
                                        // Restart the activity
                                        Intent intent = new Intent(
                                                SlideActivity.this,
                                                SlideActivity.class);
                                        finish();
                                        startActivity(intent);

                                    }

                                }

                        );
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {

                            new Thread(new Runnable() {
                                public void run() {

                                    Intent i = new Intent(SlideActivity.this, LoginActivity.class);
                                    startActivity(i);

                                }
                            }).start();

                        }
                    }

            };
            btn.setOnClickListener(listnr);



    }

    // Private class isNetworkAvailable

    private boolean isNetworkAvailable() {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    private void setUpView(){
        _mViewPager = (ViewPager) findViewById(R.id.viewPager);
        _adapter = new ViewPagerAdapter(getApplicationContext(),getSupportFragmentManager());
        _mViewPager.setAdapter(_adapter);
        _mViewPager.setCurrentItem(0);
        initButton();
    }
    private void setTab(){
        _mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrollStateChanged(int position) {}
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                btnAction(position);
            }

        });

    }
    private void btnAction(int action){

        switch(action){
            case 0: setButton(_btn1, "", 40, 40); setButton(_btn2, "", 20, 20); setButton(_btn3, "", 20, 20);
                _btn1.setBackgroundColor(Color.rgb(232, 126, 0));
                _btn2.setBackgroundColor(Color.rgb(255,255,255));
                _btn3.setBackgroundColor(Color.rgb(255, 255, 255));
                _btn4.setBackgroundColor(Color.rgb(255, 255, 255));
                _btn1.setTextColor(Color.rgb(255, 255, 255));

                break;

            case 1: setButton(_btn2,"",40,40); setButton(_btn1,"",20,20); setButton(_btn3,"",20,20);
                _btn2.setBackgroundColor(Color.rgb(232, 126, 0));
                _btn1.setBackgroundColor(Color.rgb(255,255,255));
                _btn3.setBackgroundColor(Color.rgb(255,255,255));
                _btn4.setBackgroundColor(Color.rgb(255, 255, 255));
                _btn2.setTextColor(Color.rgb(255, 255, 255));
                break;

            case 2: setButton(_btn3,"",40,40); setButton(_btn2,"",20,20); setButton(_btn1,"",20,20);
                _btn3.setBackgroundColor(Color.rgb(232, 126, 0));
                _btn1.setBackgroundColor(Color.rgb(255,255,255));
                _btn2.setBackgroundColor(Color.rgb(255,255,255));
                _btn4.setBackgroundColor(Color.rgb(255, 255, 255));
                _btn3.setTextColor(Color.rgb(255, 255, 255));
                break;
            case 3: setButton(_btn4,"",40,40); setButton(_btn2,"",20,20); setButton(_btn1,"",20,20);setButton(_btn3,"",20,20);
                _btn4.setBackgroundColor(Color.rgb(232, 126, 0));
                _btn1.setBackgroundColor(Color.rgb(255,255,255));
                _btn2.setBackgroundColor(Color.rgb(255,255,255));
                _btn3.setBackgroundColor(Color.rgb(255,255,255));
                _btn4.setTextColor(Color.rgb(255, 255, 255));
                break;
        }
    }
    private void initButton(){
        _btn1=(Button)findViewById(R.id.btn1);
        _btn2=(Button)findViewById(R.id.btn2);
        _btn3 = (Button) findViewById(R.id.btn3);
        _btn4 = (Button) findViewById(R.id.btn4);
        setButton(_btn1, "", 40, 40);
        _btn1.setBackgroundColor(Color.rgb(232, 126, 0));
        _btn1.setTextColor(Color.rgb(255, 255, 255));

        setButton(_btn2, "", 40, 40);
        setButton(_btn3, "", 40, 40);
        setButton(_btn4, "", 40, 40);
    }
    private void setButton(Button btn,String text,int h, int w){
        btn.setWidth(w);
        btn.setHeight(h);
        btn.setText(text);
    }

}