package com.chou.android.choumediaplayer.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.datas.VideoBean;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.controllers.BoxVideoPlayerController;
import com.danikula.videocache.HttpProxyCacheServer;
import java.util.List;

import static com.chou.android.choumediaplayer.app.App.getProxy;

public class DouYinAdapter extends PagerAdapter {
    private List<View> mViews;

    public DouYinAdapter(List<View> views) {
        this.mViews = views;
    }

    public void setViews(List<View> mViews) {
        this.mViews = mViews;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // container.removeView((View) object);
        container.removeView(mViews.get(position));
    }
}
