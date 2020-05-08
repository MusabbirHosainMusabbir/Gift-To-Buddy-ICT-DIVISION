package com.arena.gifttobuddy.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arena.gifttobuddy.Models.AuthenticationListener;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.InstagramApp;
import com.arena.gifttobuddy.Utils.SharedPreference;
import com.arena.gifttobuddy.retrofit.Url;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Login extends AppCompatActivity implements View.OnClickListener,BasicFunctionListener {

    Button signinBtn;
    EditText emailEdt,passwordEdt;
    String email,password;
    BasicFunction bf;
    String name,gender,birthday;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    GoogleSignInAccount account;
    private LoginButton fbLoginBtn;
    private SignInButton googleLoginBtn;
    private CallbackManager callbackManager;
    private ImageView fb,google,instalogin;
    AccessToken accessToken;
    SharedPreference sharedPreference;
    private AuthenticationListener mListener;
    String user_id;
    String loggedin;
    TextView register,forgetpassword;
    ImageView backImg;

    private InstagramApp instaObj;
    public static final String CLIENT_ID = "1059420324433709";
    public static final String CLIENT_SECRET = "cacc6ddc969b82e79dcb4fa02a3c552c";
    public static final String CALLBACK_URL = "https://oceanwp.org/instagram/";
    InstagramApp.OAuthAuthenticationListener listener;
    private boolean isShowPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bf = new BasicFunction(this, this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreference = SharedPreference.getInstance(Login.this);
        loggedin = sharedPreference.getDataLogin("loggedin");
        if(!loggedin.equalsIgnoreCase("0")){
            Intent intent = new Intent(Login.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_login);

        sharedPreference = SharedPreference.getInstance(Login.this);
        user_id = sharedPreference.getData("user_id");

        forgetpassword = findViewById(R.id.forget_password);

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgetPasswordActivity.class);
                intent.putExtra("activity","login");
                startActivity(intent);
                finish();
            }
        });

        backImg = findViewById(R.id.backImg);
        register = findViewById(R.id.login_tv);
        signinBtn = findViewById(R.id.signinBtn);
        emailEdt = findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);

        passwordEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (passwordEdt.getRight() - passwordEdt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        ShowHidePass();
                        //Toast.makeText(Login.this, "values", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });

        instalogin = findViewById(R.id.insta_login);
        instalogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeWebView();
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Login.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Closing Activity")
                        .setMessage("Are you sure you want to close this activity?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
                finish();
            }
        });
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate(emailEdt) && validate(passwordEdt)){
                    login();
                }

            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(Login.this, gso);

        account = GoogleSignIn.getLastSignedInAccount(Login.this);
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

                                            try {
                                                JSONObject jsonObject = new JSONObject();
                                                jsonObject.put("name",name);
                                                jsonObject.put("email", email);
                                                jsonObject.put("password", "111");

                                                Log.e("jsonobj",jsonObject.toString());
                                                bf.getResponceData(Url.registration,jsonObject.toString(),112,"POST");
//                                                Intent intent = new Intent(Login.this,HomeActivity.class);
//                                                startActivity(intent);



                                            } catch (JSONException e) {
                                                Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
                                            }

                                            Log.e("name",name+"email"+email+"gender"+gender+"birthday"+birthday);
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


    }

    public void ShowHidePass() {

        if (isShowPassword) {
            passwordEdt.setTransformationMethod(new PasswordTransformationMethod());
            passwordEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_remove_red_eye_normal_24dp, 0);
            isShowPassword = false;
        }else{
            passwordEdt.setTransformationMethod(null);
            passwordEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_remove_red_eye_black_24dp, 0);
            isShowPassword = true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(Login.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage("Are you sure you want to close this activity?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
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

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name",instaObj.getName());
                    jsonObject.put("email", instaObj.getName());
                    jsonObject.put("password", "111");

                    bf.getResponceData(Url.registration,jsonObject.toString(),118,"POST");
//                                                Intent intent = new Intent(Login.this,HomeActivity.class);
//                                                startActivity(intent);

                } catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                sharedPreference.saveDataLogin("loggedin", "1");
                Intent intent = new Intent(Login.this,HomeActivity.class);
                startActivity(intent);
                finish();
//                Toast.makeText(Login.this, error, Toast.LENGTH_SHORT)
//                       .show();
            }
        });


      //add this in your button click or wherever you need to call the instagram api


    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    private boolean validate(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            return true; // returs true if field is not empty
        }
        editText.setError("Please Fill This");
        editText.requestFocus();
        return false;
    }

    private void login() {
        email = emailEdt.getText().toString();
        password = passwordEdt.getText().toString();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("password", password);

            bf.getResponceData(Url.login,jsonObject.toString(),116,"GET");


        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
        }
    }

    public boolean  isLoggedIn(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        return isLoggedIn;
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
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",account.getDisplayName());
                jsonObject.put("email", account.getEmail());
                jsonObject.put("password", "111");


                bf.getResponceData(Url.registration,jsonObject.toString(),119,"POST");

//                Intent intent = new Intent(Register.this,HomeActivity.class);
//                startActivity(intent);

            } catch (JSONException e) {
                Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
            }


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

//
//    private void showPop() {
//        AlertDialog.Builder builder = newbg AlertDialog.Builder(Login.this);
//        View view = getLayoutInflater().inflate(R.layout.request_sent_confirmation, null);
//        builder.setView(view);
//        final AlertDialog dialog = builder.create();
//        dialog.requestWindowFeature(DialogFragment.STYLE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.getWindow().setLayout(100,100);
//        //dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_animation_phone;
//        dialog.show();
//    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            startActivity(new Intent(Login.this, Register.class));
//            finish();
//
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void OnServerResponce(String jsonObject, int RequestCode) {
        Log.e("jsonobject",jsonObject.toString());
        if(RequestCode == 116){
            JSONObject jsonobject = null;
            try {
                jsonobject = new JSONObject(jsonObject);
                JSONObject response = jsonobject.getJSONObject("response");
                Log.e("response", String.valueOf(response));

                int status = response.getInt("status");

                Log.e("statusvaue", String.valueOf(status));

                if(status==1){
                    int user_id = response.getInt("user_id");
                    String message = response.getString("message");
                    sharedPreference.saveData("user_id", String.valueOf(user_id));
                    sharedPreference.saveDataLogin("loggedin", "1");
                    Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    String message = response.getString("message");
                    Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if(RequestCode==112){
            try {
                JSONObject jsonobject = new JSONObject(jsonObject);
                JSONObject response = jsonobject.getJSONObject("response");
                Log.e("response", String.valueOf(response));

                int status = response.getInt("status");
                String message = response.getString("message");

                if(status==1){
                    Log.e("statusssss", String.valueOf(status));

                    int user_id = response.getInt("user_id");
                    sharedPreference.saveDataLogin("loggedin", "1");
                    sharedPreference.saveData("user_id", String.valueOf(user_id));

                    Log.e("statusoutpus", "ussr_id"+user_id+"status"+String.valueOf(status)+"message"+message);

                    Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    sharedPreference.saveDataLogin("loggedin", "0");
                    Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(RequestCode==119){
            try {
                JSONObject jsonobject = new JSONObject(jsonObject);
                JSONObject response = jsonobject.getJSONObject("response");
                Log.e("response", String.valueOf(response));

                int status = response.getInt("status");
                String message = response.getString("message");

                if(status==1){
                    Log.e("statusssss", String.valueOf(status));

                    int user_id = response.getInt("user_id");
                    sharedPreference.saveDataLogin("loggedin", "1");
                    sharedPreference.saveData("user_id", String.valueOf(user_id));

                    Log.e("statusoutpus", "ussr_id"+user_id+"status"+String.valueOf(status)+"message"+message);

                    Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    sharedPreference.saveDataLogin("loggedin", "0");
                    Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(RequestCode == 118){
            try {
                JSONObject jsonobject = new JSONObject(jsonObject);
                JSONObject response = jsonobject.getJSONObject("response");
                Log.e("response", String.valueOf(response));

                int status = response.getInt("status");
                String message = response.getString("message");

                if(status==1){
                    Log.e("statusssss", String.valueOf(status));

                    int user_id = response.getInt("user_id");
                    sharedPreference.saveDataLogin("loggedin", "1");
                    sharedPreference.saveData("user_id", String.valueOf(user_id));

                    Log.e("statusoutpus", "ussr_id"+user_id+"status"+String.valueOf(status)+"message"+message);

                    Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    sharedPreference.saveDataLogin("loggedin", "0");
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
    public void onClick(View v) {
        if (v == fb) {
            fbLoginBtn.performClick();
        }
    }
}
