package com.arena.gifttobuddy.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.FileUtils;
import com.arena.gifttobuddy.customfonts.MyTextView_SF_Pro_Display_Regular;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Akshay Raj on 06/02/18.
 * akshay@snowcorp.org
 * www.snowcorp.org
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Bitmap> arrayList;

    public MyAdapter(Context context, ArrayList<Bitmap> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

        Log.e("arraylist",arrayList.get(0).toString());
    }


    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_add_image, parent, false);
        return new MyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e("arraylist", String.valueOf(arrayList.size()));
        holder.image.setImageBitmap(arrayList.get(position));


        //holder.imageurl.setText();
//        Glide.with(context)
//                .load(arrayList.get(position))
//                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
        }
    }
}
