package com.arena.gifttobuddy.Views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.arena.gifttobuddy.Adapters.CustomViewAdapter;
import com.arena.gifttobuddy.Adapters.MyAdapter;
import com.arena.gifttobuddy.Models.AuthenticationListener;
import com.arena.gifttobuddy.Models.Items;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.CircleImageView;
import com.arena.gifttobuddy.Utils.InstagramApp;
import com.arena.gifttobuddy.Utils.SharedPreference;
import com.arena.gifttobuddy.Utils.Utility;
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
import com.google.gson.JsonArray;
import com.tanvir.test_library.BasicFunction;
import com.tanvir.test_library.BasicFunctionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Profile_Edit extends AppCompatActivity implements View.OnClickListener,BasicFunctionListener {

    EditText nameEdt,bioEdt,professionEdt,emailEdt,mobileEdt;
    BasicFunction bf;
    CircleImageView profileImg;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    GoogleSignInAccount account;
    private LoginButton fbLoginBtn;
    private SignInButton googleLoginBtn;
    private Resources mResources;
    SharedPreference sharedPreference;
    String user_id;
    JSONArray textarray;
    TextView saveeditTxt;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String base64String;
    ImageView back;
    ImageView pickImg;
    private ImageView fb,google,instalogin;
    AccessToken accessToken;
    private CallbackManager callbackManager;
    private AuthenticationListener mListener;
    String name,email;

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
        setContentView(R.layout.activity_profile__edit);

        sharedPreference = SharedPreference.getInstance(Profile_Edit.this);
        user_id = sharedPreference.getData("user_id");

        mResources = getResources();


        getDetails();

        pickImg = findViewById(R.id.pickImg);
        pickImg.setVisibility(View.INVISIBLE);
        back = findViewById(R.id.back);

        instalogin = findViewById(R.id.insta_login);
        instalogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeWebView();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Edit.this,Profile_User.class);
                startActivity(intent);
                finish();
            }
        });

        nameEdt = findViewById(R.id.name);
        bioEdt = findViewById(R.id.bio);
        professionEdt = findViewById(R.id.profession);
        emailEdt = findViewById(R.id.email);
        mobileEdt = findViewById(R.id.mobile);
        saveeditTxt = findViewById(R.id.savetxt);
        profileImg = findViewById(R.id.imageview_account_profiless);

        //srcBitmap = BitmapFactory.decodeResource(mResources, R.drawable.ibrahim);

        //roundImage();

        saveeditTxt.setText("Edit");

        saveeditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveeditTxt.getText().toString().equalsIgnoreCase("Edit")){
                      pickImg.setVisibility(View.VISIBLE);
                    pickImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectImage();
                        }
                    });

                    saveeditTxt.setText("Save");
                    //Toast.makeText(Profile_Edit.this, "clicked", Toast.LENGTH_SHORT).show();

                    //profileImg.setEnabled(true);

                    nameEdt.setFocusable(true);
                    nameEdt.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                    nameEdt.setClickable(true);


                    bioEdt.setFocusable(true);
                    bioEdt.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                    bioEdt.setClickable(true);



                    professionEdt.setFocusable(true);
                    professionEdt.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                    professionEdt.setClickable(true);


                    emailEdt.setFocusable(true);
                    emailEdt.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                    emailEdt.setClickable(true);



                    mobileEdt.setFocusable(true);
                    mobileEdt.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                    mobileEdt.setClickable(true);
                }

                else if(saveeditTxt.getText().toString().equalsIgnoreCase("Save")){
                    saveValue();
                }
            }
        });


        nameEdt.setFocusable(false);
        nameEdt.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        nameEdt.setClickable(false);

        bioEdt.setFocusable(false);
        bioEdt.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        bioEdt.setClickable(false);


        professionEdt.setFocusable(false);
        professionEdt.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        professionEdt.setClickable(false);

        emailEdt.setFocusable(false);
        emailEdt.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        emailEdt.setClickable(false);


        mobileEdt.setFocusable(false);
        mobileEdt.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        mobileEdt.setClickable(false);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(Profile_Edit.this, gso);

        account = GoogleSignIn.getLastSignedInAccount(Profile_Edit.this);
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
                                            email = object.getString("email");

                                            nameEdt.setText(name);
                                            emailEdt.setText(email);

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
        //srcBitmap = BitmapFactory.decodeResource(mResources, R.drawable.ibrahim);

        //roundImage();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 121);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);


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
   // }

    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {

        //googleSignInAccount = getIntent().getParcelableExtra(GOOGLE_ACCOUNT);
        //Picasso.get().load(googleSignInAccount.getPhotoUrl()).centerInside().fit().into(profileImage);
        //profileName.setText(googleSignInAccount.getDisplayName());
        //profileEmail.setText(googleSignInAccount.getEmail());


        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Register.GOOGLE_ACCOUNT, googleSignInAccount);

        startActivity(intent);
        finish();
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

            nameEdt.setText(account.getDisplayName());
            emailEdt.setText(account.getEmail());

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

                nameEdt.setText(instaObj.getName());
                emailEdt.setText(instaObj.getUserName());
            }

            @Override
            public void onFail(String error) {
//                Intent intent = new Intent(Login.this,HomeActivity.class);
//                startActivity(intent);
//                finish();
//                Toast.makeText(Profile_Edit.this, error, Toast.LENGTH_SHORT)
//                        .show();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Profile_Edit.this,Profile_User.class);
            startActivity(intent);
            finish();
            // back was pressed
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void saveValue() {
        //Log.e("BASE64",base64String);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",user_id);
            jsonObject.put("image",base64String);
            jsonObject.put("name",nameEdt.getText().toString());
            jsonObject.put("description",bioEdt.getText().toString());
            jsonObject.put("profession",professionEdt.getText().toString());
            jsonObject.put("email",emailEdt.getText().toString());
            jsonObject.put("mobile",mobileEdt.getText().toString());

            Log.e("jsons",jsonObject.toString());
            bf.getResponceData(Url.profile_update,jsonObject.toString(),116,"POST");

        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile_Edit.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(Profile_Edit.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Log.e("camera","carmera");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

        if (requestCode == 121) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        base64String = getStringImage(thumbnail);
        Log.e("base64",base64String);
        roundImage(thumbnail);
         //thumbnail = Bitmap.createScaledBitmap(thumbnail,  100 ,100, true);
         profileImg.setImageBitmap(thumbnail);
    }


    private void onSelectFromGalleryResult(Intent data) {

        Log.e("data",data.getDataString().toString());
        Bitmap bm=null;


        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Log.e("bm",bm.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        base64String = getStringImage(bm);
        Log.e("base64fs",base64String);
        roundImage(bm);
        //bm = Bitmap.createScaledBitmap(bm,  100 ,100, true);
        profileImg.setImageBitmap(bm);
    }

    private String getStringImage(Bitmap thumbnail) {
        String imgString="";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);
        return imgString;
        //
    }




    private void getDetails() {
        Log.e("user_detailss",user_id);
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
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
                            String name = c.getString("name");
                            String image = c.getString("image");

                            String profession = c.getString("profession");
                            String bio = c.getString("description");

                            Log.e("imagesgetvalue",image);

                            if(!image.equalsIgnoreCase("")){
                                Glide.with(Profile_Edit.this)
                                    .load(image)
                                    .into(profileImg);
                            }else{
                                //profileImg.setBackgroundResource(R.drawable.avatar);
                            }

                            nameEdt.setText(name);
                            emailEdt.setText(email);
                            mobileEdt.setText(mobile);

                            bioEdt.setText(bio);
                            professionEdt.setText(profession);

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

    private void roundImage(Bitmap srcBitmap) {
        Paint paint = new Paint();

        // Get source bitmap width and height
        int srcBitmapWidth = srcBitmap.getWidth();
        int srcBitmapHeight = srcBitmap.getHeight();

        // Define border and shadow width
        int borderWidth = 25;
        int shadowWidth = 10;

        // destination bitmap width
        int dstBitmapWidth = Math.min(srcBitmapWidth,srcBitmapHeight)+borderWidth*2;
        //float radius = Math.min(srcBitmapWidth,srcBitmapHeight)/2;

        // Initializing a newbg bitmap to draw source bitmap, border and shadow
        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth,dstBitmapWidth, Bitmap.Config.ARGB_8888);

        // Initialize a newbg canvas
        Canvas canvas = new Canvas(dstBitmap);

        // Draw a solid color to canvas
        canvas.drawColor(Color.WHITE);

        // Draw the source bitmap to destination bitmap by keeping border and shadow spaces
        canvas.drawBitmap(srcBitmap, (dstBitmapWidth - srcBitmapWidth) / 2, (dstBitmapWidth - srcBitmapHeight) / 2, null);

        // Use Paint to draw border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth * 2);
        paint.setColor(Color.WHITE);

        // Draw the border in destination bitmap
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2, paint);

        // Use Paint to draw shadow
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(shadowWidth);

        // Draw the shadow on circular bitmap
        canvas.drawCircle(canvas.getWidth()/2,canvas.getHeight()/2,canvas.getWidth()/2,paint);

        // Initialize a newbg RoundedBitmapDrawable object to make ImageView circular
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, dstBitmap);

                /*
                    setCircular(boolean circular)
                        Sets the image shape to circular.
                */
        // Make the ImageView image to a circular image
        roundedBitmapDrawable.setCircular(true);

                /*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                */
        roundedBitmapDrawable.setAntiAlias(true);

        // Set the ImageView image as drawable object
        profileImg.setImageDrawable(roundedBitmapDrawable);

    }

    @Override
    public void OnServerResponce(String jsonObject, int RequestCode) {
        Log.e("jsonobjetss",jsonObject);
          if(RequestCode == 116){
              saveeditTxt.setText("Edit");
              pickImg.setVisibility(View.GONE);
              JSONObject jsonobject = null;
              try {
                  jsonobject = new JSONObject(jsonObject);
                  JSONObject response = jsonobject.getJSONObject("response");
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

    @Override
    public void onClick(View view) {
        if (view == fb) {
            fbLoginBtn.performClick();
        }
    }
}
