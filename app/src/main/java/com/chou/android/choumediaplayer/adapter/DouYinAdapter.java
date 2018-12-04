package com.chou.android.choumediaplayer.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

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
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }
}
