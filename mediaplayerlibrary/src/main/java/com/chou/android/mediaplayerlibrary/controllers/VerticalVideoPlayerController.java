package com.chou.android.mediaplayerlibrary.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.OnVideoPlayerEventListener;
import com.chou.android.mediaplayerlibrary.R;
import com.chou.android.mediaplayerlibrary.VideoPlayerBaseController;

/**
 * 竖直播放样式
 */
public class VerticalVideoPlayerController extends VideoPlayerBaseController
    implements View.OnClickListener {

    private RelativeLayout mRelTotal;
    private ImageView mIvBackground;
    private LinearLayout mVideoLoading;
    private TextView mVideoLoadText;
    private LinearLayout mVideoError;
    private TextView mVideoRetry;
    private ImageView mIvVideoShow;
    private TextView tvLook,tvCollect,tvLike;
    private TextView tvName,tvDescription;

    private Context mContext;
    private String videoUrl;


    public VerticalVideoPlayerController(
        @NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }


    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.vertical_video_player_controller, this, true);
        mRelTotal = findViewById(R.id.rel_total);
        mIvBackground = findViewById(R.id.iv_background);
        mVideoLoading = findViewById(R.id.video_loading);
        mVideoLoadText = findViewById(R.id.video_load_text);
        mVideoError = findViewById(R.id.video_error);
        mVideoRetry = findViewById(R.id.video_retry);
        mIvVideoShow = findViewById(R.id.iv_video_show);
        mRelTotal.setOnClickListener(this);
        mVideoRetry.setOnClickListener(this);
    }


    @Override
    public void setVideoPlayerView(OnVideoPlayerEventListener onVideoPlayerEventListener) {
        super.setVideoPlayerView(onVideoPlayerEventListener);
        // 给播放器配置视频链接地址
        if (videoUrl != null && !videoUrl.isEmpty()) {
            onVideoPlayerEventListener.setVideoPath(videoUrl);
        }
    }

    public void setData(String name,String title,String look,String like,String collect){
        tvLike = findViewById(R.id.tv_like_video_douyin);
        tvLook = findViewById(R.id.tv_look_video_douyin);
        tvCollect = findViewById(R.id.tv_collect_video_douyin);
        tvName = findViewById(R.id.tv_name_video_douyin);
        tvDescription = findViewById(R.id.tv_description_video_douyin);

        tvName.setText(name);
        tvLook.setText(look);
        tvCollect.setText(collect);
        tvLike.setText(like);
        tvDescription.setText(title);
    }

    public void setPathUrl(String pathUrl) {
        this.videoUrl = pathUrl;
        // 给播放器配置视频链接地址
        if (mOnVideoPlayerEventListener != null) {
            mOnVideoPlayerEventListener.setVideoPath(pathUrl);
        }
    }


    @Override public void onClick(View view) {
        if (view == mVideoRetry) {
            mOnVideoPlayerEventListener.restart();
        }else if (view == mRelTotal){
            if (mOnVideoPlayerEventListener.isPlaying()){
                mOnVideoPlayerEventListener.pause();
                mIvVideoShow.setVisibility(VISIBLE);
            }else {
                mOnVideoPlayerEventListener.start();
                mIvVideoShow.setVisibility(GONE);
            }
        }
    }


    @Override protected void onPlayStateChanged(int playState) {
        switch (playState) {
            case ChouVideoPlayer.STATE_IDLE:
                break;
            case ChouVideoPlayer.STATE_PREPARING:
                mVideoError.setVisibility(View.GONE);
                mVideoLoading.setVisibility(View.VISIBLE);
                mVideoLoadText.setText("正在加载...");
                break;
            case ChouVideoPlayer.STATE_PREPARED:
                break;
            case ChouVideoPlayer.STATE_PLAYING:
                mVideoLoading.setVisibility(View.GONE);
                mIvBackground.setVisibility(View.GONE);
                break;
            case ChouVideoPlayer.STATE_PAUSED:
                mVideoLoading.setVisibility(View.GONE);
                break;
            case ChouVideoPlayer.STATE_BUFFERING_PLAYING:
                mVideoLoading.setVisibility(View.GONE);
                mVideoLoadText.setText("正在缓冲...");
                break;
            case ChouVideoPlayer.STATE_BUFFERING_PAUSED:
                mVideoLoading.setVisibility(View.GONE);
                mVideoLoadText.setText("正在缓冲...");
                break;
            case ChouVideoPlayer.STATE_ERROR:
                mVideoError.setVisibility(View.VISIBLE);
                break;
            case ChouVideoPlayer.STATE_COMPLETED:
                mOnVideoPlayerEventListener.restart();
                break;
        }
    }


    @Override protected void onPlayModeChanged(int playMode) {

    }


    @Override protected void reset() {
        mVideoLoading.setVisibility(View.GONE);
        mVideoError.setVisibility(View.GONE);
        mIvBackground.setVisibility(View.VISIBLE);
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
