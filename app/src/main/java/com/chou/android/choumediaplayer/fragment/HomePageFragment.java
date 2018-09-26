package com.chou.android.choumediaplayer.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.fragment.BaseFragment;

/**
 * @author : zgz
 * @time :  2018/9/25 0025 17:34
 * @describe :首页
 **/
public class HomePageFragment extends BaseFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage_layout, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
