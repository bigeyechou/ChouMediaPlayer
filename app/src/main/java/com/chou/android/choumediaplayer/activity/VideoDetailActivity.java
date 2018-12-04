package com.chou.android.choumediaplayer.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.datas.VideoListBean;
import com.chou.android.choumediaplayer.utils.NetUtils;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.VideoPlayerBaseController;
import com.chou.android.mediaplayerlibrary.VideoPlayerManager;
import com.chou.android.mediaplayerlibrary.controllers.CommonVideoPlayerController;
import com.chou.android.mediaplayerlibrary.controllers.TinyVideoPlayerController;
import com.chou.android.mediaplayerlibrary.utils.FloatView;
import com.danikula.videocache.HttpProxyCacheServer;
import com.facebook.drawee.view.SimpleDraweeView;

import static com.chou.android.choumediaplayer.app.App.getProxy;

public class VideoDetailActivity extends AppCompatActivity
    implements CommonVideoPlayerController.OnVideoDetailListener {

    @Bind(R.id.video_detail) ChouVideoPlayer videoDetail;
    @Bind(R.id.iv_bg_video_detail) SimpleDraweeView ivBgVideoDetail;
    @Bind(R.id.iv_start_video_detail) ImageView ivStartVideoDetail;
    @Bind(R.id.tv_description_video_detail) TextView tvDescriptionVideoDetail;
    @Bind(R.id.iv_head_detail) SimpleDraweeView ivHeadDetail;
    @Bind(R.id.tv_name_video_detail) TextView tvNameVideoDetail;
    private String videoPath;
    private String proxyPath;
    private VideoListBean.ListBean videoData = new VideoListBean.ListBean();

    private FloatView floatView;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail_layout);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            videoData = (VideoListBean.ListBean) getIntent().getSerializableExtra("videoData");
        }
        initLayout();
        initVideo();
        floatView = new FloatView(this, 0, 0);
    }


    private void initLayout() {
        videoPath = videoData.getVideo_href();
        ivHeadDetail.setImageURI(Uri.parse(videoData.getUser().getIcon()));
        tvNameVideoDetail.setText(videoData.getUser().getUsername());
        tvDescriptionVideoDetail.setText(videoData.getVideo_title());
        ivBgVideoDetail.setImageURI(Uri.parse(videoData.getVideo_cover()));
        ivBgVideoDetail.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (videoPath!=null){
                    videoDetail.start();
                    ivStartVideoDetail.setVisibility(View.GONE);
                    ivBgVideoDetail.setVisibility(View.GONE);
                }
            }
        });
        ivHeadDetail.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                enterTinyWindow();
            }
        });
    }


    private void initVideo() {
        HttpProxyCacheServer proxy = getProxy(this);
        proxyPath = proxy.getProxyUrl(videoPath);
        videoDetail.isOpenGesture(true);
        CommonVideoPlayerController controller = new CommonVideoPlayerController(this);
        controller.setPathUrl(proxyPath);
        controller.setOnVideoDetailListener(this);
        videoDetail.setController(controller);
        NetUtils.setContext(this);
        if (NetUtils.isNetworkConnected()) {
            if (NetUtils.isWiFiActive()) {
                videoDetail.start();
                ivStartVideoDetail.setVisibility(View.GONE);
                ivBgVideoDetail.setVisibility(View.GONE);
            } else {
                //非WiFi
                ivStartVideoDetail.setVisibility(View.VISIBLE);
                ivBgVideoDetail.setVisibility(View.VISIBLE);
            }
        } else {
            //没有网络
            ivStartVideoDetail.setVisibility(View.VISIBLE);
            ivBgVideoDetail.setVisibility(View.VISIBLE);
        }
    }

    public void enterTinyWindow() {
        if (videoDetail.isIdle()) {
            Toast.makeText(this, "要点击播放后才能进入小窗口", Toast.LENGTH_SHORT).show();
        } else {
            TinyVideoPlayerController controller = new TinyVideoPlayerController(this);
            controller.setPathUrl(proxyPath);
            videoDetail.setController(controller);
            floatView.addView(controller);
            floatView.addToWindow();
            videoDetail.enterTinyWindow();
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

    @Override public void onVideoChange(boolean isNormal) {

    }
}
