package com.chou.android.choumediaplayer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.utils.NetUtils;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.VideoPlayerManager;
import com.chou.android.mediaplayerlibrary.controllers.BoxVideoPlayerController;
import com.danikula.videocache.HttpProxyCacheServer;

import static com.chou.android.choumediaplayer.app.App.getProxy;

public class VideoDanceBoxActivity extends AppCompatActivity implements BoxVideoPlayerController.OnDancerBoxListener {

    @Bind(R.id.video) ChouVideoPlayer video;
    private String videoPath = "http://aliyunvideo.wujike.com.cn/3b4aa75e3c1b4df9bd268b67a50bfdf6/9ab7363c6422430f9d1f58e7849b281d-2f3d59ee927c4f91d67789ed134d127d-sd.mp4";
    private String proxyPath;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_dance_layout);
        ButterKnife.bind(this);
        initVideo();
    }

    private void initVideo() {
        HttpProxyCacheServer proxy = getProxy(this);
        video.isOpenGesture(true);
        proxyPath = proxy.getProxyUrl(videoPath);
        BoxVideoPlayerController controller = new BoxVideoPlayerController(this);
        controller.setPathUrl(proxyPath);
        controller.setOnDancerBoxListener(this);
        video.setController(controller);
        NetUtils.setContext(this);
        if (NetUtils.isNetworkConnected()){
            if (NetUtils.isWiFiActive()){
                video.start();
            }else {
                //非WiFi
                video.start();
            }
        }else {
            //没有网络
            video.start();
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


    @Override public void onVideoBack() {
        finish();
    }


    @Override public void onVideoNext() {

    }


    @Override public void onVideoPrevious() {

    }


    /**
     * 列表模式的Video
     */
    public static class VideoListActivity extends AppCompatActivity{
        @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
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
}
