package com.chou.android.choumediaplayer.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author : zgz
 * @time :  2018/9/25 0025 17:35
 * @describe :
 **/
public class BaseFragment extends Fragment {
    private Activity mActivity;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        restoreInstanceState(savedInstanceState);
        mActivity = getActivity();
    }


    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
    }


    /**
     * 让fragmnet横屏切换中保存实例不被重建
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    //加入全局跳转动画
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    /**
     * 显示键盘
     */
    @SuppressWarnings("static-access")
    protected void showSoftInput(EditText et) {
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(
            Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, 0);
    }


    /**
     * 隐藏键盘
     */
    @SuppressWarnings("static-access")
    protected void hideSoftInput(EditText et) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(
            Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
