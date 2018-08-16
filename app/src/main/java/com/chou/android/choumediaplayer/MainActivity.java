package com.chou.android.choumediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.btn_detail) Button btnDetail;
    @Bind(R.id.btn_box) Button btnBox;
    @Bind(R.id.btn_circle) Button btnCircle;
    @Bind(R.id.btn_douyin) Button btnDouyin;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        ButterKnife.bind(this);
        initLayout();
    }


    private void initLayout() {
        btnDetail.setOnClickListener(this);
        btnBox.setOnClickListener(this);
        btnCircle.setOnClickListener(this);
        btnDouyin.setOnClickListener(this);
    }


    @Override public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_detail:
                startActivity(new Intent(MainActivity.this,VideoDetailActivity.class));
                break;
            case R.id.btn_box:
                startActivity(new Intent(MainActivity.this,VideoDanceBoxActivity.class));
                break;
            case R.id.btn_circle:
                break;
            case R.id.btn_douyin:
                startActivity(new Intent(MainActivity.this,DouYinActivity.class));
                break;

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
