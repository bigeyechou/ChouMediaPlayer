package com.chou.android.choumediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chou.android.choumediaplayer.activity.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.main_frame) FrameLayout mainFrame;
    @Bind(R.id.rb_tab_first) RadioButton rbTabFirst;
    @Bind(R.id.rb_tab_second) RadioButton rbTabSecond;
    @Bind(R.id.rg_only_course) RadioGroup rgOnlyCourse;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        ButterKnife.bind(this);
        initLayout();
    }


    @Override protected void initLayout() {
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {

        }
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

}
