package com.chou.android.choumediaplayer.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.adapter.VideoListAdapter;
import com.chou.android.choumediaplayer.datas.ShowVideoListBean;
import com.chou.android.choumediaplayer.utils.GsonUtils;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.VideoPlayerManager;
import com.chou.android.network.subscribe.MovieSubscribe;
import com.chou.android.network.utils.OnSuccessAndFaultListener;
import com.chou.android.network.utils.OnSuccessAndFaultSub;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * @author : zgz
 * @time :  2018/9/26 0026 17:02
 * @describe :
 **/
public class ListVideoFragment extends BaseFragment
    implements BaseQuickAdapter.RequestLoadMoreListener,
    SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.recycler_video_list) RecyclerView recyclerVideoList;
    @Bind(R.id.swipe_video_list) SwipeRefreshLayout swipeVideoList;

    private Context mContext;
    private VideoListAdapter videoAdapter;
    private List<ShowVideoListBean.ListBean> videoList = new ArrayList<>();
    private int page = 0;
    private int isMore = 0;
    private int user_id = 32;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list_layout, null);
        ButterKnife.bind(this, view);
        mContext = mContext;
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        intData();
    }


    private void intData() {
        getShowList();
    }


    private void initView() {
        videoAdapter = new VideoListAdapter(null);
        videoAdapter.openLoadAnimation();
        videoAdapter.setOnLoadMoreListener(this);
        final LinearLayoutManager linearLayoutManagerVideo = new LinearLayoutManager(mContext);
        swipeVideoList.setOnRefreshListener(this);
        recyclerVideoList.setLayoutManager(linearLayoutManagerVideo);
        recyclerVideoList.setAdapter(videoAdapter);
        recyclerVideoList.setFocusable(false);
        recyclerVideoList.addOnChildAttachStateChangeListener(
            new RecyclerView.OnChildAttachStateChangeListener() {
                @Override public void onChildViewAttachedToWindow(View view) {

                }


                @Override public void onChildViewDetachedFromWindow(View view) {
                    ChouVideoPlayer videoPlayer = view.findViewById(R.id.video_video_list);
                    if (videoPlayer != null) {
                        videoPlayer.pause();
                    }
                }
            });
        recyclerVideoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int firstVisibleItem, lastVisibleItem, visibleCount;


            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case SCROLL_STATE_IDLE: //滚动停止
                        autoPlayVideo(recyclerView);
                        break;
                    default:
                        break;
                }
            }


            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManagerVideo.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManagerVideo.findLastVisibleItemPosition();
                visibleCount = lastVisibleItem - firstVisibleItem;//记录可视区域item个数
            }


            private void autoPlayVideo(RecyclerView view) {
                //循环遍历可视区域videoview,如果完全可见就开始播放
                for (int i = 0; i < visibleCount; i++) {
                    if (view == null || view.getChildAt(i) == null) {
                        continue;
                    }
                    ChouVideoPlayer videoPlayer = view.getChildAt(i)
                        .findViewById(R.id.video_video_list);
                    ImageView btnPlay = view.getChildAt(i).findViewById(R.id.iv_start_video_list);
                    if (videoPlayer != null) {
                        Rect rect = new Rect();
                        videoPlayer.getLocalVisibleRect(rect);
                        int videoHeight = videoPlayer.getHeight();
                        if (rect.top == 0 && rect.bottom == videoHeight) {
                            videoPlayer.start();
                            btnPlay.setVisibility(View.GONE);

                            return;
                        }
                    }
                }
            }
        });

        /**
         * 画出播放item暂停
         */
        recyclerVideoList.addOnChildAttachStateChangeListener(
            new RecyclerView.OnChildAttachStateChangeListener() {
                @Override public void onChildViewAttachedToWindow(View view) {

                }


                @Override public void onChildViewDetachedFromWindow(View view) {
                    VideoPlayerManager.instance().pauseVideoPlayer();
                }
            });

    }


    /**
     * 秀场
     */
    private void getShowList() {
        OnSuccessAndFaultListener l = new OnSuccessAndFaultListener() {
            @Override public void onSuccess(String result) {
                ShowVideoListBean showVideoListBean =  GsonUtils.fromJson(result,
                    ShowVideoListBean.class);
                isMore = showVideoListBean.getHas_more();
                videoList = showVideoListBean.getList();
                swipeVideoList.setRefreshing(false);
                if (page == 0) {
                    videoAdapter.setNewData(videoList);
                    videoAdapter.setEnableLoadMore(true);
                } else {
                    videoAdapter.addData(videoList);
                    videoAdapter.loadMoreComplete();
                }
                videoAdapter.notifyDataSetChanged();
            }


            @Override public void onFault(String errorMsg) {
                swipeVideoList.setRefreshing(false);
            }
        };
        MovieSubscribe.getShowList(new OnSuccessAndFaultSub(l), page + "", user_id + "");
    }


    @Override public void onRefresh() {
        page = 0;
        getShowList();
        swipeVideoList.setRefreshing(false);
    }


    @Override public void onLoadMoreRequested() {
        page++;
        recyclerVideoList.post(new Runnable() {
            @Override public void run() {
                if (isMore == 1) {
                    getShowList();
                } else {
                    videoAdapter.loadMoreEnd();
                }
            }
        });
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
        ButterKnife.unbind(this);
        VideoPlayerManager.instance().releaseVideoPlayer();
    }
}
