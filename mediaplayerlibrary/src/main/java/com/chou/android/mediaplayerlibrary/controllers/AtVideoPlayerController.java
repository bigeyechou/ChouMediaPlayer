package com.chou.android.mediaplayerlibrary.controllers;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.OnVideoPlayerEventListener;
import com.chou.android.mediaplayerlibrary.R;
import com.chou.android.mediaplayerlibrary.VideoPlayerBaseController;
import com.chou.android.mediaplayerlibrary.utils.ChouPlayerUtil;


/**
 * 爱跳界面
 */
public class AtVideoPlayerController extends VideoPlayerBaseController
    implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private RelativeLayout rel_total;
    private TextView atLoadText;
    private LinearLayout atLoading;
    private TextView atChangePositionCurrent;
    private ProgressBar atChangePositionProgress;
    private LinearLayout atChangePosition;
    private ProgressBar atChangeBrightnessProgress;
    private LinearLayout atChangeBrightness;
    private ProgressBar atChangeVolumeProgress;
    private LinearLayout atChangeVolume;
    private TextView atRetry;
    private LinearLayout atError;
    private ImageView atBack;
    private RelativeLayout atTop;
    private ImageView atRestartOrPause;
    private TextView atPosition;
    private SeekBar atSeek;
    private TextView atDuration;
    private ImageView atFullScreen;
    private LinearLayout atBottom;

    private ImageView atReport;
    private LinearLayout atRight;
    private TextView atSpeed;
    private TextView atStartTime;
    private TextView atStopTime;
    private TextView atMirror;
    private TextView atStartAB;

    private Context mContext;
    private CountDownTimer mDismissTopBottomCountDownTimer;
    private Animation showTop,showBottom,hindTop,hindBottom;
    private boolean topBottomVisible;
    private float speed = 1.0f;
    private boolean isMirror = true;
    private long startTime;
    private long stopTime;
    private int abtime;
    private String videoUrl;
    private long mProgress;
    private boolean isAB = true;
    /**
     * 通知activity的事件
     */
    private OnNoticeActivityListener onNoticeActivityListener;
    public interface OnNoticeActivityListener{
        void onEventforATController(int eventType);
    }
    public void setOnNoticeActivityListener(OnNoticeActivityListener listener){
        this.onNoticeActivityListener = listener;
    }

    public AtVideoPlayerController(@NonNull Context context) {
        super(context);
        this.mContext = context;
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


    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.at_video_player_controller, this, true);
        atLoadText = findViewById(R.id.at_load_text);
        atLoading = findViewById(R.id.at_loading);
        atChangePositionCurrent = findViewById(R.id.at_change_position_current);
        atChangePositionProgress = findViewById(R.id.at_change_position_progress);
        atChangePosition = findViewById(R.id.at_change_position);
        atChangeBrightnessProgress = findViewById(R.id.at_change_brightness_progress);
        atChangeBrightness = findViewById(R.id.at_change_brightness);
        atChangeVolumeProgress = findViewById(R.id.at_change_volume_progress);
        atChangeVolume = findViewById(R.id.at_change_volume);
        atRetry = findViewById(R.id.at_retry);
        atError = findViewById(R.id.at_error);
        atBack = findViewById(R.id.at_back);
        atTop = findViewById(R.id.at_top);
        atRestartOrPause = findViewById(R.id.at_restart_or_pause);
        atPosition = findViewById(R.id.at_position);
        atSeek = findViewById(R.id.at_seek);
        atDuration = findViewById(R.id.at_duration);
        atFullScreen = findViewById(R.id.at_full_screen);
        atBottom = findViewById(R.id.at_bottom);
        atReport = findViewById(R.id.at_report);
        atRight = findViewById(R.id.at_right);
        atSpeed = findViewById(R.id.at_speed);
        atStartTime = findViewById(R.id.at_start_time);
        atStopTime = findViewById(R.id.at_stop_time);
        atMirror = findViewById(R.id.at_mirror);
        atStartAB = findViewById(R.id.at_startab);
        rel_total = findViewById(R.id.rel_total);

        atBack.setOnClickListener(this);
        atRestartOrPause.setOnClickListener(this);
        atFullScreen.setOnClickListener(this);
        atRetry.setOnClickListener(this);
        atReport.setOnClickListener(this);
        atSpeed.setOnClickListener(this);
        atMirror.setOnClickListener(this);
        atStartTime.setOnClickListener(this);
        atStopTime.setOnClickListener(this);
        atStartAB.setOnClickListener(this);
        atSeek.setOnSeekBarChangeListener(this);
        this.setOnClickListener(this);
    }


    @Override public void onClick(View v) {
        if (v == atBack) {
            if (mOnVideoPlayerEventListener.isFullScreen()) {
                mOnVideoPlayerEventListener.exitFullScreen();
            }else {
                if (null != onNoticeActivityListener){
                    onNoticeActivityListener.onEventforATController(1);
                }
            }
        } else if (v == atRestartOrPause) {
            if (mOnVideoPlayerEventListener.isPlaying() ||
                mOnVideoPlayerEventListener.isBufferingPlaying()) {
                mOnVideoPlayerEventListener.pause();
            } else if (mOnVideoPlayerEventListener.isPaused() ||
                mOnVideoPlayerEventListener.isBufferingPaused()) {
                mOnVideoPlayerEventListener.restart();
            }
        } else if (v == atFullScreen) {
            if (mOnVideoPlayerEventListener.isNormal()) {
                mOnVideoPlayerEventListener.enterFullScreen();
            } else if (mOnVideoPlayerEventListener.isFullScreen()) {
                mOnVideoPlayerEventListener.exitFullScreen();
            }
        } else if (v == atRetry) {
            mOnVideoPlayerEventListener.restart();
        } else if (v == atReport) {
            if (null != onNoticeActivityListener){
                onNoticeActivityListener.onEventforATController(2);
            }
        } else if (v == atSpeed) {
            if (speed >= 2) {
                speed = 0.50f;
            } else {
                speed += 0.50f;
            }
            atSpeed.setText(speed + "倍");
            mOnVideoPlayerEventListener.setSpeed(speed);
        } else if (v == atMirror) {//镜像
            isMirror = !isMirror;
            mOnVideoPlayerEventListener.setMirror(isMirror);
        } else if (v == atStartTime) {
            startTime = mOnVideoPlayerEventListener.getCurrentPosition();
            atStartTime.setText(startTime + "毫秒");
        } else if (v == atStopTime) {
            stopTime = mOnVideoPlayerEventListener.getCurrentPosition();
            abtime = (int) (stopTime/1000);
            atStopTime.setText(stopTime + "毫秒");
        } else if (v == atStartAB) {
            abPlayer(isAB);
        } else if (v == this) {
            if (mOnVideoPlayerEventListener.isPlaying()
                || mOnVideoPlayerEventListener.isPaused()
                || mOnVideoPlayerEventListener.isBufferingPlaying()
                || mOnVideoPlayerEventListener.isBufferingPaused()) {
                setTopBottomVisible(!topBottomVisible);
            }
        }
    }


    /**
     * 循环播放
     */
    public void abPlayer(boolean isab) {
        if (isab) {
            isAB = false;
            atStartAB.setText("AB停掉");
            mOnVideoPlayerEventListener.seekTo(startTime);
        } else {
            isAB = true;
            atStartAB.setText("AB开启");
            mOnVideoPlayerEventListener.start();
        }
    }

    public void setPathUrl(String pathUrl) {
        this.videoUrl = pathUrl;
        // 给播放器配置视频链接地址
        if (mOnVideoPlayerEventListener != null) {
            mOnVideoPlayerEventListener.setVideoPath(pathUrl);
        }

    }

    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mProgress = (long) (progress * mOnVideoPlayerEventListener.getDuration() / 100f);
        int pro = (int)mOnVideoPlayerEventListener.getCurrentPosition()/1000;
        if (pro == abtime){
            abPlayer(!isAB);
        }
        long duration = mOnVideoPlayerEventListener.getDuration();
        showChangePosition(duration,progress,false);

    }

    @Override public void onStartTrackingTouch(SeekBar seekBar) {
        cancelUpdateProgressTimer();
    }


    @Override public void onStopTrackingTouch(SeekBar seekBar) {
        if (mOnVideoPlayerEventListener.isBufferingPaused() ||
            mOnVideoPlayerEventListener.isPaused()) {
            mOnVideoPlayerEventListener.restart();
        }

        mOnVideoPlayerEventListener.seekTo(mProgress);
        startDismissTopBottomTimer();
        startUpdateProgressTimer();
    }


    @Override protected void onPlayStateChanged(int playState) {
        switch (playState) {
            case ChouVideoPlayer.STATE_IDLE:
                break;
            case ChouVideoPlayer.STATE_PREPARING:
                atLoading.setVisibility(View.VISIBLE);
                atLoadText.setText("正在准备...");
                atError.setVisibility(View.GONE);
                atTop.setVisibility(View.GONE);
                break;
            case ChouVideoPlayer.STATE_PREPARED:
                startUpdateProgressTimer();
                break;
            case ChouVideoPlayer.STATE_PLAYING:
                atLoading.setVisibility(View.GONE);
                atRestartOrPause.setImageResource(R.drawable.ic_player_pause);
                startDismissTopBottomTimer();
                break;
            case ChouVideoPlayer.STATE_PAUSED:
                atLoading.setVisibility(View.GONE);
                atRestartOrPause.setImageResource(R.drawable.ic_player_start);
                cancelDismissTopBottomTimer();
                break;
            case ChouVideoPlayer.STATE_BUFFERING_PLAYING:
                atLoading.setVisibility(View.VISIBLE);
                atRestartOrPause.setImageResource(R.drawable.ic_player_pause);
                atLoadText.setText("正在缓冲...");
                startDismissTopBottomTimer();
                break;
            case ChouVideoPlayer.STATE_BUFFERING_PAUSED:
                atLoading.setVisibility(View.VISIBLE);
                atRestartOrPause.setImageResource(R.drawable.ic_player_start);
                atLoadText.setText("正在缓冲...");
                cancelDismissTopBottomTimer();
                break;
            case ChouVideoPlayer.STATE_ERROR:
                cancelUpdateProgressTimer();
                setTopBottomVisible(false);
                atTop.setVisibility(View.VISIBLE);
                atError.setVisibility(View.VISIBLE);
                break;
            case ChouVideoPlayer.STATE_COMPLETED:
                cancelUpdateProgressTimer();
                setTopBottomVisible(false);
                mOnVideoPlayerEventListener.restart();
                break;
        }

    }


    @Override protected void onPlayModeChanged(int playMode) {
        switch (playMode) {
            case ChouVideoPlayer.MODE_NORMAL:
                atBack.setVisibility(View.VISIBLE);
                atRight.setVisibility(View.GONE);
                atReport.setVisibility(View.VISIBLE);
                atFullScreen.setImageResource(R.drawable.ic_player_enlarge);
                atFullScreen.setVisibility(View.VISIBLE);
                break;
            case ChouVideoPlayer.MODE_FULL_SCREEN:
                atBack.setVisibility(View.VISIBLE);
                atRight.setVisibility(View.VISIBLE);
                atReport.setVisibility(View.GONE);
                atFullScreen.setVisibility(View.GONE);
                atFullScreen.setImageResource(R.drawable.ic_player_shrink);
                break;
        }
    }
    /**
     * 设置top、bottom的显示和隐藏
     *
     * @param visible true显示，false隐藏.
     */
    private void setTopBottomVisible(boolean visible) {
        showTop =  AnimationUtils.loadAnimation(mContext,R.anim.top_to_bottom_mirror);
        hindTop = AnimationUtils.loadAnimation(mContext,R.anim.bottom_to_top_mirror);
        showBottom = AnimationUtils.loadAnimation(mContext,R.anim.bottom_to_top);
        hindBottom = AnimationUtils.loadAnimation(mContext,R.anim.top_to_bottom);

        if (visible){
            atTop.setAnimation(showTop);
            atBottom.setAnimation(showBottom);
        }else {
            atTop.setAnimation(hindTop);
            atBottom.setAnimation(hindBottom);
        }
        atTop.setVisibility(visible ? View.VISIBLE : View.GONE);
        atBottom.setVisibility(visible ? View.VISIBLE : View.GONE);
        topBottomVisible = visible;
        if (visible) {
            if (!mOnVideoPlayerEventListener.isPaused() &&
                !mOnVideoPlayerEventListener.isBufferingPaused()) {
                startDismissTopBottomTimer();
            }
        } else {
            cancelDismissTopBottomTimer();
        }
    }


    /**
     * 开启top、bottom自动消失的timer
     */
    private void startDismissTopBottomTimer() {
        cancelDismissTopBottomTimer();
        if (mDismissTopBottomCountDownTimer == null) {
            mDismissTopBottomCountDownTimer = new CountDownTimer(8000, 8000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }


                @Override
                public void onFinish() {
                    setTopBottomVisible(false);
                }
            };
        }
        mDismissTopBottomCountDownTimer.start();
    }


    /**
     * 取消top、bottom自动消失的timer
     */
    private void cancelDismissTopBottomTimer() {
        if (mDismissTopBottomCountDownTimer != null) {
            mDismissTopBottomCountDownTimer.cancel();
        }
    }


    @Override protected void reset() {
        topBottomVisible = false;
        cancelUpdateProgressTimer();
        cancelDismissTopBottomTimer();
        atSeek.setProgress(0);
        atSeek.setSecondaryProgress(0);
        atBottom.setVisibility(View.GONE);
        atFullScreen.setImageResource(R.drawable.ic_player_enlarge);
        atTop.setVisibility(View.VISIBLE);
        atBack.setVisibility(View.VISIBLE);
        atLoading.setVisibility(View.GONE);
        atError.setVisibility(View.GONE);
        atRight.setVisibility(GONE);
    }


    @Override protected void updateProgress() {
        long position = mOnVideoPlayerEventListener.getCurrentPosition();
        long duration = mOnVideoPlayerEventListener.getDuration();
        int bufferPercentage = mOnVideoPlayerEventListener.getBufferPercentage();
        atSeek.setSecondaryProgress(bufferPercentage);
        int progress = (int) (100f * position / duration);
        atSeek.setProgress(progress);
        atPosition.setText(ChouPlayerUtil.formatTime(position));
        atDuration.setText(ChouPlayerUtil.formatTime(duration));

    }


    @Override protected void showChangePosition(long duration, int newPositionProgress,boolean isShow) {
        if (isShow){
            atChangePosition.setVisibility(View.VISIBLE);
        }
        long newPosition = (long) (duration * newPositionProgress / 100f);
        atChangePositionCurrent.setText(ChouPlayerUtil.formatTime(newPosition));
        atChangePositionProgress.setProgress(newPositionProgress);
        atSeek.setProgress(newPositionProgress);
        atPosition.setText(ChouPlayerUtil.formatTime(newPosition));
    }


    @Override protected void hideChangePosition() {
        atChangePosition.setVisibility(View.GONE);
    }


    @Override protected void showChangeVolume(int newVolumeProgress) {
        atChangeVolume.setVisibility(View.VISIBLE);
        atChangeVolumeProgress.setProgress(newVolumeProgress);
    }


    @Override protected void hideChangeVolume() {
        atChangeVolume.setVisibility(View.GONE);
    }


    @Override protected void showChangeBrightness(int newBrightnessProgress) {
        atChangeBrightness.setVisibility(View.VISIBLE);
        atChangeBrightnessProgress.setProgress(newBrightnessProgress);
    }


    @Override protected void hideChangeBrightness() {
        atChangeBrightness.setVisibility(View.GONE);
    }
}
