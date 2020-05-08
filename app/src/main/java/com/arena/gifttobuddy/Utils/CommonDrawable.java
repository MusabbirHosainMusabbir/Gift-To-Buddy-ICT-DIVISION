package com.arena.gifttobuddy.Utils;

import android.graphics.drawable.GradientDrawable;

public class CommonDrawable extends GradientDrawable {

    public CommonDrawable(int color){
        setColor(color);
        setShape(GradientDrawable.RECTANGLE);
        setCornerRadii(new float[]{30, 30, 30, 30, 30, 30, 30, 30 });
    }
}
