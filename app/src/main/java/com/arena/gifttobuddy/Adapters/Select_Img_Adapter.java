package com.arena.gifttobuddy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arena.gifttobuddy.R;

import androidx.viewpager.widget.PagerAdapter;


/**
 * Created by PROSEN on 7/16/2016.
 */
public class Select_Img_Adapter extends PagerAdapter {
    private int[] imgs = {R.drawable.bg,R.drawable.bg,R.drawable.bg};
    private LayoutInflater inflater;
    private Context ctx;

    public Select_Img_Adapter(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view ==(LinearLayout)object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater =(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.swipe,container,false);
        ImageView img = (ImageView)v.findViewById(R.id.imageView);

        img.setImageResource(imgs[position]);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
