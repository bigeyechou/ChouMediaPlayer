package com.chou.android.choumediaplayer;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chou.android.choumediaplayer.adapter.DouYinAdapter;
import com.chou.android.choumediaplayer.datas.DataUtil;
import com.chou.android.choumediaplayer.datas.VideoBean;
import com.chou.android.choumediaplayer.utils.GsonUtils;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.VideoPlayerManager;
import com.chou.android.mediaplayerlibrary.controllers.BoxVideoPlayerController;
import com.chou.android.mediaplayerlibrary.controllers.DouYinVideoPlayerController;
import com.chou.android.network.bean.ShowVideoListBean;
import com.chou.android.network.subscribe.MovieSubscribe;
import com.chou.android.network.utils.OnSuccessAndFaultListener;
import com.chou.android.network.utils.OnSuccessAndFaultSub;
import com.danikula.videocache.HttpProxyCacheServer;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import java.util.ArrayList;
import java.util.List;

import static com.chou.android.choumediaplayer.app.App.getProxy;

public class DouYinActivity extends AppCompatActivity {

    @Bind(R.id.vp_douyin) VerticalViewPager douyinViewPage;
    private DouYinAdapter douYinAdapter;
    private List<View> views = new ArrayList<>();
    private LayoutInflater inflater = null;

    private List<ShowVideoListBean.ListBean> showListData = new ArrayList<>();

    private int mCurrentPosition;
    private int mPlayingPosition;
    private int page = 0;
    private int user_id = 32;

    private ChouVideoPlayer chouVideoPlayer;
    private DouYinVideoPlayerController controller;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douyin_layout);
        ButterKnife.bind(this);

        getShowList();

        setStatusBarTransparent();
    }


    private void initView(ShowVideoListBean data) {
        showListData = data.getList();
        for (ShowVideoListBean.ListBean item : showListData) {
            inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.douyin_item, null);
            chouVideoPlayer = view.findViewById(R.id.douyin_video);
            chouVideoPlayer.isOpenGesture(false);
            controller = new DouYinVideoPlayerController(this);
            // controller.setPathUrl(item.getVideo_href());
            controller.setDate(item.getVideo_cover(),item.getUser().getUsername(),item.getVideo_title());
            chouVideoPlayer.setController(controller);
            views.add(view);
        }

        douYinAdapter = new DouYinAdapter(views);
        douyinViewPage.setOffscreenPageLimit(3);
        douyinViewPage.setAdapter(douYinAdapter);

        douyinViewPage.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override public void onPageSelected(int position) {
                mCurrentPosition = position;
                chouVideoPlayer.pause();
                if (mCurrentPosition == showListData.size() - 1) {
                    Toast.makeText(DouYinActivity.this, "加载中，请稍后", Toast.LENGTH_SHORT).show();
                    getShowList();
                }
            }


            @Override public void onPageScrollStateChanged(int state) {
                if (mPlayingPosition == mCurrentPosition) {
                    return;
                }
                switch (state) {
                    case VerticalViewPager.SCROLL_STATE_IDLE:
                        startVideo();
                        break;
                    case VerticalViewPager.SCROLL_STATE_DRAGGING:
                        chouVideoPlayer.pause();
                        break;
                    case VerticalViewPager.SCROLL_STATE_SETTLING:
                        chouVideoPlayer.release();
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


    private void startVideo() {
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
            getWindow().setStatusBarColor(
                ContextCompat.getColor(this, android.R.color.transparent));
        }
    }


    /**
     * 秀场
     */
    private void getShowList() {
        OnSuccessAndFaultListener l = new OnSuccessAndFaultListener() {
            @Override public void onSuccess(String result) {
                ShowVideoListBean showVideoListBean = GsonUtils.fromJson(result,
                    ShowVideoListBean.class);

                if (showVideoListBean.getList() == null ||
                    showVideoListBean.getList().size() == 0) {
                    return;
                }

                if (page == 0) {
                    initView(showVideoListBean);
                } else {
                    showListData.addAll(showVideoListBean.getList());
                    views.clear();
                    for (ShowVideoListBean.ListBean item : showListData) {
                        View view = LayoutInflater.from(DouYinActivity.this)
                            .inflate(R.layout.douyin_item, null);
                        chouVideoPlayer = view.findViewById(R.id.douyin_video);
                        chouVideoPlayer.isOpenGesture(false);
                        controller = new DouYinVideoPlayerController(DouYinActivity.this);
                        controller.setPathUrl(item.getVideo_href());
                        controller.setDate(item.getVideo_cover(),item.getUser().getUsername(),item.getVideo_title());
                        chouVideoPlayer.setController(controller);
                        views.add(view);
                    }
                    douYinAdapter.setViews(views);
                    douYinAdapter.notifyDataSetChanged();
                }
                page++;
            }


            @Override public void onFault(String errorMsg) {

            }
        };
        MovieSubscribe.getShowList(new OnSuccessAndFaultSub(l), page + "", user_id + "");
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
