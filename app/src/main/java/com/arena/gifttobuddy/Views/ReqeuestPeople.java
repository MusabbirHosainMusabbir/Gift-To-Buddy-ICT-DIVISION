package com.arena.gifttobuddy.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arena.gifttobuddy.Adapters.AdapterPeopleRequest;
import com.arena.gifttobuddy.Adapters.CustomViewAdapter;
import com.arena.gifttobuddy.Models.Items;
import com.arena.gifttobuddy.Models.PeopleRequest;
import com.arena.gifttobuddy.Models.TrendingTopItems;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.RecyclerTouchListener;
import com.arena.gifttobuddy.retrofit.Url;
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

public class ReqeuestPeople extends AppCompatActivity implements BasicFunctionListener{

    String post_id,description,name;
    int id;
    int postid,userid;
    BasicFunction bf;
    JSONArray getRequestArray;
    String location;
    private List<PeopleRequest> peoplelist = new ArrayList<>();
    RecyclerView recyclerView;
    AdapterPeopleRequest adapter;
    Button getRequestBtn,myrequestBtn;
    ImageView backBtn;
    int status;
    String size;
    BottomNavigationView bottomNavigationView;
    Button acceptButton,rejectButton;
    ImageView fabmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reqeuest_people);
        bf = new BasicFunction(this, this);
        Bundle extras = getIntent().getExtras();

        myrequestBtn = findViewById(R.id.myRequest);
        myrequestBtn.setBackgroundResource(R.drawable.unselected_capsule);
        getRequestBtn = findViewById(R.id.getRequest);
        getRequestBtn.setBackgroundResource(R.drawable.selected_capsule);

        if (extras != null) {
            post_id = extras.getString("post_id");
            size = extras.getString("size");
            Log.e("size",size);
            getRequestBtn.setText(size);
            // and get whatever type user account id is
            //getvalue(post_id);
            getPeople(post_id);
        }



        recyclerView = findViewById(R.id.requestPeopleRecycleview);
        backBtn = findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReqeuestPeople.this,RequestList.class);
                startActivity(intent);
                finish();
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.notifications);
        fabmap = findViewById(R.id.post_img);

        fabmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReqeuestPeople.this,PublishPost.class);
                startActivity(intent);
                finish();
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
                    Intent intent = new Intent(ReqeuestPeople.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (item.getItemId() == R.id.post) {
                    // on favorites clicked
                    Intent intent = new Intent(ReqeuestPeople.this,Categories.class);
                    startActivity(intent);
                    finish();
                    return true;
                }else if(item.getItemId() == R.id.notifications){
                    Intent intent = new Intent(ReqeuestPeople.this,RequestList.class);
                    startActivity(intent);
                    finish();
                    return true;
                }else if(item.getItemId() == R.id.profile){
                    Intent intent = new Intent(ReqeuestPeople.this,Profile_User.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ReqeuestPeople.this,RequestList.class);
            startActivity(intent);
            finish();
            // back was pressed
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void getPeople(final String post_id) {
        Log.e("postid",post_id);
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
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
                    ArrayList<Items> list = new ArrayList<Items>();
                    try {
                        jsonobject = new JSONObject(String.valueOf(response));
                        getRequestArray = jsonobject.getJSONArray("response");
                        if(getRequestArray.length() == 0){
                            progress.dismiss();
                        }

                        Log.e("getrequestarray", String.valueOf(getRequestArray.length()));
                        for (int i = 0; i < getRequestArray.length(); i++) {
                            JSONObject object = getRequestArray.getJSONObject(i);
                            id = object.getInt("id");
                            postid = object.getInt("post_id");
                            userid = object.getInt(("id"));
                            description = object.getString("description");
                            status = object.getInt("status");

                            Log.e("userid", String.valueOf(status));

                            progress.dismiss();
                            getValue(id,post_id,userid,description,status);


                            //name = object.getString("name");

                            //Log.e("idsfor",location);
                        }

                        //getAllvalue(post_id,time,getRequestArray);
                        // Log.e("getrequestarray", String.valueOf(getRequestArray.length()));



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

    private void getValue(final int id, final String post_id, final int userid, final String description, final int status) {
        Log.e("valuess","id"+id+"postid"+post_id+"userid"+userid+"des"+description+"stat"+status);
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
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
                    ArrayList<Items> list = new ArrayList<Items>();
                    try {
                        jsonobject = new JSONObject(String.valueOf(response));
                        getRequestArray = jsonobject.getJSONArray("response");
                        Log.e("getrequestarray", String.valueOf(getRequestArray.length()));
                        for (int i = 0; i < getRequestArray.length(); i++) {
                            JSONObject object = getRequestArray.getJSONObject(i);
                            location = object.getString("location");
                            //name = object.getString("name");

                            Log.e("idsfor",location);
                            getUsername(id,post_id,location,userid,description,status);
                        }

                        progress.dismiss();

                        //getAllvalue(post_id,time,getRequestArray);
                        // Log.e("getrequestarray", String.valueOf(getRequestArray.length()));



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

    private void getUsername(final int id,final String post_id,final String location, final int userid, final String description,final  int status) {
        Log.e("valuelist","id"+id+"locatoin"+location+"postid"+post_id+"userid"+userid+"des"+description+"stat"+status);
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            String URL = Url.user_details;
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("user_id", userid);


            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject jsonobject = null;
                    ArrayList<Items> list = new ArrayList<Items>();
                    try {
                        jsonobject = new JSONObject(String.valueOf(response));
                        getRequestArray = jsonobject.getJSONArray("response");
                        Log.e("getrequestarray", String.valueOf(getRequestArray.length()));

                        for (int i = 0; i < getRequestArray.length(); i++) {
                            JSONObject object = getRequestArray.getJSONObject(i);
                            String name = object.getString("name");
                            String image = object.getString("image");
                            //name = object.getString("name");

                            Log.e("userdetailsnvas",name);
                            Log.e("image",image);

                          Log.e("valueslistsvakye","name"+name+"location"+location+"description"+description);
                          peoplelist.add(new PeopleRequest(userid,image,name,location,description,status));
                        }

                        progress.dismiss();
                        GridLayoutManager layoutManager = new GridLayoutManager(ReqeuestPeople.this,1);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new AdapterPeopleRequest(ReqeuestPeople.this,peoplelist);
                        recyclerView.setAdapter(adapter);
                        recyclerView.scrollToPosition(1);

                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                TextView textView = view.findViewById(R.id.id);
                                acceptButton = view.findViewById(R.id.accept);
                                rejectButton = view.findViewById(R.id.reject);

                                acceptButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        acceptButton.setText("Request Accepted");
                                        rejectButton.setText("Reject");
                                        try {
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("id",id);
                                            jsonObject.put("user_id",userid);
                                            jsonObject.put("post_id",post_id);
                                            jsonObject.put("status",1);

                                            bf.getResponceData(Url.request_update,jsonObject.toString(),109,"POST");



                                        } catch (JSONException e) {
                                            Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
                                        }

                                    }
                                });

                                rejectButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        rejectButton.setText("Request Rejected");
                                        acceptButton.setText("Accept");
                                        try {
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("id",id);
                                            jsonObject.put("user_id",userid);
                                            jsonObject.put("post_id",post_id);
                                            jsonObject.put("status",2);

                                            bf.getResponceData(Url.request_update,jsonObject.toString(),110,"POST");



                                        } catch (JSONException e) {
                                            Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
                                        }
                                    }
                                });

                                String value = textView.getText().toString();
                                Log.e("value",value);

                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }
                        }));

                        //getAllvalue(post_id,time,getRequestArray);
                        // Log.e("getrequestarray", String.valueOf(getRequestArray.length()));



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
          if(RequestCode == 109){
              JSONObject jsonobject = null;
              try {
                  jsonobject = new JSONObject(jsonObject);
                  JSONObject response = jsonobject.getJSONObject("response");
                  int requested_id = response.getInt("request_id");
                  String user_id = response.getString("user_id");
                  int status = response.getInt("status");

                  Log.e("jsonobjjext",jsonObject);
                  Log.e("astatus", String.valueOf(status));
                  String message = response.getString("message");

                  if(status==1){
                      Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();

                  }else{
                      Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                  }

              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }
          else if(RequestCode == 110){
              JSONObject jsonobject = null;
              try {
                  jsonobject = new JSONObject(jsonObject);
                  JSONObject response = jsonobject.getJSONObject("response");
                  int requested_id = response.getInt("request_id");
                  String user_id = response.getString("user_id");
                  int status = response.getInt("status");
                  String message = response.getString("message");

                  if(status==1){
                      Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();

                  }else{
                      Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
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
