package com.chou.android.choumediaplayer.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.activity.BaseActivity;
import com.chou.android.choumediaplayer.fragment.UserPageFragment;
import com.chou.android.choumediaplayer.fragment.HomePageFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.rb_tab_first) RadioButton rbTabFirst;
    @Bind(R.id.rb_tab_second) RadioButton rbTabSecond;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment mCurFragment;

    private long mBackPressedTime;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        ButterKnife.bind(this);
        initLayout();
    }

    private void initLayout() {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        rbTabFirst.setOnClickListener(this);
        rbTabSecond.setOnClickListener(this);
        switchCenter(HomePageFragment.class);
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_tab_first:
                switchCenter(HomePageFragment.class);
                break;
            case R.id.rb_tab_second:
                switchCenter(UserPageFragment.class);
                break;
            default:
                break;
        }
    }
    public void switchCenter(Class<? extends Fragment> clazz) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment userFragment = fm.findFragmentByTag(clazz.getName());
        if (userFragment == null) {
            try {
                userFragment = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (mCurFragment != null && mCurFragment != userFragment) {
            ft.hide(mCurFragment);
        }

        if (!userFragment.isAdded()) {
            ft.add(R.id.main_frame, userFragment, clazz.getName());
        } else {
            ft.show(userFragment);
        }
        ft.commitAllowingStateLoss();
        mCurFragment = userFragment;

    }

    @Override protected void onResume() {
        super.onResume();
    }


    @Override protected void onPause() {
        super.onPause();
    }


    @Override protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onBackPressed() {
        long curTime = SystemClock.uptimeMillis();
        if ((curTime - mBackPressedTime) < (3 * 1000)) {
            finish();
        } else {
            mBackPressedTime = curTime;
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        }

    }

}
