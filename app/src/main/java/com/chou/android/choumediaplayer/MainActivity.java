package com.chou.android.choumediaplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.VideoPlayerManager;
import com.chou.android.mediaplayerlibrary.controllers.AtVideoPlayerController;
import com.danikula.videocache.HttpProxyCacheServer;

import static com.chou.android.choumediaplayer.App.getProxy;

public class MainActivity extends AppCompatActivity implements AtVideoPlayerController.OnNoticeActivityListener{

    @Bind(R.id.video) ChouVideoPlayer video;
    private String videoPath = "http://play.g3proxy.lecloud.com/vod/v2/MjUxLzE2LzgvbGV0di11dHMvMTQvdmVyXzAwXzIyLTExMDc2NDEzODctYXZjLTE5OTgxOS1hYWMtNDgwMDAtNTI2MTEwLTE3MDg3NjEzLWY1OGY2YzM1NjkwZTA2ZGFmYjg2MTVlYzc5MjEyZjU4LTE0OTg1NTc2ODY4MjMubXA0?b=259&mmsid=65565355&tm=1499247143&key=f0eadb4f30c404d49ff8ebad673d3742&platid=3&splatid=345&playid=0&tss=no&vtype=21&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super";
    private String proxyPath;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        ButterKnife.bind(this);
        initVideo();
    }


    private void initVideo() {
        HttpProxyCacheServer proxy = getProxy(this);
        proxyPath = proxy.getProxyUrl(videoPath);
        video.isOpenGesture(true);
        AtVideoPlayerController controller = new AtVideoPlayerController(this);
        controller.setPathUrl(proxyPath);
        controller.setOnNoticeActivityListener(this);
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


    @Override public void onEventforATController(int eventType) {
        switch (eventType){
            case 1:
                finish();
                break;
            case 2:
                Toast.makeText(this,"举报~",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
