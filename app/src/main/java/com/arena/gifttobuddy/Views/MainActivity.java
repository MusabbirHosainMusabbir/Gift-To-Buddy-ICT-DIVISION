package com.arena.gifttobuddy.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.arena.gifttobuddy.Helpers.Utils;
import com.arena.gifttobuddy.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.forceFullScreen(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if (restorePrefData()){
                    startActivity(newbg Intent(MainActivity.this,Login.class));
                    finish();
                }*/
                startActivity(new Intent(MainActivity.this,IntroActivity.class));
                finish();
            }
        },5000);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus || !hasFocus) {
            Utils.forceFullScreen(this);
        }
    }

    private boolean restorePrefData() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;



    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();


    }
}
