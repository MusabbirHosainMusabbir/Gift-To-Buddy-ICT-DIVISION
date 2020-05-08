package com.arena.gifttobuddy.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arena.gifttobuddy.Models.GetRequests;
import com.arena.gifttobuddy.Models.MyRequest;
import com.arena.gifttobuddy.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterGetRequest extends RecyclerView.Adapter<AdapterGetRequest.ViewHolder> {
    private Context mContext;
    private List<GetRequests> itemList = new ArrayList<GetRequests>();

    public AdapterGetRequest(Context context, List<GetRequests> itemlist) {
        itemList = itemlist;
        mContext = context;
        Log.e("itelistls", String.valueOf(itemlist.size()));
    }

    @NonNull
    @Override
    public AdapterGetRequest.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.getrequest, parent, false);
        return new AdapterGetRequest.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGetRequest.ViewHolder holder, int position) {
        holder.id.setText(""+itemList.get(position).getId());
        holder.name.setText(itemList.get(position).getName());
        holder.location.setText(itemList.get(position).getLocation());
        holder.time.setText(itemList.get(position).getTime());
        holder.requests.setText(""+itemList.get(position).getNumberofrequests()+"Requests");

        Glide.with(mContext)
                .load(itemList.get(position).getImage()) // image url
                //.placeholder(itemList.get(position).getIcon()) // any placeholder to load at start
                //.error()
                .into(holder.image);  // imageview object
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView id,name,location,time,requests;

        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.location);
            image = itemView.findViewById(R.id.image);
            time = itemView.findViewById(R.id.time);
            requests = itemView.findViewById(R.id.requests);

        }
    }
}
