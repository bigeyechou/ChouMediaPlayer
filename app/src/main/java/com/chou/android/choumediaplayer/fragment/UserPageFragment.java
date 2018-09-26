package com.chou.android.choumediaplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.fragment.BaseFragment;

/**
 * @author : zgz
 * @time :  2018/9/25 0025 17:32
 * @describe :我的页面
 **/
public class UserPageFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_userpage_layout, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}