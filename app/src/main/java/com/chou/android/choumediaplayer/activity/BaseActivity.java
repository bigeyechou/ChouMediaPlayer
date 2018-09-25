package com.chou.android.choumediaplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.chou.android.choumediaplayer.app.App;

/**
 * @author : zgz
 * @time :  2018/9/25 0025 16:47
 * @describe :
 **/
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.addActivity(this);

        initLayout();
    }
    /**
     * 初始化布局
     */
    protected abstract void initLayout();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void finish() {
        super.finish();
        hideKeyboard();
        System.gc();
        System.gc();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
            && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return false;
    }
    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        if (null != this.getCurrentFocus()) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(this.getCurrentFocus()
                        .getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示键盘
     */
    @SuppressWarnings("static-access")
    protected void showSoftInput(Context context, EditText et) {
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, 0);

    }

    /**
     * 隐藏键盘
     */
    @SuppressWarnings("static-access")
    protected void hideSoftInput(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 简单startActivity方法 直接 给要跳转的Activity就行
     *
     * @param calss
     */
    public void startActivity(Class<? extends BaseActivity> calss) {
        startActivity(new Intent(this, calss));
    }
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }
}
