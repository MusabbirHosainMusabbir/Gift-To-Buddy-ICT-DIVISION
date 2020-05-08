package com.arena.gifttobuddy.Views;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.SharedPreference;
import com.arena.gifttobuddy.preference.PrefManager;

public class SplashActviity extends AppCompatActivity {

    PrefManager prefManager;
    SharedPreference sharedPreference;
    String loggedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_actviity);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreference = SharedPreference.getInstance(SplashActviity.this);
        //prefManager = new PrefManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                loggedin = sharedPreference.getDataLogin("loggedin");
                if(!loggedin.equalsIgnoreCase("0")){
                    Intent intent = new Intent(SplashActviity.this,Login.class);
                    startActivity(intent);
                    finish();
                }else{
                    startActivity(new Intent(SplashActviity.this,IntroActivity.class));
                    finish();
                }

//                if (!prefManager.isFirstTimeLaunch()){
//                    prefManager.setFirstTimeLaunch(false);
//                    //btnNext.setVisibility(View.INVISIBLE);
//
//                    Intent intent = new Intent(SplashActviity.this,Login.class);
//                    startActivity(intent);
//                    finish();
//                }
//                else {
//                    startActivity(new Intent(SplashActviity.this,IntroActivity.class));
//                    finish();
//                }



            }
        },4000);
    }
}
