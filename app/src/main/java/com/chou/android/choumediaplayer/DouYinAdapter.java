package com.chou.android.choumediaplayer;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.controllers.BoxVideoPlayerController;
import com.danikula.videocache.HttpProxyCacheServer;
import java.util.List;

import static com.chou.android.choumediaplayer.App.getProxy;

public class DouYinAdapter extends PagerAdapter {
    private Context context;
    private List<VideoBean> list;
    private String proxyPath;
    private ChouVideoPlayer videoview;
    private  BoxVideoPlayerController controller;

    public DouYinAdapter(Context context, List<VideoBean> list) {
        this.context = context;
        this.list = list;
    }





    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(context, R.layout.douyin_item, null);
        videoview = view.findViewById(R.id.video);
        HttpProxyCacheServer proxy = getProxy(context);
        proxyPath = proxy.getProxyUrl(list.get(position).getUrl());
        videoview.isOpenGesture(false);
        controller = new BoxVideoPlayerController(context);
        videoview.setController(controller);
        videoview.setVideoPath(proxyPath);
        videoview.start();
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        videoview.pause();
        container.removeView((View) object);
    }
}
