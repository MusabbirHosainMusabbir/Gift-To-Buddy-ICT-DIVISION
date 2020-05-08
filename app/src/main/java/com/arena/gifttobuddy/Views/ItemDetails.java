package com.arena.gifttobuddy.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arena.gifttobuddy.Helpers.Utils;
import com.arena.gifttobuddy.Models.TrendingTopItems;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.retrofit.Url;
import com.bumptech.glide.Glide;
import com.tanvir.test_library.BasicFunction;
import com.tanvir.test_library.BasicFunctionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemDetails extends AppCompatActivity implements BasicFunctionListener {

    BasicFunction bf;
    ImageView image,back;
    TextView nametxt,conditiontxt,locationtxt,descriptiontxt,reasontxt,targettxt;
    Button request;
    int id;
    String images;
    String post_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bf = new BasicFunction(this, this);
        setContentView(R.layout.activity_item_details);

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
                Intent intent = new Intent(ItemDetails.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
       image = findViewById(R.id.image);
       nametxt = findViewById(R.id.name);
       conditiontxt = findViewById(R.id.condition);
       locationtxt = findViewById(R.id.location);
       descriptiontxt = findViewById(R.id.description);
       reasontxt = findViewById(R.id.reasonforgift);
       targettxt = findViewById(R.id.targetpeople);
       request = findViewById(R.id.request_btn);

       request.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(ItemDetails.this,SendRequest.class);
               intent.putExtra("post_id",post_id);
               startActivity(intent);
               finish();
           }
       });

        Utils.fullScreenView(this,false);

        if (!Utils.hasNavBar(this)){
            Log.e("hasNav--->", "onCreate: YES---> " );
            Utils.adjustBottomNav(this, R.id.soft_key_layout, R.id.main_layout);
            //navLayout.setVisibility(View.GONE);

        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ItemDetails.this,HomeActivity.class);
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
            bf.getResponceData(Url.post_details,jsonObject.toString(),116,"POST");


        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
        }
    }

    @Override
    public void OnServerResponce(String jsonObject, int RequestCode) {
          if(RequestCode == 116){
              try {
                  JSONObject jsonobject = new JSONObject(jsonObject);
                  JSONArray response = jsonobject.getJSONArray("response");
                  for (int i = 0; i < response.length(); i++) {
                      JSONObject object = response.getJSONObject(i);
                      id = object.getInt("id");

                      String name = object.getString("name");
                      int conditions = object.getInt("conditions");
                      String description = object.getString("description");
                      String time = "2 minutes";

                      //JSONArray jsonArray = (JSONArray) object.get("images");
                      String reasonforgift = object.getString("reason_for_gift");
                      String targettogift = object.getString("target_to_gift");
                      String location = object.getString("location");
                      String price = object.getString("price");

                      JSONArray jsonArray = (JSONArray) object.get("images");
                      Log.e("jsonarray", String.valueOf(jsonArray.length()));

                      try {
                          images = object.getJSONArray("images").getString(0);
                          Glide.with(ItemDetails.this)
                                  .load(images) // image url
                                  //.placeholder(itemList.get(position).getIcon()) // any placeholder to load at start
                                  //.error()
                                  .into(image);  // imageview object

                      } catch (JSONException e) {
                          e.printStackTrace();
                      }


                      nametxt.setText(name);
                      locationtxt.setText(location);
                      descriptiontxt.setText(description);
                      reasontxt.setText(reasonforgift);
                      targettxt.setText(targettogift);

                      if(conditions == 0){
                          conditiontxt.setText("Old");
                      }else{
                          conditiontxt.setText("New");
                      }

                      //String image = ""


                      Log.e("name",name);
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
