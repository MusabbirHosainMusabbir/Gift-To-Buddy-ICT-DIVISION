package com.arena.gifttobuddy.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.widget.NestedScrollView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arena.gifttobuddy.Helpers.RoundedImg;
import com.arena.gifttobuddy.Models.AuthenticationListener;
import com.arena.gifttobuddy.Models.GetRequests;
import com.arena.gifttobuddy.Models.Items;
import com.arena.gifttobuddy.Models.MyRequest;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.CircleImageView;
import com.arena.gifttobuddy.Utils.InstagramApp;
import com.arena.gifttobuddy.Utils.SharedPreference;
import com.arena.gifttobuddy.customfonts.MyTextView_SF_Pro_Display_Bold;
import com.arena.gifttobuddy.customfonts.MyTextView_SF_Pro_Display_Regular;
import com.arena.gifttobuddy.retrofit.Url;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.tanvir.test_library.BasicFunction;
import com.tanvir.test_library.BasicFunctionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftReceiver extends AppCompatActivity implements View.OnClickListener,BasicFunctionListener {

    ImageView profileImg;
    RoundedImg roundedImage;
    CardView cardView;
    ScrollView scrollview;
    SharedPreference sharedPreference;
    String user_id;
    MyTextView_SF_Pro_Display_Regular nametxt,locationtxt,descriptiontxt,professiontxt;
    JSONArray textarray,jsonlist,getRequestArray;
    CircleImageView circleImageView;
    ImageView backImg;
    private List<MyRequest> myRequest = new ArrayList<>();
    private List<GetRequests> getRequest = new ArrayList<>();
    MyTextView_SF_Pro_Display_Bold provideRequest,getMyRequest;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    GoogleSignInAccount account;
    private LoginButton fbLoginBtn;
    private SignInButton googleLoginBtn;
    private ImageView fb,google,instalogin;
    AccessToken accessToken;
    private CallbackManager callbackManager;
    private AuthenticationListener mListener;
    BasicFunction bf;
    String name;

    private InstagramApp instaObj;
    public static final String CLIENT_ID = "1059420324433709";
    public static final String CLIENT_SECRET = "cacc6ddc969b82e79dcb4fa02a3c552c";
    public static final String CALLBACK_URL = "https://oceanwp.org/instagram/";
    InstagramApp.OAuthAuthenticationListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bf = new BasicFunction(this, this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gift_receiver);

        sharedPreference = SharedPreference.getInstance(GiftReceiver.this);
        user_id = sharedPreference.getData("user_id");
        circleImageView = findViewById(R.id.imageview_account_profile);

        myRequest(user_id);
        getRequest();

        instalogin = findViewById(R.id.insta_login);
        instalogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeWebView();
            }
        });

        provideRequest = findViewById(R.id.myRequest);
        getMyRequest = findViewById(R.id.getRequest);

        backImg = findViewById(R.id.backImg);

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GiftReceiver.this,Profile_User.class);
                startActivity(intent);
                finish();
            }
        });

        nametxt = findViewById(R.id.name);
        locationtxt = findViewById(R.id.location);
        descriptiontxt = findViewById(R.id.description);
        professiontxt = findViewById(R.id.profession);

        //cardView = findViewById(R.id.cardView);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(GiftReceiver.this, gso);

        account = GoogleSignIn.getLastSignedInAccount(GiftReceiver.this);
//        if (account != null)
//            updateUI(account);
//        else
//            btnLogout.setVisibility(View.GONE);

        FacebookSdk.sdkInitialize(getApplicationContext());
        accessToken = AccessToken.getCurrentAccessToken();
        Log.e("accesstoken", String.valueOf(accessToken));

        callbackManager = CallbackManager.Factory.create();
        fb = (ImageView) findViewById(R.id.fb_login);
        google = (ImageView) findViewById(R.id.google_login);

        fbLoginBtn = (LoginButton) findViewById(R.id.login_button);
        googleLoginBtn = (SignInButton) findViewById(R.id.google_button);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        fb.setOnClickListener(this);

        mListener = new AuthenticationListener() {
            @Override
            public void onSuccess(String accessToken) {
                Log.e("accesstoken",accessToken);
            }

            @Override
            public void onFail(String error) {

            }
        };

        fbLoginBtn.setReadPermissions(Arrays.asList(
                "public_profile", "email"));

        fbLoginBtn.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("onSuccess");

                        String accessToken = loginResult.getAccessToken()
                                .getToken();
                        Log.i("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {

                                        Log.i("LoginActivity",
                                                response.toString());
                                        try {
                                            name = object.getString("name");
                                            nametxt.setText(name);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields",
                                "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.v("LoginActivity", exception.getCause().toString());
                    }
                });

        getUser();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 121);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

//        callbackManager.onActivityResult(requestCode,resultCode,data);
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK)
//            switch (requestCode) {
//                case 101:
//                    try {
//                        // The Task returned from this call is always completed, no need to attach
//                        // a listener.
//                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//                        GoogleSignInAccount account = task.getResult(ApiException.class);
//                        onLoggedIn(account);
//                    } catch (ApiException e) {
//                        // The ApiException status code indicates the detailed failure reason.
//                        Log.w("Tag", "signInResult:failed code=" + e.getStatusCode());
//                    }
//                    break;
//            }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            updateUI(account);
        } catch (ApiException e) {
            Log.w("Google Error ", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        try {

            String strData = "Name : " + account.getDisplayName()
                    + "\r\nEmail : " + account.getEmail() + "\r\nGiven name : " + account.getGivenName()
                    + "\r\nDisplay Name : " + account.getDisplayName() + "\r\nId : "
                    + account.getId();

            Log.e("strData",strData);

            nametxt.setText(account.getDisplayName());

            //forgetPasswordTv.setText(account.getEmail());
            //Log.d("Image URL : ", account.getPhotoUrl().toString());
            Log.d("LoginData : ", strData);


        } catch (NullPointerException ex) {
            //lblInfo.setText(lblInfo.getText().toString() + "\r\n" + "NullPointerException : " + ex.getMessage().toString());
        } catch (RuntimeException ex) {
            //lblInfo.setText(lblInfo.getText().toString() + "\r\n" + "RuntimeException : " + ex.getMessage().toString());
        } catch (Exception ex) {
// lblInfo.setText(ex.getMessage().toString());
        }
    }

    private void initializeWebView() {
        instaObj = new InstagramApp(this, CLIENT_ID,
                CLIENT_SECRET, CALLBACK_URL);
        instaObj.authorize();
        instaObj.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                Log.e("Userid", instaObj.getId());
                Log.e("Name", instaObj.getName());
                Log.e("UserName", instaObj.getName());
                //Log.e("email",instaObj.)

               nametxt.setText(instaObj.getName());
            }

            @Override
            public void onFail(String error) {
//                Intent intent = new Intent(Login.this,HomeActivity.class);
//                startActivity(intent);
//                finish();
                Toast.makeText(GiftReceiver.this, error, Toast.LENGTH_SHORT)
                        .show();
            }
        });

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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(GiftReceiver.this,Profile_User.class);
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
                            String description = c.getString("description");
                            String location = c.getString("location");
                            String profession = c.getString("profession");
                            String name = c.getString("name");
                            String image = c.getString("image");

                            Log.e("imagesgetvalue",image);


                            if(!image.equalsIgnoreCase("")){
                                Glide.with(GiftReceiver.this)
                                        .load(image)
                                        .into(circleImageView);
                            }else{
                                //imageview.setBackgroundResource(R.drawable.avatar);
                            }

                            nametxt.setText(name);
                            locationtxt.setText(location);
                            descriptiontxt.setText(description);
                            professiontxt.setText(profession);

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
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
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
                        provideRequest.setText(""+myRequest.size());




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
                        getMyRequest.setText(""+getRequest.size());




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

    @Override
    public void OnConnetivityError() {

    }

    @Override
    public void onClick(View view) {
        if (view == fb) {
            fbLoginBtn.performClick();
        }
    }
}
