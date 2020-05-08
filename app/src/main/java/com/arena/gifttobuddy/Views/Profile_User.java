package com.arena.gifttobuddy.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arena.gifttobuddy.Adapters.AdapterAbout;
import com.arena.gifttobuddy.Adapters.AdapterGetRequest;
import com.arena.gifttobuddy.Adapters.AdapterMyRequest;
import com.arena.gifttobuddy.Adapters.AdapterProfileUser;
import com.arena.gifttobuddy.Models.About;
import com.arena.gifttobuddy.Models.GetRequests;
import com.arena.gifttobuddy.Models.Items;
import com.arena.gifttobuddy.Models.MyRequest;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.CircleImageView;
import com.arena.gifttobuddy.Utils.RecyclerTouchListener;
import com.arena.gifttobuddy.Utils.SharedPreference;
import com.arena.gifttobuddy.customfonts.MyTextView_SF_Pro_Display_Bold;
import com.arena.gifttobuddy.customfonts.MyTextView_SF_Pro_Display_Regular;
import com.arena.gifttobuddy.retrofit.Url;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;
import com.tanvir.test_library.BasicFunction;
import com.tanvir.test_library.BasicFunctionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile_User extends AppCompatActivity implements BasicFunctionListener {

    AdapterAbout adapterProfileUser;
    ArrayList<About> list;
    CircleImageView imageview;
    SharedPreference sharedPreference;
    String user_id;
    MyTextView_SF_Pro_Display_Regular nametxt,locationtxt;
    MyTextView_SF_Pro_Display_Bold requestprovide,requestrecive;
    JSONArray textarray,getRequestArray,jsonlist;
    ImageView back;
    private List<MyRequest> myRequest = new ArrayList<>();
    private List<GetRequests> getRequest = new ArrayList<>();
    BasicFunction bf;
    BottomNavigationView bottomNavigationView;
    ImageView fabmap;
    Button signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bf = new BasicFunction(this, this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile__user);

        sharedPreference = SharedPreference.getInstance(Profile_User.this);
        user_id = sharedPreference.getData("user_id");

        imageview = findViewById(R.id.imageview_account_profile);

        requestprovide = findViewById(R.id.numberprovide);
        requestrecive = findViewById(R.id.numberreceive);

        signout = findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreference.saveDataLogin("loggedin", "0");
                Intent intent = new Intent(Profile_User.this,Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        nametxt = findViewById(R.id.name);
        locationtxt = findViewById(R.id.location);
        fabmap = findViewById(R.id.fab_map);

        fabmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_User.this,PublishPost.class);
                startActivity(intent);
                finish();
            }
        });

        myRequest(user_id);
        getRequest();

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_User.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
                    Intent intent = new Intent(Profile_User.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (item.getItemId() == R.id.post) {
                    // on favorites clicked
                    Intent intent = new Intent(Profile_User.this,Categories.class);
                    startActivity(intent);
                    finish();
                    return true;
                }else if(item.getItemId() == R.id.notifications){
                    Intent intent = new Intent(Profile_User.this,RequestList.class);
                    startActivity(intent);
                    finish();
                    return true;
                }else if(item.getItemId() == R.id.profile){
                    Intent intent = new Intent(Profile_User.this,Profile_User.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });


        list = new ArrayList<About>();
        list.add(new About(R.drawable.profileimg,"Profile"));
        list.add(new About(R.drawable.faq,"About Me"));
        list.add(new About(R.drawable.msg,"Contact Me"));
        list.add(new About(R.drawable.privacy_policy,"Privacy Notice"));
        list.add(new About(R.drawable.legalinfo,"Legal Information"));
        list.add(new About(R.drawable.shareapp,"Share the app"));

        getUser();

        LinearLayoutManager layoutManagerPayment = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerViewPaymentMethods = findViewById(R.id.recycleView);
        recyclerViewPaymentMethods.setLayoutManager(layoutManagerPayment);
        adapterProfileUser = new AdapterAbout(Profile_User.this, list);
        recyclerViewPaymentMethods.setAdapter(adapterProfileUser);

        recyclerViewPaymentMethods.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerViewPaymentMethods, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                 if(position == 0){
                     Intent intent = new Intent(Profile_User.this,Profile_Edit.class);
                     startActivity(intent);
                     finish();
                 }else if(position == 1){
                     Intent intent = new Intent(Profile_User.this,GiftReceiver.class);
                     startActivity(intent);
                     finish();
                 }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getRequest() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id",user_id);

            Log.e("jsonObject",jsonObject.toString());
            bf.getResponceData(Url.post_list,jsonObject.toString(),113,"POST");


        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
        }
    }

    private void myRequest(String user_id) {
//        final ProgressDialog progress = new ProgressDialog(this);
//        progress.setTitle("Loading");
//        progress.setMessage("Wait while loading...");
//        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
//        progress.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            String URL = Url.request_list;
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("user_id", user_id);


            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonobjects) {

                    try {

                        Log.e("response", String.valueOf(jsonobjects));
                        JSONObject jsonobject = new JSONObject(String.valueOf(jsonobjects));
                        JSONArray response = jsonobject.getJSONArray("response");

                        if(response.length() == 0){
                            //progress.dismiss();
                        }


                        for (int i = 0; i < response.length(); i++) {
                            JSONObject object = response.getJSONObject(i);
                            int id = object.getInt("id");
                            Log.e("id", String.valueOf(id));
                            String name = object.getString("name");

                            int user_id = object.getInt("user_id");
                            String description = object.getString("description");
                            int status = object.getInt("status");
                            int post_id = object.getInt("post_id");
                            String time = "2 minutes";

                            Log.e("namevaluse",name);

                            //progress.dismiss();
                            passvalue(name,time,post_id,status);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    onBackPressed();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    //headers.put("Authorization", "Basic " + "c2FnYXJAa2FydHBheS5jb206cnMwM2UxQUp5RnQzNkQ5NDBxbjNmUDgzNVE3STAyNzI=");//put your token here
                    return headers;
                }
            };
            requestQueue.add(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void passvalue(final String name, final String time, int post_id, final int status) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            String URL = Url.post_details;
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("post_id", post_id);


            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonobjects) {

                    try {
                        JSONObject jsonobject = new JSONObject(String.valueOf(jsonobjects));
                        JSONArray response = jsonobject.getJSONArray("response");
                        String images = null;
                        String location = null;
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject object = response.getJSONObject(i);
                            location = object.getString("location");
                            JSONArray jsonArray = (JSONArray) object.get("images");
                            Log.e("jsonarray", String.valueOf(jsonArray.length()));
                            try {
                                images = object.getJSONArray("images").getString(0);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        progress.dismiss();
                        //Log.e("namevaluescrond",name+"location"+location+"image"+images);
                        myRequest.add(new MyRequest(images,name,location,time,status));
                        requestprovide.setText(""+myRequest.size());




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    onBackPressed();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    //headers.put("Authorization", "Basic " + "c2FnYXJAa2FydHBheS5jb206cnMwM2UxQUp5RnQzNkQ5NDBxbjNmUDgzNVE3STAyNzI=");//put your token here
                    return headers;
                }
            };
            requestQueue.add(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Profile_User.this,HomeActivity.class);
            startActivity(intent);
            finish();
            // back was pressed
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void getUser() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            String URL = Url.user_details;
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("user_id", user_id);


            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject jsonobject = null;
                    ArrayList<Items> list = new ArrayList<Items>();
                    try {
                        jsonobject = new JSONObject(String.valueOf(response));
                        textarray = jsonobject.getJSONArray("response");
                        Log.e("response", String.valueOf(textarray.length()));
                        for (int i = 0; i < textarray.length(); i++) {
                            JSONObject c = textarray.getJSONObject(i);
                            String id = c.getString("id");
                            String email = c.getString("email");
                            String mobile = c.getString("mobile");
                            String location = c.getString("location");
                            String name = c.getString("name");
                            String image = c.getString("image");

                            Log.e("imagesgetvalue",image);


                            if(!image.equalsIgnoreCase("")){
                                Glide.with(Profile_User.this)
                                    .load(image)
                                    .into(imageview);
                            }else{
                                //imageview.setBackgroundResource(R.drawable.avatar);
                            }

                           nametxt.setText(name);
                           locationtxt.setText(location);

                            Log.e("name",name);
                            //list.add()

                        }

                        progress.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    onBackPressed();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    //headers.put("Authorization", "Basic " + "c2FnYXJAa2FydHBheS5jb206cnMwM2UxQUp5RnQzNkQ5NDBxbjNmUDgzNVE3STAyNzI=");//put your token here
                    return headers;
                }
            };
            requestQueue.add(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnServerResponce(String jsonObject, int RequestCode) {
          if(RequestCode == 113){
            try {
                JSONObject jsonobject = new JSONObject(jsonObject);
                JSONArray response = jsonobject.getJSONArray("response");

                for (int i = 0; i < response.length(); i++) {
                    JSONObject object = response.getJSONObject(i);
                    int id = object.getInt("id");
                    Log.e("idsfor", String.valueOf(id));
                    getRequests(id);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getRequests(final int post_id) {
  
//        final ProgressDialog progress = new ProgressDialog(this);
//        progress.setTitle("Loading");
//        progress.setMessage("Wait while loading...");
//        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
//        progress.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            String URL = Url.request_list;
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("post_id", post_id);


            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String time = null;

                    JSONObject jsonobject = null;
                    ArrayList<Items>list = new ArrayList<Items>();
                    try {
                        jsonobject = new JSONObject(String.valueOf(response));
                        getRequestArray = jsonobject.getJSONArray("response");
                        Log.e("getrequestarray", String.valueOf(getRequestArray.length()));
                        for (int i = 0; i < getRequestArray.length(); i++) {
                            JSONObject object = getRequestArray.getJSONObject(i);
                            int id = object.getInt("id");
                            //name = object.getString("name");
                            time = "2 minutes";
                            //Log.e("idsfor", String.valueOf(id)+"name"+name);
                        }
                        //progress.dismiss();
                        getAllvalue(post_id,time,getRequestArray);
                        Log.e("getrequestarray", String.valueOf(getRequestArray.length()));



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    onBackPressed();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    //headers.put("Authorization", "Basic " + "c2FnYXJAa2FydHBheS5jb206cnMwM2UxQUp5RnQzNkQ5NDBxbjNmUDgzNVE3STAyNzI=");//put your token here
                    return headers;
                }
            };
            requestQueue.add(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAllvalue(int post_id, final String time, final JSONArray getRequestArray) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            String URL = Url.post_details;
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("post_id", post_id);


            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                   String image=null;
                   int id = 0;
                   String name = null,location=null;
                    JSONObject jsonobject = null;
                    ArrayList<Items>list = new ArrayList<Items>();
                    try {
                        jsonobject = new JSONObject(String.valueOf(response));
                        jsonlist = jsonobject.getJSONArray("response");

                        for (int i = 0; i < jsonlist.length(); i++) {
                            JSONObject object = jsonlist.getJSONObject(i);
                            id = object.getInt("id");
                            name = object.getString("name");
                            location = object.getString("location");

                            JSONArray jsonArray = (JSONArray) object.get("images");
                            Log.e("jsonarray", String.valueOf(jsonArray.length()));

                            try {
                                image = object.getJSONArray("images").getString(0);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //Log.e("allvalu",location+"image"+image);
                            //Log.e("allvalue","image"+image+"name"+name+"location"+"time"+time+"");
                        }

                        progress.dismiss();


                        getRequest.add(new GetRequests(id,image,name,location,time,getRequestArray.length()));
                        requestrecive.setText(""+getRequest.size());




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    onBackPressed();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    //headers.put("Authorization", "Basic " + "c2FnYXJAa2FydHBheS5jb206cnMwM2UxQUp5RnQzNkQ5NDBxbjNmUDgzNVE3STAyNzI=");//put your token here
                    return headers;
                }
            };
            requestQueue.add(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void OnConnetivityError() {

    }
}
