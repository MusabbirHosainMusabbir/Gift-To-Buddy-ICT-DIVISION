package com.arena.gifttobuddy.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arena.gifttobuddy.Adapters.AdapterGetRequest;
import com.arena.gifttobuddy.Adapters.AdapterMyRequest;
import com.arena.gifttobuddy.Adapters.AdapterPeopleRequest;
import com.arena.gifttobuddy.Adapters.CustomViewAdapter;
import com.arena.gifttobuddy.Adapters.MyAdapter;
import com.arena.gifttobuddy.Adapters.TrendingTopAdapter;
import com.arena.gifttobuddy.Helpers.Utils;
import com.arena.gifttobuddy.Models.GetRequests;
import com.arena.gifttobuddy.Models.Items;
import com.arena.gifttobuddy.Models.MyRequest;
import com.arena.gifttobuddy.Models.PeopleRequest;
import com.arena.gifttobuddy.Models.TrendingTopItems;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.RecyclerTouchListener;
import com.arena.gifttobuddy.Utils.SharedPreference;
import com.arena.gifttobuddy.retrofit.Url;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tanvir.test_library.BasicFunction;
import com.tanvir.test_library.BasicFunctionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestList extends AppCompatActivity implements BasicFunctionListener {

    Button myrequestBtn,getRequestBtn;
    ImageView backBtn;
    SharedPreference sharedPreference;
    String user_id,image;
    BasicFunction bf;
    String name,images,location,time;
    int id;
    int status;
    private List<MyRequest> myRequest = new ArrayList<>();
    private List<GetRequests> getRequest = new ArrayList<>();
    AdapterMyRequest myRequestAdapter;
    AdapterGetRequest myGetRequestAdapter;
    RecyclerView recyclerView;
    JSONArray getRequestArray,jsonlist;
    BottomNavigationView bottomNavigationView;
    ImageView fabmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        bf = new BasicFunction(this, this);

        sharedPreference = SharedPreference.getInstance(RequestList.this);
        user_id = sharedPreference.getData("user_id");

        backBtn = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recycleView);
        myrequestBtn = findViewById(R.id.myRequest);
        getRequestBtn = findViewById(R.id.getRequest);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.post).setChecked(true);
        fabmap = findViewById(R.id.post_img);

        fabmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestList.this,PublishPost.class);
                startActivity(intent);
                finish();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
                    Intent intent = new Intent(RequestList.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (item.getItemId() == R.id.post) {
                    // on favorites clicked
                    Intent intent = new Intent(RequestList.this,Categories.class);
                    startActivity(intent);
                    finish();
                    return true;
                }else if(item.getItemId() == R.id.notifications){
                    Intent intent = new Intent(RequestList.this,RequestList.class);
                    startActivity(intent);
                    finish();
                    return true;
                }else if(item.getItemId() == R.id.profile){
                    Intent intent = new Intent(RequestList.this,Profile_User.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        Utils.fullScreenView(this,false);

        myRequest(user_id);
        myrequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myrequestBtn.setBackgroundResource(R.drawable.selected_capsule);
                getRequestBtn.setBackgroundResource(R.drawable.unselected_capsule);
                myRequest.clear();
                getRequest.clear();
                myRequest(user_id);

            }
        });

        getRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRequestBtn.setBackgroundResource(R.drawable.selected_capsule);
                myrequestBtn.setBackgroundResource(R.drawable.unselected_capsule);
                getRequest.clear();
                myRequest.clear();
                getRequest();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent = new Intent(RequestList.this,HomeActivity.class);
                  startActivity(intent);
                  finish();
            }
        });



        if (!Utils.hasNavBar(this)){
            Log.e("hasNav--->", "onCreate: YES---> " );
            Utils.adjustBottomNav(this, R.id.soft_key_layout, R.id.main_layout);
            //navLayout.setVisibility(View.GONE);
        }

        //requestBtn = findViewById(R)
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(RequestList.this,HomeActivity.class);
            startActivity(intent);
            finish();
            // back was pressed
            return true;
        }
        return super.onKeyDown(keyCode, event);

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
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();

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
                            progress.dismiss();
                        }

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject object = response.getJSONObject(i);
                            int id = object.getInt("id");
                            Log.e("id", String.valueOf(id));
                            name = object.getString("name");

                            int user_id = object.getInt("user_id");
                            String description = object.getString("description");
                            status = object.getInt("status");
                            int post_id = object.getInt("post_id");
                            time = "2 minutes";

                            Log.e("namevaluse",name);

                            progress.dismiss();
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

    @Override
    public void OnServerResponce(String jsonObject, int RequestCode) {
          if(RequestCode == 116){
              try {

                  Log.e("response",jsonObject);
                  JSONObject jsonobject = new JSONObject(jsonObject);
                  JSONArray response = jsonobject.getJSONArray("response");


                  for (int i = 0; i < response.length(); i++) {
                      JSONObject object = response.getJSONObject(i);
                      int id = object.getInt("id");
                      Log.e("id", String.valueOf(id));
                      name = object.getString("name");

                      int user_id = object.getInt("user_id");
                      String description = object.getString("description");
                      status = object.getInt("status");
                      int post_id = object.getInt("post_id");
                      time = "2 minutes";

                      Log.e("namevaluse",name);

                      passvalue(name,time,post_id,status);
                  }
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }
          else if(RequestCode == 119){

          }

          else if(RequestCode == 113){
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
          }else if(RequestCode == 111){
              try {
                  JSONObject jsonobject = new JSONObject(jsonObject);
                  JSONArray response = jsonobject.getJSONArray("response");
                  Log.e("responsesvalue", String.valueOf(response.length()));
//                  for (int i = 0; i < response.length(); i++) {
//                      JSONObject object = response.getJSONObject(i);
//                      int id = object.getInt("id");
//                      Log.e("id", String.valueOf(id));
//                      getRequests(id);
//
//                  }
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }
    }

    private void getRequests(final int post_id) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            String URL = Url.request_list;
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("post_id", post_id);


            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

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
                        progress.dismiss();
                        getAllvalue(post_id,time,getRequestArray.length());
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

    private void getAllvalue(int post_id, final String time, final int getRequestArray) {
        Log.e("Reqeustarray", String.valueOf(getRequestArray));
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

                        Log.e("allvalueget",image+"name"+name+"location"+location+"time"+time+"getRequestArray"+getRequestArray);
                        getRequest.add(new GetRequests(id,image,name,location,time,getRequestArray));


                        myGetRequestAdapter = new AdapterGetRequest(RequestList.this, getRequest);

                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(RequestList.this, 1);
                        recyclerView.setLayoutManager(mLayoutManager);

                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(myGetRequestAdapter);
                        getRequestBtn.setText(("Get Requests(" + getRequest.size() + ")"));

                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Log.e("buttontext",getRequestBtn.getText().toString());

                                TextView textView = view.findViewById(R.id.id);
                                String value = textView.getText().toString();
                                Log.e("value",value);
                                Intent intent = new Intent(RequestList.this,ReqeuestPeople.class);
                                intent.putExtra("post_id",value);
                                intent.putExtra("size",getRequestBtn.getText().toString());
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }
                        }));


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


                        myRequestAdapter = new AdapterMyRequest(RequestList.this, myRequest);

                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(RequestList.this, 1);
                        recyclerView.setLayoutManager(mLayoutManager);

                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(myRequestAdapter);
                        myrequestBtn.setText(("My Request(" + myRequest.size() + ")"));

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
