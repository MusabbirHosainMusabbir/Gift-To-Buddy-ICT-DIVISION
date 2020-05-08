package com.arena.gifttobuddy.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arena.gifttobuddy.Models.GetRequests;
import com.arena.gifttobuddy.Models.Items;
import com.arena.gifttobuddy.Models.PeopleRequest;
import com.arena.gifttobuddy.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterPeopleRequest extends RecyclerView.Adapter<AdapterPeopleRequest.ViewHolder> {
    private Context mContext;
    private List<PeopleRequest> itemList = new ArrayList<PeopleRequest>();

    public AdapterPeopleRequest(Context context, List<PeopleRequest> itemlist) {
        itemList = itemlist;
        mContext = context;
        Log.e("itelistls", String.valueOf(itemlist.size()));
    }

    @NonNull
    @Override
    public AdapterPeopleRequest.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list_item, parent, false);
        return new AdapterPeopleRequest.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPeopleRequest.ViewHolder holder, int position) {
        holder.id.setText(""+itemList.get(position).getId());
        holder.name.setText(itemList.get(position).getName());
        holder.location.setText(itemList.get(position).getLocation());
        holder.description.setText(itemList.get(position).getDescription());

        if(itemList.get(position).getStatus() == 1){
            holder.accept.setText("Request Accepted");
            holder.reject.setText("Reject");
        }else if(itemList.get(position).getStatus() == 2){
            holder.reject.setText("Request Rejected");
            holder.accept.setText("Accept");
        }else {

        }

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
        TextView id,name,location,description;
        Button accept,reject;

        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.location);
            image = itemView.findViewById(R.id.image);
            description = itemView.findViewById(R.id.description);

            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);

        }
    }
}
