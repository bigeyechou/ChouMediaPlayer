package com.chou.android.choumediaplayer;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chou.android.choumediaplayer.adapter.DouYinAdapter;
import com.chou.android.choumediaplayer.datas.DataUtil;
import com.chou.android.choumediaplayer.datas.VideoBean;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.VideoPlayerManager;
import com.chou.android.mediaplayerlibrary.controllers.BoxVideoPlayerController;
import com.chou.android.mediaplayerlibrary.controllers.DouYinVideoPlayerController;
import com.danikula.videocache.HttpProxyCacheServer;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import java.util.ArrayList;
import java.util.List;

import static com.chou.android.choumediaplayer.app.App.getProxy;

public class DouYinActivity extends AppCompatActivity {

    @Bind(R.id.vp_douyin) VerticalViewPager douyinViewPage;
    private DouYinAdapter douYinAdapter;
    private List<VideoBean> mVideoList;
    private List<View> views = new ArrayList<>();
    private LayoutInflater inflater = null;

    private int mCurrentPosition;
    private int mPlayingPosition;
    private ChouVideoPlayer chouVideoPlayer;
    private DouYinVideoPlayerController controller;
    private String proxyPath ;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douyin_layout);
        ButterKnife.bind(this);

        HttpProxyCacheServer proxy = getProxy(this);

        mVideoList = DataUtil.getDouYinVideoList();
        for (VideoBean item : mVideoList) {
            inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.douyin_item, null);
            chouVideoPlayer = view.findViewById(R.id.douyin_video);
            chouVideoPlayer.isOpenGesture(false);
            controller = new DouYinVideoPlayerController(this);
            proxyPath = proxy.getProxyUrl(item.getUrl());
            controller.setPathUrl(proxyPath);
            controller.setImagePath(item.getThumb());
            chouVideoPlayer.setController(controller);
            views.add(view);
        }
        douYinAdapter = new DouYinAdapter(views);
        douyinViewPage.setOffscreenPageLimit(3);
        douyinViewPage.setAdapter(douYinAdapter);
        douyinViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                mCurrentPosition = position;
            }

            @Override public void onPageScrollStateChanged(int state) {
                if (mPlayingPosition == mCurrentPosition) {
                    return;
                }
                switch (state){
                    case VerticalViewPager.SCROLL_STATE_IDLE:
                        startVideo();
                        break;
                    case VerticalViewPager.SCROLL_STATE_DRAGGING:
                        chouVideoPlayer.pause();
                        break;
                    case VerticalViewPager.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
        douyinViewPage.post(new Runnable() {
            @Override public void run() {
                startVideo();
            }
        });
    }

    private void startVideo(){
        View view = views.get(mCurrentPosition);
        chouVideoPlayer = view.findViewById(R.id.douyin_video);
        chouVideoPlayer.start();
        mPlayingPosition = mCurrentPosition;
    }

    /**
     * 把状态栏设成透明
     */
    private void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = DouYinActivity.this.getWindow().getDecorView();
            decorView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    WindowInsets defaultInsets = view.onApplyWindowInsets(windowInsets);
                    return defaultInsets.replaceSystemWindowInsets(
                        defaultInsets.getSystemWindowInsetLeft(),
                        0,
                        defaultInsets.getSystemWindowInsetRight(),
                        defaultInsets.getSystemWindowInsetBottom());
                }
            });
            ViewCompat.requestApplyInsets(decorView);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }
    @Override protected void onResume() {
        super.onResume();
        VideoPlayerManager.instance().resumeVideoPlayer();
    }


    @Override protected void onPause() {
        super.onPause();
        VideoPlayerManager.instance().pauseVideoPlayer();
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        VideoPlayerManager.instance().releaseVideoPlayer();
    }
}
