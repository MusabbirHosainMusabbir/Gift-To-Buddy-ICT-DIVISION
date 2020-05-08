package com.arena.gifttobuddy.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arena.gifttobuddy.Adapters.MyAdapter;
import com.arena.gifttobuddy.Helpers.Utils;
import com.arena.gifttobuddy.Models.Category;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.FileUtils;
import com.arena.gifttobuddy.Utils.SharedPreference;
import com.arena.gifttobuddy.Utils.Utility;
import com.arena.gifttobuddy.retrofit.Url;
import com.google.android.flexbox.FlexboxLayout;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PublishPost extends AppCompatActivity implements BasicFunctionListener {
    Button publishButton,newBtn,usedBtn;
    ImageView addBtn;
    FlexboxLayout categoryLinear;
    EditText productTitle,productDescription,reasonForGift,targetToGift,location;
    RecyclerView recyclerView;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 2;
    String base64String;
    private ArrayList<Bitmap> arrayList;
    private ArrayList<String> base64Images;
    Button userLabel[];
    int i=0;
    AlertDialog dialog;

    public ArrayList<Category> booleanArrayList = new ArrayList<>();

    private final int REQUEST_CODE_PERMISSIONS  = 1;
    private final int REQUEST_CODE_READ_STORAGE = 2;
    BasicFunction bf;
    JSONArray textarray;
    SharedPreference sharedPreference;
    String user_id;
    String category;
    int selected = 0;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bf = new BasicFunction(this, this);
        setContentView(R.layout.activity_publish_post);
        publishButton = findViewById(R.id.publish_btn);

        sharedPreference = SharedPreference.getInstance(PublishPost.this);
        user_id = sharedPreference.getData("user_id");

        addBtn = findViewById(R.id.add_btn);
        newBtn = findViewById(R.id.newBtn);
        usedBtn = findViewById(R.id.usedBtn);
        back = findViewById(R.id.back);

        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = 2;
                newBtn.setBackgroundResource(R.drawable.used);
                newBtn.setTextColor(getResources().getColor(R.color.white));
                usedBtn.setBackgroundResource(R.drawable.newbg);
            }
        });

        usedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = 1;
                usedBtn.setBackgroundResource(R.drawable.used);
                newBtn.setTextColor(getResources().getColor(R.color.intro_title_color));
                newBtn.setBackgroundResource(R.drawable.newbg);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublishPost.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        productTitle = findViewById(R.id.productTitleEdt);
        productDescription = findViewById(R.id.productDescriptionEdt);
        reasonForGift = findViewById(R.id.resaonEdt);
        targetToGift = findViewById(R.id.targetEdt);
        location = findViewById(R.id.locationEdt);

        categoryLinear = (FlexboxLayout) findViewById(R.id.mainRecepientLayout);
        getTextViews();


        arrayList = new ArrayList<>();
        base64Images = new ArrayList<>();

        Utils.fullScreenView(this,false);

        if (!Utils.hasNavBar(this)){
            Log.e("hasNav--->", "onCreate: YES---> " );
            Utils.adjustBottomNav(this, R.id.soft_key_layout, R.id.main_layout);
            //navLayout.setVisibility(View.GONE);

        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishPost();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(PublishPost.this,HomeActivity.class);
            startActivity(intent);
            finish();
            // back was pressed
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void getTextViews() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value","0");

            bf.getResponceData(Url.category,jsonObject.toString(),116,"POST");

        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
        }



    }

    private void publishPost() {

        Log.e("selected", String.valueOf(selected));
        try {
            JSONObject post = new JSONObject();
            post.put("user_id", user_id);
            post.put("name",productTitle.getText().toString());
            post.put("description",productDescription.getText().toString());
            post.put("category_id", category);
            post.put("top_gift", "1");
            post.put("conditions",selected);

            JSONArray jsonArray = new JSONArray();

            Log.e("base64length", String.valueOf(base64Images.size()));
            for (int i = 0; i < base64Images.size(); i++) {
                String imageString = base64Images.get(i).toString();
                jsonArray.put(imageString);
            }

            post.put("images",jsonArray);

            post.put("reason_for_gift",reasonForGift.getText().toString());
            post.put("target_to_gift",targetToGift.getText().toString());
            post.put("location",location.getText().toString());


            bf.getResponceData(Url.create_post,post.toString(),114,"POST");

            Log.e("postjson", post.toString());
            Log.e("arraylist", String.valueOf(base64Images.size()));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(PublishPost.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(PublishPost.this);

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

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_READ_STORAGE);

//        Intent intent = newbg Intent();
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Log.e("camera","carmera");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        Log.e("resultcodce", String.valueOf(requestCode));
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(resultData);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(resultData);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Log.e("getgallery","data");
        Log.e("datavalues", String.valueOf(data));
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        base64String = getStringImage(bm);
        int maxLogSize = 1000;
        for(int j = 0; j <= base64String.length() / maxLogSize; j++) {
            int start = j * maxLogSize;
            int end = (j+1) * maxLogSize;
            end = end > base64String.length() ? base64String.length() : end;
            Log.e("Tags", base64String.substring(start, end));
        }


        base64Images.add(base64String);
        Log.e("base64",base64String);
        arrayList.add(bm);
        LinearLayoutManager layoutManagerPayment = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(layoutManagerPayment);
        MyAdapter mAdapter = new MyAdapter(PublishPost.this, arrayList);
        recyclerView.setAdapter(mAdapter);

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);

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
        base64Images.add(base64String);
        arrayList.add(thumbnail);
        LinearLayoutManager layoutManagerPayment = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(layoutManagerPayment);
        MyAdapter mAdapter = new MyAdapter(PublishPost.this, arrayList);
        recyclerView.setAdapter(mAdapter);
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

    @Override
    public void OnServerResponce(String jsonObject, int RequestCode) {
        Log.e("requestcode", String.valueOf(RequestCode));
        if(RequestCode == 116){
            JSONObject jsonobject = null;
            try {
                jsonobject = new JSONObject(jsonObject);
                textarray = jsonobject.getJSONArray("response");
                Log.e("response", String.valueOf(textarray.length()));
                for (int i = 0; i < textarray.length(); i++) {
                    JSONObject c = textarray.getJSONObject(i);
                    String id = c.getString("id");
                    String name = c.getString("name");
                    Log.e("name",name);
                    booleanArrayList.add(new Category(id,name));

                }

                addRecepientList(booleanArrayList,categoryLinear);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
           else if(RequestCode == 114){
               Log.e("publish","publils");
               Log.e("responsepost",jsonObject.toString());
            try {
                JSONObject jsonobject = new JSONObject(jsonObject);
                JSONObject response = jsonobject.getJSONObject("response");
                String user_id = response.getString("post_id");
                int status = response.getInt("status");
                String message = response.getString("message");
                if(status==1){
                    showPop();
                    Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                }
                Log.e("userid",user_id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showPop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PublishPost.this);


        View view = getLayoutInflater().inflate(R.layout.popup, null);
        TextView keepbrowsing = view.findViewById(R.id.read_btn);
        keepbrowsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublishPost.this,HomeActivity.class);
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
        int dialogWindowWidth = (int) (displayWidth * 0.75f);
        // Set alert dialog height equal to screen height 70%
        int dialogWindowHeight = (int) (displayHeight * 0.75f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        dialog.getWindow().setAttributes(layoutParams);


    }

    private void addRecepientList(final ArrayList<Category> booleanArrayList, FlexboxLayout categoryLinear) {
        Log.e("arraylkist", String.valueOf(booleanArrayList.size()));
        userLabel = new Button[booleanArrayList.size()];
        for (i = 0; i < booleanArrayList.size(); i++) {
            LinearLayout layout = new LinearLayout(PublishPost.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setPadding(2, 0, 2, 0);
            layout.setLayoutParams(params);

            userLabel[i] = new Button(this);
            userLabel[i].setText(booleanArrayList.get(i).getName());
            userLabel[i].setTextColor(Color.parseColor("#868686"));
            userLabel[i].setLayoutParams(params);
            userLabel[i].setTag(booleanArrayList.get(i).getId());
            userLabel[i].setBackgroundResource(R.drawable.unselected_capsule);
            userLabel[i].setPadding(10, 10, 10, 10);
            layout.addView(userLabel[i]);
            categoryLinear.addView(layout);

            userLabel[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < booleanArrayList.size(); i++) {
                        userLabel[i].setBackgroundResource(R.drawable.unselected_capsule);
                    }
                    Log.e("ivalue", String.valueOf(i));
                    v.setBackgroundResource(R.drawable.selected_capsule);
                    Log.e("tag",v.getTag().toString());

                    category = v.getTag().toString();
                }
            });
        }
    }

    @Override
    public void OnConnetivityError() {

    }
}
