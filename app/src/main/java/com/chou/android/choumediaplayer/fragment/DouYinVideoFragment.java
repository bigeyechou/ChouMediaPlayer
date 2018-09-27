package com.chou.android.choumediaplayer.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.adapter.DouYinAdapter;
import com.chou.android.choumediaplayer.datas.ShowVideoListBean;
import com.chou.android.choumediaplayer.utils.GsonUtils;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.VideoPlayerManager;
import com.chou.android.mediaplayerlibrary.controllers.DouYinVideoPlayerController;
import com.chou.android.network.subscribe.MovieSubscribe;
import com.chou.android.network.utils.OnSuccessAndFaultListener;
import com.chou.android.network.utils.OnSuccessAndFaultSub;
import com.facebook.drawee.view.SimpleDraweeView;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : zgz
 * @time :  2018/9/26 0026 17:21
 * @describe :抖音页面
 **/
public class DouYinVideoFragment extends BaseFragment {
    @Bind(R.id.vp_douyin) VerticalViewPager douyinViewPage;
    private TextView tvLook,tvCollect,tvLike;
    private TextView tvName,tvDescription;
    private SimpleDraweeView headImage;

    private DouYinAdapter douYinAdapter;
    private List<View> views = new ArrayList<>();
    private LayoutInflater inflater = null;


    private List<ShowVideoListBean.ListBean> showListData = new ArrayList<>();

    private Context mContext;
    private int mCurrentPosition;
    private int mPlayingPosition;
    private int page = 0;
    private int user_id = 32;

    private ChouVideoPlayer chouVideoPlayer;
    private DouYinVideoPlayerController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_douyin_layout, null);
        ButterKnife.bind(this, view);
        mContext = getContext();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getShowList();

        // setStatusBarTransparent();
    }

    private void initView(ShowVideoListBean data) {
        showListData = data.getList();
        for (ShowVideoListBean.ListBean item : showListData) {
            inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.douyin_item, null);
            chouVideoPlayer = view.findViewById(R.id.douyin_video);
            chouVideoPlayer.isOpenGesture(false);
            controller = new DouYinVideoPlayerController(mContext);
            controller.setPathUrl(item.getVideo_href());
            controller.setData(item.getUser().getUsername(),item.getVideo_title(),item.getVideo_play_nums(),item.getVideo_like_nums(),item.getVideo_collect_nums());
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
                    Toast.makeText(mContext, "加载中，请稍后", Toast.LENGTH_SHORT).show();
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

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            VideoPlayerManager.instance().pauseVideoPlayer();
        }
    }

    /**
     * 把状态栏设成透明
     */
    private void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getActivity().getWindow().getDecorView();
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
            getActivity().getWindow().setStatusBarColor(
                ContextCompat.getColor(mContext, android.R.color.transparent));
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
                        View view = LayoutInflater.from(mContext)
                            .inflate(R.layout.douyin_item, null);
                        chouVideoPlayer = view.findViewById(R.id.douyin_video);
                        chouVideoPlayer.isOpenGesture(false);
                        controller = new DouYinVideoPlayerController(mContext);
                        controller.setPathUrl(item.getVideo_href());
                        controller.setData(item.getUser().getUsername(),item.getVideo_title(),item.getVideo_play_nums(),item.getVideo_like_nums(),item.getVideo_collect_nums());
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


    @Override public void onResume() {
        super.onResume();
        VideoPlayerManager.instance().resumeVideoPlayer();
    }


    @Override public void onPause() {
        super.onPause();
        VideoPlayerManager.instance().pauseVideoPlayer();
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(mContext);
        VideoPlayerManager.instance().releaseVideoPlayer();
    }
}
