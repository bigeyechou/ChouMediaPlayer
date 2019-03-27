package com.chou.android.mediaplayerlibrary.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.OnVideoPlayerEventListener;
import com.chou.android.mediaplayerlibrary.R;
import com.chou.android.mediaplayerlibrary.VideoPlayerBaseController;
import com.chou.android.mediaplayerlibrary.utils.ChouPlayerUtil;

/**
 * 列表播放器
 */
public class ListVideoPlayerController extends VideoPlayerBaseController
    implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private LinearLayout videoLoading;
    private LinearLayout videoError;
    private TextView videoRetry;
    private SeekBar videoSeek;
    private ImageView volumeControl;

    private Context mContext;
    private String videoUrl;

    //音量控制
    private Boolean volumeType;

    private ListVideoPlayerController.OnFollowListener onFollowListener;

    public interface OnFollowListener {
        void pause();
        void start();
        void reset();
    }


    public void setOnFollowListener(ListVideoPlayerController.OnFollowListener listener) {
        this.onFollowListener = listener;
    }


    public ListVideoPlayerController(
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


    public void setUrl(String pathUrl) {
        this.videoUrl = pathUrl;
        // 给播放器配置视频链接地址
        if (mOnVideoPlayerEventListener != null) {
            mOnVideoPlayerEventListener.setVideoPath(pathUrl);
        }
    }


    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.list_video_player_controller, this, true);
        videoLoading = (LinearLayout) findViewById(R.id.focus_video_loading);
        videoError = (LinearLayout) findViewById(R.id.focus_video_error);
        videoRetry = (TextView) findViewById(R.id.focus_video_retry);
        videoSeek = (SeekBar) findViewById(R.id.focus_video_seek);
        volumeControl = findViewById(R.id.video_volume);
        videoRetry.setOnClickListener(this);
        volumeControl.setOnClickListener(this);
    }


    @Override public void onClick(View v) {
        if (v == videoRetry) {
            mOnVideoPlayerEventListener.restart();
        }else if (v == volumeControl) {//静音
            volumeType = !volumeType;
            if (volumeType) {
                volumeControl.setImageResource(R.mipmap.ic_volume_on);
                mOnVideoPlayerEventListener.setVolumeMode(volumeType);
            } else {
                volumeControl.setImageResource(R.mipmap.ic_volume_off);
                mOnVideoPlayerEventListener.setVolumeMode(volumeType);
            }
        }  else if (mOnVideoPlayerEventListener.isPlaying()
            || mOnVideoPlayerEventListener.isPaused()
            || mOnVideoPlayerEventListener.isBufferingPlaying()
            || mOnVideoPlayerEventListener.isBufferingPaused()) {
            if (mOnVideoPlayerEventListener.isPlaying()) {
                mOnVideoPlayerEventListener.pause();
            } else {
                mOnVideoPlayerEventListener.start();
            }
        }
    }


    @Override protected void onPlayStateChanged(int playState) {
        volumeType = ChouPlayerUtil.volumeMode;
        mOnVideoPlayerEventListener.setVolumeMode(volumeType);
        if (volumeType) {
            volumeControl.setImageResource(R.mipmap.ic_volume_on);
        } else {
            volumeControl.setImageResource(R.mipmap.ic_volume_off);
        }
        switch (playState) {
            case ChouVideoPlayer.STATE_IDLE:
                break;
            case ChouVideoPlayer.STATE_PREPARING:
                videoError.setVisibility(View.GONE);
                videoLoading.setVisibility(View.VISIBLE);
                break;
            case ChouVideoPlayer.STATE_PREPARED:
                break;
            case ChouVideoPlayer.STATE_PLAYING:
                videoLoading.setVisibility(View.GONE);
                onFollowListener.start();
                break;
            case ChouVideoPlayer.STATE_PAUSED:
                onFollowListener.pause();
                break;
            case ChouVideoPlayer.STATE_ERROR:
                videoError.setVisibility(View.VISIBLE);
                break;
            case ChouVideoPlayer.STATE_COMPLETED:
                mOnVideoPlayerEventListener.restart();

                break;
        }
    }


    private long mProgress;


    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mProgress = (long) (progress * mOnVideoPlayerEventListener.getDuration() / 100f);
        int pro = (int) mOnVideoPlayerEventListener.getCurrentPosition() / 1000;
        long duration = mOnVideoPlayerEventListener.getDuration();
        showChangePosition(duration, progress, false);
    }


    @Override public void onStartTrackingTouch(SeekBar seekBar) {
        cancelUpdateProgressTimer();
    }


    @Override public void onStopTrackingTouch(SeekBar seekBar) {
        mOnVideoPlayerEventListener.seekTo(mProgress);
        startUpdateProgressTimer();
    }


    @Override protected void onPlayModeChanged(int playMode) {

    }


    @Override protected void reset() {
        cancelUpdateProgressTimer();
        videoSeek.setProgress(0);
        videoLoading.setVisibility(View.GONE);
        videoError.setVisibility(View.GONE);
        if (null != onFollowListener) {
            onFollowListener.reset();
        }
    }


    @Override protected void updateProgress() {
        long position = mOnVideoPlayerEventListener.getCurrentPosition();
        long duration = mOnVideoPlayerEventListener.getDuration();
        int progress = (int) (100f * position / duration);
        videoSeek.setProgress(progress);
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
