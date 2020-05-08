package com.arena.gifttobuddy.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arena.gifttobuddy.Adapters.AdapterProfileUser;
import com.arena.gifttobuddy.Helpers.Utils;
import com.arena.gifttobuddy.Models.Category;
import com.arena.gifttobuddy.Models.Items;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.retrofit.Url;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tanvir.test_library.BasicFunction;
import com.tanvir.test_library.BasicFunctionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Categories extends AppCompatActivity implements BasicFunctionListener {
    ImageView postBtn;
    RecyclerView recyclerView;
    BasicFunction bf;
    JSONArray textarray;
    ArrayList<Items> list;
    AdapterProfileUser adapterCategory;
    ImageView back;
    BottomNavigationView bottomNavigationView;
    ImageView fabmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bf = new BasicFunction(this, this);
        setContentView(R.layout.activity_categories);
        postBtn = findViewById(R.id.post_img);
        fabmap = findViewById(R.id.post_img);

        fabmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Categories.this,PublishPost.class);
                startActivity(intent);
                finish();
            }
        });

        back = findViewById(R.id.back);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
                    Intent intent = new Intent(Categories.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (item.getItemId() == R.id.post) {
                    // on favorites clicked
                    Intent intent = new Intent(Categories.this,PublishPost.class);
                    startActivity(intent);
                    finish();
                    return true;
                }else if(item.getItemId() == R.id.notifications){
                    Intent intent = new Intent(Categories.this,RequestList.class);
                    startActivity(intent);
                    finish();
                    return true;
                }else if(item.getItemId() == R.id.profile){
                    Intent intent = new Intent(Categories.this,Profile_User.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Categories.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getValue();

        list = new ArrayList<>();
        Utils.fullScreenView(this,false);

        if (!Utils.hasNavBar(this)){
            Log.e("hasNav--->", "onCreate: YES---> " );
            Utils.adjustBottomNav(this, R.id.soft_key_layout, R.id.main_layout);
            //navLayout.setVisibility(View.GONE);

        }

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Categories.this,PublishPost.class));
                finish();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Categories.this,HomeActivity.class);
            startActivity(intent);
            finish();
            // back was pressed
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void getValue() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value","0");

            bf.getResponceData(Url.category,jsonObject.toString(),116,"POST");

        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
        }
    }

    @Override
    public void OnServerResponce(String jsonObject, int RequestCode) {
        JSONObject jsonobject = null;
        list = new ArrayList<Items>();
        try {
            jsonobject = new JSONObject(jsonObject);
            textarray = jsonobject.getJSONArray("response");
            Log.e("response", String.valueOf(textarray.length()));
            for (int i = 0; i < textarray.length(); i++) {
                JSONObject c = textarray.getJSONObject(i);
                String id = c.getString("id");
                String name = c.getString("name");
                String image = c.getString("image");

                list.add(new Items(image,name));

                Log.e("name",name);
                //list.add()

            }

            LinearLayoutManager layoutManagerPayment = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView = findViewById(R.id.recycleView);
            recyclerView.setLayoutManager(layoutManagerPayment);
            adapterCategory = new AdapterProfileUser(this, list);
            recyclerView.setAdapter(adapterCategory);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void OnConnetivityError() {

    }
}
