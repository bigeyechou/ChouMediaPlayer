package com.chou.android.mediaplayerlibrary.utils;

import android.content.Context;

public class ConvertUtils {

    public static int dp2px(Context context, float dip) {
        return (int) (context.getResources().getDisplayMetrics().density * dip);
    }

}
