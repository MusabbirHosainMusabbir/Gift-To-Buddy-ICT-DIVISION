package com.arena.gifttobuddy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arena.gifttobuddy.Models.Items;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.customfonts.MyTextView_SF_Pro_Display_Regular;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterProfileUser extends RecyclerView.Adapter<AdapterProfileUser.ViewHolder> {
    private Context mContext;
    private ArrayList<Items> itemList = new ArrayList<Items>();

    public AdapterProfileUser(Context context, ArrayList<Items> itemlist) {
        itemList = itemlist;
        mContext = context;
    }
    @NonNull
    @Override
    public AdapterProfileUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row, parent, false);
        return new AdapterProfileUser.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProfileUser.ViewHolder holder, int position) {
        holder.name.setText(itemList.get(position).getName());
        Glide.with(mContext)
                .load(itemList.get(position).getIcon()) // image url
                //.placeholder(itemList.get(position).getIcon()) // any placeholder to load at start
                //.error()
                .into(holder.logo);  // imageview object
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        MyTextView_SF_Pro_Display_Regular name;
        ImageView logo;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            logo = itemView.findViewById(R.id.logo);


        }
    }
}
