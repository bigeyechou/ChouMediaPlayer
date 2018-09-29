package com.chou.android.choumediaplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.activity.VideoDetailActivity;
import com.chou.android.choumediaplayer.adapter.SupernatantAdapter;
import com.chou.android.choumediaplayer.adapter.VideoListAdapter;
import com.chou.android.choumediaplayer.datas.VideoListBean;
import com.chou.android.choumediaplayer.utils.GsonUtils;
import com.chou.android.mediaplayerlibrary.VideoPlayerManager;
import com.chou.android.network.subscribe.MovieSubscribe;
import com.chou.android.network.utils.OnSuccessAndFaultListener;
import com.chou.android.network.utils.OnSuccessAndFaultSub;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : zgz
 * @time :  2018/9/29 0029 15:40
 * @describe :悬浮、画中画视频
 **/
public class ListSupernatantFragment extends BaseFragment
    implements BaseQuickAdapter.RequestLoadMoreListener,
    SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.recycler_supernatant) RecyclerView recyclerSupernatant;
    @Bind(R.id.swipe_supernatant) SwipeRefreshLayout swipeSupernatant;
    private Context mContext;
    private SupernatantAdapter supAdapter;
    private List<VideoListBean.ListBean> videoList = new ArrayList<>();
    private int page = 0;
    private int isMore = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supernatant_layout, null);
        ButterKnife.bind(this, view);
        mContext = getContext();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getVideoList();
        initAdapter();
    }


    private void initAdapter() {
        supAdapter = new SupernatantAdapter(R.layout.item_supernatant_list, null);
        supAdapter.openLoadAnimation();
        supAdapter.setOnLoadMoreListener(this);
        swipeSupernatant.setOnRefreshListener(this);
        recyclerSupernatant.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerSupernatant.setAdapter(supAdapter);
        recyclerSupernatant.setFocusable(false);
        supAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                intent.putExtra("videoData", videoList.get(position));
                startActivity(intent);
            }
        });
    }


    /**
     * 视频列表
     */
    private void getVideoList() {
        OnSuccessAndFaultListener l = new OnSuccessAndFaultListener() {
            @Override public void onSuccess(String result) {
                VideoListBean videoListBean = GsonUtils.fromJson(result,
                    VideoListBean.class);
                isMore = videoListBean.getHas_more();
                videoList = videoListBean.getList();
                swipeSupernatant.setRefreshing(false);
                if (page == 0) {
                    supAdapter.setNewData(videoList);
                    supAdapter.setEnableLoadMore(true);
                } else {
                    supAdapter.addData(videoList);
                    supAdapter.loadMoreComplete();
                }
                supAdapter.notifyDataSetChanged();
            }


            @Override public void onFault(String errorMsg) {
                swipeSupernatant.setRefreshing(false);
            }
        };
        MovieSubscribe.getShowList(new OnSuccessAndFaultSub(l), page + "");
    }


    @Override public void onRefresh() {
        VideoPlayerManager.instance().releaseVideoPlayer();
        page = 0;
        getVideoList();
    }


    @Override public void onLoadMoreRequested() {
        page++;
        recyclerSupernatant.post(new Runnable() {
            @Override public void run() {
                if (isMore == 1) {
                    getVideoList();
                } else {
                    supAdapter.loadMoreEnd();
                }
            }
        });
    }


    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            VideoPlayerManager.instance().pauseVideoPlayer();
        }
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
