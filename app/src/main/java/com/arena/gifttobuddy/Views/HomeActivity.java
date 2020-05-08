package com.arena.gifttobuddy.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.arena.gifttobuddy.Adapters.Select_Img_Adapter;
import com.arena.gifttobuddy.Adapters.TrendingTopAdapter;
import com.arena.gifttobuddy.Models.CustomRecyclerViewItem;
import com.arena.gifttobuddy.Models.Items;
import com.arena.gifttobuddy.Models.TrendingTopItems;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.RecyclerTouchListener;
import com.arena.gifttobuddy.Utils.SharedPreference;
import com.arena.gifttobuddy.Utils.VolleyApplication;
import com.arena.gifttobuddy.customfonts.MyTextView_SF_Pro_Display_Regular;
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


public class HomeActivity extends AppCompatActivity implements BasicFunctionListener {

    private ViewPager viewPager;
    private RecyclerView recyclerView,toprecycleView,trendingrecycleView;
    private LinearLayout pager_indicator;
    private Select_Img_Adapter adapter;
    TrendingTopAdapter trendingAdapter;
    public final static int PAGES = 5;
    public final static int LOOPS = 1000;
    public final static int FIRST_PAGE = PAGES * LOOPS / 2;
    private int dotsCount;
    private ImageView[] dots;
    private List<CustomRecyclerViewItem> itemList = null;
    private CustomViewAdapter customRecyclerViewDataAdapter;
    private List<TrendingTopItems> trendingTopList = new ArrayList<>();;
    NestedScrollView nestedScrollView;
    SearchView mainSearchView;
    androidx.appcompat.widget.SearchView toolbarSearchView;
    ImageView searchToolbar,searchMain;
    BasicFunction bf;
    BottomNavigationView bottomNavigationView;
    SharedPreference sharedPreference;
    String user_id;
    ImageView fabmap;
    JSONArray textarray;
    String image;
    CustomViewAdapter customviewAdapter;
    MyTextView_SF_Pro_Display_Regular categoryseeAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bf = new BasicFunction(this, this);

        sharedPreference = SharedPreference.getInstance(HomeActivity.this);
        user_id = sharedPreference.getData("user_id");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);



        viewPager =(ViewPager)findViewById(R.id.img_list);

        categoryseeAll = findViewById(R.id.categorySeeAll);
        fabmap = findViewById(R.id.fab_map);
        mainSearchView = findViewById(R.id.searchviewMain);
        toolbarSearchView = findViewById(R.id.searchviewToolbar);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        searchMain = findViewById(R.id.searchtoolbarImageMain);
        searchToolbar = findViewById(R.id.searchtoolbarImage);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        categoryseeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,Categories.class);
                startActivity(intent);
                finish();
            }
        });

        fabmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,PublishPost.class);
                startActivity(intent);
                finish();
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.post) {
                    // on favorites clicked
                    Intent intent = new Intent(HomeActivity.this,Categories.class);
                    startActivity(intent);
                    finish();
                    return true;
                }else if(item.getItemId() == R.id.notifications){
                    Intent intent = new Intent(HomeActivity.this,RequestList.class);
                    startActivity(intent);
                    finish();
                    return true;
                }else if(item.getItemId() == R.id.profile){
                    Intent intent = new Intent(HomeActivity.this,Profile_User.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });


        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                boolean isSearchViewVisible = isSearchViewVisible(v,mainSearchView);

                if (isSearchViewVisible){
                    toolbarSearchView.setVisibility(View.GONE);
                    mainSearchView.setVisibility(View.VISIBLE);
                    searchMain.setVisibility(View.VISIBLE);
                    searchToolbar.setVisibility(View.GONE);
                } else{
                    toolbarSearchView.setVisibility(View.VISIBLE);
                    mainSearchView.setVisibility(View.GONE);
                    searchMain.setVisibility(View.GONE);
                    searchToolbar.setVisibility(View.VISIBLE);
                }

            }
        });

        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        adapter = new Select_Img_Adapter(this);
        viewPager.setAdapter(adapter);
        setUiPageViewController();

        viewPager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        viewPager.setPadding(80, 0, 80, 0);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        viewPager.setPageMargin(20);

        viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //viewPager.setPageMargin(-250);
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_select));
                }

                dots[position].setImageDrawable(getResources().getDrawable(R.drawable.select));


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        recyclerView = findViewById(R.id.categoryRecycleView);
        toprecycleView = findViewById(R.id.topwantedGiftRecylceview);
        trendingrecycleView = findViewById(R.id.trendingRecylceview);


        prepareItems();
        initRecycle();
    }


    private boolean isSearchViewVisible(NestedScrollView nestedScrollView, View view) {
        Rect scrollBounds = new Rect();
        nestedScrollView.getDrawingRect(scrollBounds);

        float top = 0f;
        View view1 = view;

        while (!(view1 instanceof LinearLayout)){
            top += (view1).getY();
            view1 = (View)view1.getParent();
        }

        float bottom = top + view.getHeight();
        return scrollBounds.top < bottom && scrollBounds.bottom >top;
    }


    private void prepareItems() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id",user_id);

            Log.e("jsonObject",jsonObject.toString());
            bf.getResponceData(Url.post_list,jsonObject.toString(),116,"POST");


        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
        }
    }

    private void initRecycle() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            String URL = Url.category;
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("value", "1");


            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject jsonobject = null;
                    ArrayList<Items>list = new ArrayList<Items>();
                    try {
                        jsonobject = new JSONObject(String.valueOf(response));
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

                        progress.dismiss();
                        GridLayoutManager layoutManager = new GridLayoutManager(HomeActivity.this,1);
                        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setLayoutManager(layoutManager);
                        customRecyclerViewDataAdapter = new CustomViewAdapter(HomeActivity.this,list);
                        recyclerView.setAdapter(customRecyclerViewDataAdapter);
                        recyclerView.scrollToPosition(1);


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
        // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();



//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("value","0");
//
//            bf.getResponceData(Url.category,jsonObject.toString(),115,"POST");
//
//        } catch (JSONException e) {
//            Log.e("MYAPP", "unexpected JSON exception"+ e.getMessage());
//        }


    }



    private void setUiPageViewController() {
        dotsCount = adapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_select));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.select));
    }

    @Override
    public void OnServerResponce(String jsonObject, int RequestCode) {
        if(RequestCode == 116){
            try {
                JSONObject jsonobject = new JSONObject(jsonObject);
                JSONArray response = jsonobject.getJSONArray("response");
                for (int i = 0; i < response.length(); i++) {
                    JSONObject object = response.getJSONObject(i);
                    int id = object.getInt("id");

                    String name = object.getString("name");
                    String location = object.getString("location");
                    String time = "2 minutes";

                    JSONArray jsonArray = (JSONArray) object.get("images");
                    Log.e("jsonarray", String.valueOf(jsonArray.length()));

                    try {
                        image = object.getJSONArray("images").getString(0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    trendingTopList.add(new TrendingTopItems(id,image,name,location,time));

                    Log.e("trendingtoplistsize", String.valueOf(trendingTopList.size()));
                    Log.e("name",name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            trendingAdapter = new TrendingTopAdapter(this, trendingTopList);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            toprecycleView.setLayoutManager(mLayoutManager);
            toprecycleView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            toprecycleView.setItemAnimator(new DefaultItemAnimator());
            toprecycleView.setAdapter(trendingAdapter);


            toprecycleView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    TextView textView = view.findViewById(R.id.idvalue);
                    String value = textView.getText().toString();
                    Log.e("value",value);
                     Intent intent = new Intent(HomeActivity.this,ItemDetails.class);
                     intent.putExtra("post_id",value);
                     startActivity(intent);
                     finish();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


            RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(this, 2);
            trendingrecycleView.setLayoutManager(mLayoutManager1);
            trendingrecycleView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            trendingrecycleView.setItemAnimator(new DefaultItemAnimator());
            trendingrecycleView.setAdapter(trendingAdapter);

            trendingrecycleView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    TextView textView = view.findViewById(R.id.idvalue);
                    String value = textView.getText().toString();
                    Log.e("value",value);
                    Intent intent = new Intent(HomeActivity.this,ItemDetails.class);
                    intent.putExtra("post_id",value);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

        }

        else if(RequestCode == 115){

        }

    }

    @Override
    public void OnConnetivityError() {

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
