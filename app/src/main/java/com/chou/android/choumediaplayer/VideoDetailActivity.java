package com.chou.android.choumediaplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chou.android.choumediaplayer.utils.NetUtils;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.VideoPlayerManager;
import com.chou.android.mediaplayerlibrary.controllers.AtVideoPlayerController;
import com.danikula.videocache.HttpProxyCacheServer;

import static com.chou.android.choumediaplayer.app.App.getProxy;

public class VideoDetailActivity extends AppCompatActivity implements AtVideoPlayerController.OnVideoDetailListener{

    @Bind(R.id.video) ChouVideoPlayer video;
    private String videoPath = "http://aliyunvideo.wujike.com.cn/3b4aa75e3c1b4df9bd268b67a50bfdf6/9ab7363c6422430f9d1f58e7849b281d-2f3d59ee927c4f91d67789ed134d127d-sd.mp4";
    private String proxyPath;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail_layout);
        ButterKnife.bind(this);
        initVideo();

    }


    private void initVideo() {
        HttpProxyCacheServer proxy = getProxy(this);
        proxyPath = proxy.getProxyUrl(videoPath);
        video.isOpenGesture(true);
        AtVideoPlayerController controller = new AtVideoPlayerController(this);
        controller.setPathUrl(proxyPath);
        controller.setOnVideoDetailListener(this);
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


    @Override public void onVideoInform() {

    }


    @Override public void onVideoSaveCut(long startTime, long stopTime) {

    }
}
