package com.arena.gifttobuddy.Adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arena.gifttobuddy.Models.TrendingTopItems;
import com.arena.gifttobuddy.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrendingTopAdapter extends RecyclerView.Adapter<TrendingTopAdapter.MyViewHolder> {
    Context mContext;
    private List<TrendingTopItems> trendingTopList;

    @NonNull
    @Override
    public TrendingTopAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list, parent, false);

        return new MyViewHolder(itemView);
    }

    public TrendingTopAdapter(Context mContext, List<TrendingTopItems> trendingTop) {
        this.mContext = mContext;
        this.trendingTopList = trendingTop;

        Log.e("trendingtoplist", String.valueOf(trendingTopList.size()));
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingTopAdapter.MyViewHolder holder, int position) {
        TrendingTopItems trendingTop = trendingTopList.get(position);
        holder.id.setText(""+trendingTop.getId());
        holder.name.setText(trendingTop.getName());
        holder.location.setText(trendingTop.getLocation());
        holder.time.setText(trendingTop.getTime());
        Glide.with(mContext)
                .load(trendingTop.getImage())
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return trendingTopList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id,name, location,time;
        public LinearLayout thumbnail;
        public ImageView favourites;
        public ImageView image;


        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.idvalue);
            name = (TextView) view.findViewById(R.id.name);
            location = (TextView) view.findViewById(R.id.location);
            time = (TextView) view.findViewById(R.id.time);
            thumbnail = (LinearLayout) view.findViewById(R.id.image_view);
            favourites = (ImageView) view.findViewById(R.id.favourites);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }


}
