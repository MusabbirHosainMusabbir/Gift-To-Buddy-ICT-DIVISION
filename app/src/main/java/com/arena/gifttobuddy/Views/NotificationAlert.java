package com.arena.gifttobuddy.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.SharedPreference;

public class NotificationAlert extends AppCompatActivity {

    String loggedin;
    SharedPreference sharedPreference;
    TextView allowtxt,denytxt;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreference = SharedPreference.getInstance(NotificationAlert.this);
        loggedin = sharedPreference.getNotification("notificationalert");

        Log.e("loggin",loggedin);

        if(!loggedin.equalsIgnoreCase("0")){
            Intent intent = new Intent(NotificationAlert.this,Login.class);
            startActivity(intent);
            finish();
        }

//        else{
//            Intent intent = new Intent(NotificationAlert.this,Login.class);
//            startActivity(intent);
//            finish();
//        }
        setContentView(R.layout.activity_notification_alert);

        allowtxt = findViewById(R.id.allow);
        denytxt = findViewById(R.id.deny);

        allowtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreference.saveNotification("notificationalert", "1");
               intent = new Intent(NotificationAlert.this,Login.class);
               startActivity(intent);
               finish();
            }
        });

        denytxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreference.saveNotification("notificationalert", "0");
                intent = new Intent(NotificationAlert.this,Login.class);
                startActivity(intent);
                finish();
            }
        });




    }
}
