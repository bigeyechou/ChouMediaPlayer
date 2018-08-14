package com.chou.android.choumediaplayer;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowInsets;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chou.android.choumediaplayer.adapter.DouYinAdapter;
import com.chou.android.choumediaplayer.datas.DataUtil;
import com.chou.android.choumediaplayer.datas.VideoBean;
import com.chou.android.choumediaplayer.utils.GsonUtils;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.VideoPlayerManager;
import com.chou.android.mediaplayerlibrary.controllers.AtVideoPlayerController;
import com.chou.android.network.bean.ShowVideoListBean;
import com.chou.android.network.subscribe.MovieSubscribe;
import com.chou.android.network.utils.OnSuccessAndFaultListener;
import com.chou.android.network.utils.OnSuccessAndFaultSub;
import com.danikula.videocache.HttpProxyCacheServer;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import java.util.ArrayList;
import java.util.List;

import static com.chou.android.choumediaplayer.app.App.getProxy;

public class DetailActivity extends AppCompatActivity {
    private int start = 0;
    private int count = 20;

    private List<ShowVideoListBean.ListBean> showListData = new ArrayList<>();

    @Bind(R.id.vp_douyin) VerticalViewPager douyinViewPage;
    private DouYinAdapter douYinAdapter;
    private List<VideoBean> mVideoList;
    private List<View> views = new ArrayList<>();
    private LayoutInflater inflater = null;

    private int mCurrentPosition;
    private int mPlayingPosition;
    private ChouVideoPlayer chouVideoPlayer;
    private AtVideoPlayerController controller;
    private String proxyPath ;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douyin_layout);
        ButterKnife.bind(this);

        HttpProxyCacheServer proxy = getProxy(this);
        setStatusBarTransparent();
        mVideoList = DataUtil.getDouYinVideoList();
        for (VideoBean item : mVideoList) {
            inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.aitiao_item, null);
            chouVideoPlayer = view.findViewById(R.id.douyin_video);
            chouVideoPlayer.isOpenGesture(false);
            controller = new AtVideoPlayerController(this);
            proxyPath = proxy.getProxyUrl(item.getUrl());
            controller.setPathUrl(proxyPath);
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
            View decorView = DetailActivity.this.getWindow().getDecorView();
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

    /**
     * 秀场
     */
    private void getShowList() {
        OnSuccessAndFaultListener l = new OnSuccessAndFaultListener() {
            @Override public void onSuccess(String result) {
                ShowVideoListBean showVideoListBean = GsonUtils.fromJson(result,ShowVideoListBean.class);
                showListData = showVideoListBean.getList();

            }


            @Override public void onFault(String errorMsg) {

            }
        };
        MovieSubscribe.getShowList(new OnSuccessAndFaultSub(l,this,true),0,1);
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
