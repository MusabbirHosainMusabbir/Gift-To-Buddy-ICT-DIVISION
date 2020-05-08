package com.arena.gifttobuddy.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.arena.gifttobuddy.Adapters.IntroViewPagerAdapter;
import com.arena.gifttobuddy.Models.ScreenItem;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.SharedPreference;
import com.arena.gifttobuddy.preference.PrefManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    Button btnGetStarted;
    Animation btnAnim ;
    TextView register;

    TextView title;
    TextView description;
    private PrefManager prefManager;
    String loggedin;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the activity on full screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sharedPreference = SharedPreference.getInstance(IntroActivity.this);
        loggedin = sharedPreference.getDataLogin("loggedin");
        Log.e("loggedin",loggedin);
        if(!loggedin.equalsIgnoreCase("0")){
            Intent intent = new Intent(IntroActivity.this,Login.class);
            startActivity(intent);
            finish();
        }

       // load when intro slider reaches at last positition
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            loaddLastScreen();
            finish();
        }



        setContentView(R.layout.activity_intro);

        // hide the action bar

       // getSupportActionBar().hide();

        // ini views
        btnNext = findViewById(R.id.btn_next);
        //btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        register = findViewById(R.id.register_tv);

        title = findViewById(R.id.intro_title);
        description = findViewById(R.id.intro_description);

        // fill list screen

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Fresh Food","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",R.drawable.gift_to));
        mList.add(new ScreenItem("Fast Delivery","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",R.drawable.gift_from));
        mList.add(new ScreenItem("Easy Payment","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",R.drawable.gift_req));

        // setup viewpager
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tablayout with viewpager

        tabIndicator.setupWithViewPager(screenPager);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this,Register.class));
                finish();
            }
        });

        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("position------>", "onPageScrolled: "+position );
                title.setText(mList.get(position).getTitle());
                description.setText(mList.get(position).getDescription());
            }

            @Override
            public void onPageSelected(int position) {

                if (position < mList.size()-1 && btnNext.getVisibility()==View.INVISIBLE){
                    btnNext.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // next button click Listner

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()) {

                    position++;
                    screenPager.setCurrentItem(position);
                    title.setText(mList.get(position).getTitle());
                    description.setText(mList.get(position).getDescription());


                }

                if (position == mList.size()-1) { // when we rech to the last screen

                    // TODO : show the GETSTARTED Button and hide the indicator and the next button

                    loaddLastScreen();


                }



            }
        });

        // tablayout add change listener


        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1) {

                    loaddLastScreen();

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        // Get Started button click listener

        /*btnGetStarted.setOnClickListener(newbg View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //open main activity

                Intent mainActivity = newbg Intent(getApplicationContext(),Login.class);
                startActivity(mainActivity);
                // also we need to save a boolean value to storage so next time when the user run the app
                // we could know that he is already checked the intro screen activity
                // i'm going to use shared preferences to that process
                savePrefsData();
                finish();



            }
        });*/

        // skip button click listener

        /*tvSkip.setOnClickListener(newbg View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });
*/


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

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {

        prefManager.setFirstTimeLaunch(false);

        //btnNext.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(IntroActivity.this,NotificationAlert.class);
        startActivity(intent);
        finish();
        //btnGetStarted.setVisibility(View.VISIBLE);
        //tvSkip.setVisibility(View.INVISIBLE);
        //tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        //btnGetStarted.setAnimation(btnAnim);



    }
}
