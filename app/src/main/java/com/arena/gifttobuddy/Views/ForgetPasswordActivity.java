package com.arena.gifttobuddy.Views;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.SharedPreference;
import com.arena.gifttobuddy.retrofit.Url;
import com.tanvir.test_library.BasicFunction;
import com.tanvir.test_library.BasicFunctionListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPasswordActivity extends AppCompatActivity implements BasicFunctionListener {

    EditText registeredEdt;
    TextView submit,back;
    SharedPreference sharedPreference;
    String user_id;
    BasicFunction bf;
    Intent intent;
    String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        bf = new BasicFunction(this, this);

        intent = getIntent();
        activity = intent.getStringExtra("activity");

        sharedPreference = SharedPreference.getInstance(ForgetPasswordActivity.this);
        user_id = sharedPreference.getData("user_id");

        registeredEdt = findViewById(R.id.registered_emailid);
        submit = findViewById(R.id.forgot_button);
        back = findViewById(R.id.backToLoginBtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 setPassword();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.equalsIgnoreCase("login")){
                    Intent intent = new Intent(ForgetPasswordActivity.this,Login.class);
                    startActivity(intent);
                    finish();
                }else if(activity.equalsIgnoreCase("register")){
                    Intent intent = new Intent(ForgetPasswordActivity.this,Register.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(activity.equalsIgnoreCase("login")){
            Intent intent = new Intent(ForgetPasswordActivity.this,Login.class);
            startActivity(intent);
            finish();
            return true;
        }else if(activity.equalsIgnoreCase("register")){
            Intent intent = new Intent(ForgetPasswordActivity.this,Register.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void setPassword() {
        try {
            JSONObject jsonObject = new JSONObject();
            //jsonObject.put("user_id", user_id);
            jsonObject.put("email", registeredEdt.getText().toString());

            Log.e("json",jsonObject.toString());
            bf.getResponceData(Url.forget_password,jsonObject.toString(),116,"GET");


        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
        }
    }

    @Override
    public void OnServerResponce(String jsonObject, int RequestCode) {
        if(RequestCode == 116){
            JSONObject jsonobject = null;
            try {
                jsonobject = new JSONObject(jsonObject);
                JSONObject response = jsonobject.getJSONObject("response");
                Log.e("response",response.toString());

                int status = response.getInt("status");

                if(status==1){
                      String message = response.getString("message");
                      Toast.makeText(this, ""+message, Toast.LENGTH_LONG).show();
                }else{
                    String message = response.getString("message");
                    Toast.makeText(this, ""+message, Toast.LENGTH_LONG).show();
               }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void OnConnetivityError() {

    }
}
