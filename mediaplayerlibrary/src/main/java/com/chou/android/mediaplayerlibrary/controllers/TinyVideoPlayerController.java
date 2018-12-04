package com.chou.android.mediaplayerlibrary.controllers;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.OnVideoPlayerEventListener;
import com.chou.android.mediaplayerlibrary.R;
import com.chou.android.mediaplayerlibrary.VideoPlayerBaseController;
import com.chou.android.mediaplayerlibrary.utils.ChouPlayerUtil;

/**
 *
 */
public class TinyVideoPlayerController extends VideoPlayerBaseController
    implements View.OnClickListener {

    private ImageView tinyVideoBack;
    private LinearLayout tinyVideoLoading;
    private Context mContext;
    private String videoUrl;

    private OnDancerBoxListener onDancerBoxListener;
    public interface OnDancerBoxListener {
    }
    public void setOnDancerBoxListener(OnDancerBoxListener listener) {
        this.onDancerBoxListener = listener;
    }


    public TinyVideoPlayerController(
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
        LayoutInflater.from(mContext).inflate(R.layout.tiny_video_player_controller, this, true);
        tinyVideoBack = (ImageView) findViewById(R.id.tiny_video_back);
        tinyVideoLoading = (LinearLayout) findViewById(R.id.tiny_video_loading);

        tinyVideoBack.setOnClickListener(this);
        initWindow();
    }


    @Override public void onClick(View v) {
        if (videoUrl==null){
            return;
        }
        if (v == tinyVideoBack) {//返回
                mOnVideoPlayerEventListener.exitFullScreen();
        }
    }


    @Override protected void onPlayStateChanged(int playState) {
        switch (playState) {
            case ChouVideoPlayer.STATE_IDLE:
                break;
            case ChouVideoPlayer.STATE_PREPARING:
                tinyVideoLoading.setVisibility(View.VISIBLE);
                break;
            case ChouVideoPlayer.STATE_PREPARED:
                break;
            case ChouVideoPlayer.STATE_PLAYING:
                tinyVideoLoading.setVisibility(View.GONE);
                break;
            case ChouVideoPlayer.STATE_PAUSED:
                tinyVideoLoading.setVisibility(View.GONE);
                break;
            case ChouVideoPlayer.STATE_BUFFERING_PLAYING:
                tinyVideoLoading.setVisibility(View.GONE);
                break;
            case ChouVideoPlayer.STATE_BUFFERING_PAUSED:
                tinyVideoLoading.setVisibility(View.GONE);
                break;
            case ChouVideoPlayer.STATE_ERROR:
                break;
            case ChouVideoPlayer.STATE_COMPLETED:
                mOnVideoPlayerEventListener.restart();
                break;
                default:
                    break;
        }
    }


    @Override protected void onPlayModeChanged(int playMode) {
        switch (playMode) {
            case ChouVideoPlayer.MODE_NORMAL:
                break;
            case ChouVideoPlayer.MODE_FULL_SCREEN:
                break;
        }
    }

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private int floatX;
    private int floatY;
    private boolean firstTouch = true;
    private void initWindow() {
        mWindowManager = ChouPlayerUtil.getWindowManager(getContext().getApplicationContext());
        mParams = new WindowManager.LayoutParams();
        //        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT; // 设置window type
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            mParams.type =  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        // 设置图片格式，效果为背景透明
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // mParams.windowAnimations = R.style.FloatWindowAnimation;
        mParams.gravity = Gravity.START | Gravity.TOP; // 调整悬浮窗口至右下角
        // 设置悬浮窗口长宽数据
        int width = ChouPlayerUtil.dp2px(getContext(), 250);
        mParams.width = width;
        mParams.height = width * 9 / 16;
        mParams.x = floatX;
        mParams.y = floatY;
    }
    /**
     * 添加至窗口
     */
    public boolean addToWindow() {
        if (mWindowManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!isAttachedToWindow()) {
                    mWindowManager.addView(this, mParams);
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() == null) {
                        mWindowManager.addView(this, mParams);
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * 从窗口移除
     */
    public boolean removeFromWindow() {
        if (mWindowManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isAttachedToWindow()) {
                    mWindowManager.removeViewImmediate(this);
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() != null) {
                        mWindowManager.removeViewImmediate(this);
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                firstTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (firstTouch) {
                    floatX = (int) event.getX();
                    floatY = (int) (event.getY() + ChouPlayerUtil.getStatusBarHeight(getContext()));
                    firstTouch = false;
                }
                mParams.x = X - floatX;
                mParams.y = Y - floatY;
                mWindowManager.updateViewLayout(this, mParams);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override protected void reset() {
        tinyVideoLoading.setVisibility(View.GONE);
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
