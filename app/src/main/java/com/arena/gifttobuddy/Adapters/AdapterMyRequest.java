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

import com.arena.gifttobuddy.Models.Items;
import com.arena.gifttobuddy.Models.MyRequest;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Views.RequestList;
import com.arena.gifttobuddy.customfonts.MyTextView_SF_Pro_Display_Regular;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterMyRequest extends RecyclerView.Adapter<AdapterMyRequest.ViewHolder> {
    private Context mContext;
    private List<MyRequest> itemList = new ArrayList<MyRequest>();

    public AdapterMyRequest(Context context, List<MyRequest> itemlist) {
        itemList = itemlist;
        mContext = context;
    }

    @NonNull
    @Override
    public AdapterMyRequest.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myrequest, parent, false);
        return new AdapterMyRequest.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMyRequest.ViewHolder holder, int position) {
        holder.name.setText(itemList.get(position).getItem());
        holder.location.setText(itemList.get(position).getLocation());
        holder.time.setText(itemList.get(position).getTime());

        String status = String.valueOf(itemList.get(position).getStatus());
        //Log.e("status", );
        if(status.equalsIgnoreCase("1")){
            holder.statusimage.setBackgroundResource(R.drawable.right);
            holder.statustext.setText("Congratulation your request has been accepted");
            holder.statustext.setTextColor(Color.parseColor("#27AE60"));
        }else{
            holder.statusimage.setBackgroundResource(R.drawable.cross);
            holder.statustext.setText("Sorry your request has been cancelled");
            holder.statustext.setTextColor(Color.parseColor("#EB5757"));
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

        ImageView image,statusimage;
        TextView name,location,time,statustext;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            statusimage = itemView.findViewById(R.id.statusbutton);
            location = itemView.findViewById(R.id.location);
            time = itemView.findViewById(R.id.time);
            statustext = itemView.findViewById(R.id.statustext);

        }
    }
}
