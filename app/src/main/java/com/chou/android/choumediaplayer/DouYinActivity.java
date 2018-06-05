package com.chou.android.choumediaplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chou.android.choumediaplayer.adapter.DouYinAdapter;
import com.chou.android.choumediaplayer.datas.DataUtil;
import com.chou.android.choumediaplayer.datas.VideoBean;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import java.util.List;

public class DouYinActivity extends AppCompatActivity {

    @Bind(R.id.douyin_vvp) VerticalViewPager douyinVvp;
    private DouYinAdapter douYinAdapter;
    private List<VideoBean> mVideoList;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douyin_layout);
        ButterKnife.bind(this);

        mVideoList = DataUtil.getDouYinVideoList();
        douYinAdapter = new DouYinAdapter(this,mVideoList);
        douyinVvp.setAdapter(douYinAdapter);
        douyinVvp.setOffscreenPageLimit(1);

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
