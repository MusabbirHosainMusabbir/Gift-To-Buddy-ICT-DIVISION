package com.arena.gifttobuddy.Views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arena.gifttobuddy.Helpers.Utils;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.SharedPreference;
import com.arena.gifttobuddy.retrofit.Url;
import com.bumptech.glide.Glide;
import com.tanvir.test_library.BasicFunction;
import com.tanvir.test_library.BasicFunctionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SendRequest extends AppCompatActivity implements BasicFunctionListener {
    Button reqBtn;
    ImageView productImage;
    TextView location,time;
    EditText description;
    String locationTxt,timeTxt,descriptionTxt,user_id;
    BasicFunction bf;
    SharedPreference sharedPreference;
    String images;
    String post_id;
    ImageView back;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);
        bf = new BasicFunction(this, this);
        Utils.fullScreenView(this,false);

        sharedPreference = SharedPreference.getInstance(SendRequest.this);
        user_id = sharedPreference.getData("user_id");

        Log.e("user_id",user_id);

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            post_id = extras.getString("post_id");
            // and get whatever type user account id is
            getvalue(post_id);
        }

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendRequest.this,ItemDetails.class);
                intent.putExtra("post_id",post_id);
                startActivity(intent);
                finish();
            }
        });


        productImage = findViewById(R.id.productImage);
        location = findViewById(R.id.location);
        time = findViewById(R.id.time);
        description = findViewById(R.id.description);

        if (!Utils.hasNavBar(this)){
            Log.e("hasNav--->", "onCreate: YES---> " );
            Utils.adjustBottomNav(this, R.id.soft_key_layout, R.id.main_layout);
            //navLayout.setVisibility(View.GONE);

        }

        reqBtn = findViewById(R.id.send_req_btn);

        reqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
                //startActivity(new Intent(SendRequest.this, RequestList.class));
                //finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(SendRequest.this,ItemDetails.class);
            intent.putExtra("post_id",post_id);
            startActivity(intent);
            finish();
            // back was pressed
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void getvalue(String post_id) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("post_id",post_id);

            Log.e("jsonObject",jsonObject.toString());
            bf.getResponceData(Url.post_details,jsonObject.toString(),117,"POST");


        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
        }
    }

    private void sendRequest() {
        descriptionTxt = description.getText().toString();

        try {
            JSONObject post = new JSONObject();
            post.put("name", "");
            post.put("post_id",post_id);
            post.put("user_id",user_id);
            post.put("description",description.getText().toString());

            bf.getResponceData(Url.create_request,post.toString(),114,"POST");

            Log.e("postjson", post.toString());


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void OnServerResponce(String jsonObject, int RequestCode) {
         if(RequestCode==114){
             try {
                 JSONObject jsonobject = new JSONObject(jsonObject);
                 JSONObject response = jsonobject.getJSONObject("response");
                 int request_id = response.getInt("request_id");
                 int status = response.getInt("status");
                 String message = response.getString("message");
                 Log.e("statusoutpus", "ussr_id"+user_id+"status"+String.valueOf(status)+"message"+message);
                 if(status==0){
                     showPop();
                     Log.e("successstatus", String.valueOf(status));
                     Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                     //Intent intent = new Intent(SendRequest.this,Login.class);
                     //startActivity(intent);
                     //finish();
                 }else{
                     Log.e("erros", String.valueOf(status));
                     Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                 }
                 Log.e("userid", String.valueOf(user_id));

             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }else if(RequestCode == 117){
             try {
                 JSONObject jsonobject = new JSONObject(jsonObject);
                 JSONArray response = jsonobject.getJSONArray("response");
                 for (int i = 0; i < response.length(); i++) {
                     JSONObject object = response.getJSONObject(i);

                     JSONArray jsonArray = (JSONArray) object.get("images");
                     Log.e("jsonarray", String.valueOf(jsonArray.length()));

                     try {
                         images = object.getJSONArray("images").getString(0);
                         Glide.with(SendRequest.this)
                                 .load(images) // image url
                                 //.placeholder(itemList.get(position).getIcon()) // any placeholder to load at start
                                 //.error()
                                 .into(productImage);  // imageview object

                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
    }

    private void showPop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SendRequest.this);


        View view = getLayoutInflater().inflate(R.layout.request_sent_confirmation, null);
        TextView keepbrowsing = view.findViewById(R.id.read_btn);
        keepbrowsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendRequest.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setView(view);

        dialog = builder.create();
        dialog.requestWindowFeature(DialogFragment.STYLE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // dialog.getWindow().setLayout(100,100);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

//
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        // Set alert dialog width equal to screen width 70%
        int dialogWindowWidth = (int) (displayWidth * 0.9f);
        // Set alert dialog height equal to screen height 70%
        int dialogWindowHeight = (int) (displayHeight * 0.9f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        dialog.getWindow().setAttributes(layoutParams);

    }

    @Override
    public void OnConnetivityError() {

    }
}
