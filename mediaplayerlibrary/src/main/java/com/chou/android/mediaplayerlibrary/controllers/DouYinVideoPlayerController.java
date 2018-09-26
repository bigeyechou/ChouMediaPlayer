package com.chou.android.mediaplayerlibrary.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.OnVideoPlayerEventListener;
import com.chou.android.mediaplayerlibrary.R;
import com.chou.android.mediaplayerlibrary.VideoPlayerBaseController;
import com.chou.android.mediaplayerlibrary.datas.ShowVideoListBean;

public class DouYinVideoPlayerController extends VideoPlayerBaseController
    implements View.OnClickListener {

    private RelativeLayout dyRelTotal;
    private ImageView dyIvBackground;
    private LinearLayout dyVideoLoading;
    private TextView dyVideoLoadText;
    private LinearLayout dyVideoError;
    private TextView dyVideoRetry;
    private ImageView dyIvVideoShow;

    private Context mContext;
    private String videoUrl;


    public DouYinVideoPlayerController(
        @NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }


    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.douyin_video_player_controller, this, true);
        dyRelTotal = findViewById(R.id.dy_rel_total);
        dyIvBackground = findViewById(R.id.dy_iv_background);
        dyVideoLoading = findViewById(R.id.dy_video_loading);
        dyVideoLoadText = findViewById(R.id.dy_video_load_text);
        dyVideoError = findViewById(R.id.dy_video_error);
        dyVideoRetry = findViewById(R.id.dy_video_retry);
        dyIvVideoShow = findViewById(R.id.dy_iv_video_show);
        dyRelTotal.setOnClickListener(this);
        dyVideoRetry.setOnClickListener(this);
    }


    @Override
    public void setVideoPlayerView(OnVideoPlayerEventListener onVideoPlayerEventListener) {
        super.setVideoPlayerView(onVideoPlayerEventListener);
        // 给播放器配置视频链接地址
        if (videoUrl != null && !videoUrl.isEmpty()) {
            onVideoPlayerEventListener.setVideoPath(videoUrl);
        }
    }

    public void setData(ShowVideoListBean data){

    }

    public void setPathUrl(String pathUrl) {
        this.videoUrl = pathUrl;
        // 给播放器配置视频链接地址
        if (mOnVideoPlayerEventListener != null) {
            mOnVideoPlayerEventListener.setVideoPath(pathUrl);
        }
    }


    @Override public void onClick(View view) {
        if (view == dyVideoRetry) {
            mOnVideoPlayerEventListener.restart();
        }else if (view == dyRelTotal){
            if (mOnVideoPlayerEventListener.isPlaying()){
                mOnVideoPlayerEventListener.pause();
                dyIvVideoShow.setVisibility(VISIBLE);
            }else {
                mOnVideoPlayerEventListener.start();
                dyIvVideoShow.setVisibility(GONE);
            }
        }
    }


    @Override protected void onPlayStateChanged(int playState) {
        switch (playState) {
            case ChouVideoPlayer.STATE_IDLE:
                break;
            case ChouVideoPlayer.STATE_PREPARING:
                dyVideoError.setVisibility(View.GONE);
                dyVideoLoading.setVisibility(View.VISIBLE);
                dyVideoLoadText.setText("正在加载...");
                break;
            case ChouVideoPlayer.STATE_PREPARED:
                break;
            case ChouVideoPlayer.STATE_PLAYING:
                dyVideoLoading.setVisibility(View.GONE);
                dyIvBackground.setVisibility(View.GONE);
                break;
            case ChouVideoPlayer.STATE_PAUSED:
                dyVideoLoading.setVisibility(View.GONE);
                break;
            case ChouVideoPlayer.STATE_BUFFERING_PLAYING:
                dyVideoLoading.setVisibility(View.GONE);
                dyVideoLoadText.setText("正在缓冲...");
                break;
            case ChouVideoPlayer.STATE_BUFFERING_PAUSED:
                dyVideoLoading.setVisibility(View.GONE);
                dyVideoLoadText.setText("正在缓冲...");
                break;
            case ChouVideoPlayer.STATE_ERROR:
                dyVideoError.setVisibility(View.VISIBLE);
                break;
            case ChouVideoPlayer.STATE_COMPLETED:
                mOnVideoPlayerEventListener.restart();
                break;
        }
    }


    @Override protected void onPlayModeChanged(int playMode) {

    }


    @Override protected void reset() {
        dyVideoLoading.setVisibility(View.GONE);
        dyVideoError.setVisibility(View.GONE);
        dyIvBackground.setVisibility(View.VISIBLE);
    }


    @Override protected void updateProgress() {

    }


    @Override
    protected void showChangePosition(long duration, int newPositionProgress, boolean isShow) {

    }


    @Override protected void hideChangePosition() {

    }


    @Override protected void showChangeVolume(int newVolumeProgress) {

    }


    @Override protected void hideChangeVolume() {

    }


    @Override protected void showChangeBrightness(int newBrightnessProgress) {

    }


    @Override protected void hideChangeBrightness() {

    }
}
