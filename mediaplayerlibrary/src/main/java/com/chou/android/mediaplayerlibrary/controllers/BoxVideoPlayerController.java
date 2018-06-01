package com.chou.android.mediaplayerlibrary.controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.OnVideoPlayerEventListener;
import com.chou.android.mediaplayerlibrary.R;
import com.chou.android.mediaplayerlibrary.VideoPlayerBaseController;

/**
 *
 */
public class BoxVideoPlayerController extends VideoPlayerBaseController
    implements View.OnClickListener {

    private ImageView boxVideoBack;
    private LinearLayout boxVideoControl;
    private Button boxVideoMirror;
    private Button boxVideoSpeed;
    private LinearLayout boxVideoLoading;
    private TextView boxVideoLoadText;
    private LinearLayout boxVideoError;
    private TextView boxVideoRetry;
    private ImageView boxVideoPrevious;
    private ImageView boxVideoNext;
    private ImageView boxVideoFullScreen;

    private Context mContext;
    private String videoUrl;
    private boolean isSpeed = false;
    private boolean isMirror = true;

    private OnNoticeActivityListener onNoticeActivityListener;
    private static final int BACK_EVENT = 1;
    public interface OnNoticeActivityListener {
        /**
         * 交互的事件通知
         *
         * @param eventType 通知类型：1
         */
        void onEventforBoxController(int eventType, Bundle eventBundle);
    }

    public void setOnNoticeActivityListener(OnNoticeActivityListener listener) {
        this.onNoticeActivityListener = listener;
    }


    public BoxVideoPlayerController(
        @NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    @Override
    public void setVideoPlayerView(OnVideoPlayerEventListener onVideoPlayerEventListener) {
        super.setVideoPlayerView(onVideoPlayerEventListener);
        // 给播放器配置视频链接地址
        if (videoUrl != null && !videoUrl.isEmpty()) {
            onVideoPlayerEventListener.setVideoPath(videoUrl);
        }

    }

    public void setPathUrl(String pathUrl) {
        this.videoUrl = pathUrl;
        // 给播放器配置视频链接地址
        if (mOnVideoPlayerEventListener != null) {
            mOnVideoPlayerEventListener.setVideoPath(pathUrl);
        }
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.box_video_player_controller, this, true);
        boxVideoBack = findViewById(R.id.box_video_back);
        boxVideoControl = findViewById(R.id.box_video_control);
        boxVideoMirror = findViewById(R.id.box_video_mirror);
        boxVideoSpeed = findViewById(R.id.box_video_speed);
        boxVideoLoading = findViewById(R.id.box_video_loading);
        boxVideoLoadText = findViewById(R.id.box_video_load_text);
        boxVideoError = findViewById(R.id.box_video_error);
        boxVideoRetry = findViewById(R.id.box_video_retry);
        boxVideoPrevious = findViewById(R.id.box_video_previous);
        boxVideoNext = findViewById(R.id.box_video_next);
        boxVideoFullScreen = findViewById(R.id.box_video_full_screen);

        boxVideoBack.setOnClickListener(this);
        boxVideoMirror.setOnClickListener(this);
        boxVideoSpeed.setOnClickListener(this);
        boxVideoRetry.setOnClickListener(this);
        boxVideoPrevious.setOnClickListener(this);
        boxVideoNext.setOnClickListener(this);
        boxVideoFullScreen.setOnClickListener(this);
    }


    @Override public void onClick(View v) {
        if (v == boxVideoBack) {//返回
            if (mOnVideoPlayerEventListener.isFullScreen()) {
                mOnVideoPlayerEventListener.exitFullScreen();
            } else {
                if (null != onNoticeActivityListener) {
                    onNoticeActivityListener.onEventforBoxController(BACK_EVENT, null);
                }
            }
        } else if (v == boxVideoMirror) {//镜像
            isMirror = !isMirror;
            mOnVideoPlayerEventListener.setMirror(isMirror);
        } else if (v == boxVideoSpeed) {//调速
            isSpeed = !isSpeed;
            if (isSpeed){
                mOnVideoPlayerEventListener.setSpeed(0.5f);
                boxVideoSpeed.setText("慢速");
            }else {
                mOnVideoPlayerEventListener.setSpeed(1f);
                boxVideoSpeed.setText("原速");
            }
        } else if (v == boxVideoRetry) {//重试
            mOnVideoPlayerEventListener.restart();
        } else if (v == boxVideoPrevious) {//上一个

        } else if (v == boxVideoNext) {//下一个

        } else if (v == boxVideoFullScreen) {//宽屏
            if (mOnVideoPlayerEventListener.isNormal()) {
                mOnVideoPlayerEventListener.enterFullScreen();
            } else if (mOnVideoPlayerEventListener.isFullScreen()) {
                mOnVideoPlayerEventListener.exitFullScreen();
            }
        }
    }


    @Override protected void onPlayStateChanged(int playState) {
        switch (playState) {
            case ChouVideoPlayer.STATE_IDLE:
                break;
            case ChouVideoPlayer.STATE_PREPARING:

                boxVideoLoading.setVisibility(View.VISIBLE);
                boxVideoLoadText.setText("正在准备...");
                break;
            case ChouVideoPlayer.STATE_PREPARED:
                break;
            case ChouVideoPlayer.STATE_PLAYING:
                boxVideoLoading.setVisibility(View.GONE);
                break;
            case ChouVideoPlayer.STATE_PAUSED:
                boxVideoLoading.setVisibility(View.GONE);
                break;
            case ChouVideoPlayer.STATE_BUFFERING_PLAYING:
                boxVideoLoading.setVisibility(View.GONE);
                boxVideoLoadText.setText("正在缓冲...");
                break;
            case ChouVideoPlayer.STATE_BUFFERING_PAUSED:
                boxVideoLoading.setVisibility(View.GONE);
                boxVideoLoadText.setText("正在缓冲...");
                break;
            case ChouVideoPlayer.STATE_ERROR:
                boxVideoError.setVisibility(View.VISIBLE);
                break;
            case ChouVideoPlayer.STATE_COMPLETED:
                mOnVideoPlayerEventListener.restart();

                break;
        }
    }


    @Override protected void onPlayModeChanged(int playMode) {
        switch (playMode) {
            case ChouVideoPlayer.MODE_NORMAL:
                boxVideoControl.setVisibility(GONE);
                boxVideoFullScreen.setImageResource(R.mipmap.ic_video_enlarge);
                break;
            case ChouVideoPlayer.MODE_FULL_SCREEN:
                boxVideoControl.setVisibility(VISIBLE);
                boxVideoFullScreen.setImageResource(R.mipmap.ic_video_shrink);
                break;
        }
    }


    @Override protected void reset() {
        boxVideoFullScreen.setImageResource(R.mipmap.ic_video_enlarge);
        boxVideoBack.setVisibility(View.VISIBLE);
        boxVideoLoading.setVisibility(View.GONE);
        boxVideoError.setVisibility(View.GONE);
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
