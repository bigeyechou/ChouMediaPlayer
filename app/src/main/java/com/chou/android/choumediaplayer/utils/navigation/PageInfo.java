package com.chou.android.choumediaplayer.utils.navigation;

import android.os.Bundle;

/**
 * @author : zgz
 * @time :  2018/9/26 0026 17:00
 * @describe :
 **/
public class PageInfo {
    public String title;
    public Class<?> clx;
    public Bundle args;


    public PageInfo(String title, Class<?> clx, Bundle args) {
        this.title = title;
        this.clx = clx;
        this.args = args;
    }
}
