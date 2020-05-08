package com.arena.gifttobuddy.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arena.gifttobuddy.Models.CustomRecyclerViewItem;
import com.arena.gifttobuddy.Models.Items;
import com.arena.gifttobuddy.R;
import com.arena.gifttobuddy.Utils.CommonDrawable;
import com.arena.gifttobuddy.Views.Categories;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CustomViewAdapter extends RecyclerView.Adapter<CustomViewAdapter.CustomRecyclerViewHolder> {

    private List<Items> viewItemList;
    CommonDrawable commonDrawable;
    Context context;

    public CustomViewAdapter(Context context,List<Items> viewItemList) {
        this.context = context;
        this.viewItemList = viewItemList;
    }

    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get LayoutInflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        View itemView = layoutInflater.inflate(R.layout.activity_custom_refresh_recycler_view_item, parent, false);

        // Create and return our customRecycler View Holder object.
        CustomRecyclerViewHolder ret = new CustomRecyclerViewHolder(itemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewHolder holder, int position) {
        if(viewItemList!=null) {
            // Get car item dto in list.
            String buttoncolor = String.valueOf(Color.parseColor("#1AFB6D3A"));
            Items viewItem = viewItemList.get(position);
            //commonDrawable = new CommonDrawable(Integer.parseInt(buttoncolor));

            if(viewItem != null) {
                // Set car item title.
                holder.textView.setText(viewItem.getName());
                Glide.with(context)
                        .load(viewItem.getIcon()) // image url
                        //.placeholder(itemList.get(position).getIcon()) // any placeholder to load at start
                        //.error()
                        .into(holder.imageView);

            }
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if(viewItemList!=null)
        {
            ret = viewItemList.size();
        }
        return ret;
    }

    public class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView textView = null;
        private LinearLayout linearLayout;
        ImageView imageView;

        public CustomRecyclerViewHolder(View itemView) {
            super(itemView);

            if(itemView != null)
            {
                imageView = itemView.findViewById(R.id.image);
                textView = (TextView)itemView.findViewById(R.id.custom_refresh_recycler_view_text_view);
                linearLayout = itemView.findViewById(R.id.linear_layout);
            }
        }

        public TextView getTextView() {
            return textView;
        }
    }
}