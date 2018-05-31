package com.chou.android.mediaplayerlibrary.controllers;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import android.widget.Toast;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.OnVideoPlayerEventListener;
import com.chou.android.mediaplayerlibrary.R;
import com.chou.android.mediaplayerlibrary.VideoPlayerBaseController;
import com.chou.android.mediaplayerlibrary.utils.ChouPlayerUtil;
import com.chou.android.mediaplayerlibrary.view.CircularProgressBar;
import com.chou.android.mediaplayerlibrary.view.TouchView;

/**
 * 爱跳详情界面
 */
public class AtVideoPlayerController extends VideoPlayerBaseController
    implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private RelativeLayout atVideoTotal;
    private TextView atVideoLoadText;
    private LinearLayout atVideoLoading;
    private TextView atVideoChangePositionCurrent;
    private ProgressBar atVideoChangePositionProgress;
    private LinearLayout atVideoChangePosition;
    private ProgressBar atVideoChangeBrightnessProgress;
    private LinearLayout atVideoChangeBrightness;
    private ProgressBar atVideoChangeVolumeProgress;
    private LinearLayout atVideoChangeVolume;
    private TextView atVideoRetry;
    private LinearLayout atVideoError;
    private ImageView atVideoBack;
    private RelativeLayout atVideoTop;
    private ImageView atVideoRestartOrPause;
    private TextView atVideoPosition;
    private SeekBar atVideoSeek;
    private TextView atVideoDuration;
    private ImageView atVideoFullScreen;
    private LinearLayout atVideoBottom;
    private LinearLayout atVideoCutLayout;
    private CircularProgressBar atVideoCutProgress;
    private ImageView atVideoReport;
    private ImageView atVideoSaveClose;
    private ImageView atVideoSave;
    private ImageView atVideoRew;
    private LinearLayout atVideoRight;
    private ImageView atVideoSpeed;
    private TouchView atVideoCut;
    private ImageView atVideoMirror;

    private Context mContext;
    /**
     * 倒计时
     */
    private CountDownTimer mDismissTopBottomCountDownTimer;
    /**
     * 隐藏显示的动画
     */
    private Animation showTop, showBottom, showRight, hindTop, hindBottom, hindRight;
    private boolean topBottomVisible;
    /**
     * 倒带时间
     */
    private static final long BACK_TIME = 5 * 1000;
    /**
     * 速度调节
     */
    private int speedType = 3;
    private static final float SPEED_050 = 0.50f;
    private static final float SPEED_075 = 0.75f;
    private static final float SPEED_100 = 1f;
    private static final float SPEED_150 = 1.50f;
    private static final float SPEED_200 = 2.0f;

    private boolean isShowRight = false;
    private boolean isMirror = true;

    private String videoUrl;
    /**
     * AB循环相关
     */
    private long mProgress;
    private long cutStartTime;
    private long cutStopTime;
    private int stopTime;
    private boolean isABCirculation = false;
    /**
     * 录制动画相关
     */
    private ValueAnimator cutAnimator;
    public static final int VIDEO_CUT_MIN = 1000;
    public static final float VIDEO_CUT_MAX = 15000f;
    private int currentCutTime;

    /**
     * 通知activity的事件
     */
    private OnNoticeActivityListener onNoticeActivityListener;
    private static final int BACK_EVENT = 1;
    private static final int INFORM_EVENT = 2;
    private static final int SAVE_CUT_EVENT = 3;
    private Bundle mEventBundle;


    public interface OnNoticeActivityListener {
        /**
         * 交互的事件通知
         *
         * @param eventType 通知类型：1
         */
        void onEventforATController(int eventType, Bundle eventBundle);
    }


    public void setOnNoticeActivityListener(OnNoticeActivityListener listener) {
        this.onNoticeActivityListener = listener;
    }


    /**
     * 构造
     */
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
        atVideoLoadText = findViewById(R.id.at_video_load_text);
        atVideoLoading = findViewById(R.id.at_video_loading);
        atVideoChangePositionCurrent = findViewById(R.id.at_video_change_position_current);
        atVideoChangePositionProgress = findViewById(R.id.at_video_change_position_progress);
        atVideoChangePosition = findViewById(R.id.at_video_change_position);
        atVideoChangeBrightnessProgress = findViewById(R.id.at_video_change_brightness_progress);
        atVideoChangeBrightness = findViewById(R.id.at_video_change_brightness);
        atVideoChangeVolumeProgress = findViewById(R.id.at_video_change_volume_progress);
        atVideoChangeVolume = findViewById(R.id.at_video_change_volume);
        atVideoRetry = findViewById(R.id.at_video_retry);
        atVideoError = findViewById(R.id.at_video_error);
        atVideoBack = findViewById(R.id.at_video_back);
        atVideoTop = findViewById(R.id.at_video_top);
        atVideoRestartOrPause = findViewById(R.id.at_video_restart_or_pause);
        atVideoPosition = findViewById(R.id.at_video_position);
        atVideoSeek = findViewById(R.id.at_video_seek);
        atVideoDuration = findViewById(R.id.at_video_duration);
        atVideoFullScreen = findViewById(R.id.at_video_full_screen);
        atVideoBottom = findViewById(R.id.at_video_bottom);
        atVideoReport = findViewById(R.id.at_video_report);
        atVideoSaveClose = findViewById(R.id.at_video_save_close);
        atVideoSave = findViewById(R.id.at_video_save);
        atVideoRew = findViewById(R.id.at_video_rew);
        atVideoRight = findViewById(R.id.at_video_right);
        atVideoSpeed = findViewById(R.id.at_video_speed);
        atVideoCut = findViewById(R.id.at_video_cut);
        atVideoMirror = findViewById(R.id.at_video_mirror);
        atVideoTotal = findViewById(R.id.rel_total);
        atVideoCutLayout = findViewById(R.id.at_video_cut_layout);
        atVideoCutProgress = findViewById(R.id.at_video_cut_progress);
        /**
         * 动画
         */
        cutAnimator = ValueAnimator.ofFloat(VIDEO_CUT_MAX, 0);
        LinearInterpolator ll = new LinearInterpolator();
        cutAnimator.setDuration((long) VIDEO_CUT_MAX);
        cutAnimator.setInterpolator(ll);
        cutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                currentCutTime = Math.round((float) animation.getAnimatedValue());
                atVideoCutProgress.setProgress((Float) animation.getAnimatedValue());
                if (currentCutTime == 0) {//自动显示
                    saveShow();
                    isABCirculation = true;
                }
            }
        });

        atVideoBack.setOnClickListener(this);
        atVideoRestartOrPause.setOnClickListener(this);
        atVideoFullScreen.setOnClickListener(this);
        atVideoRetry.setOnClickListener(this);
        atVideoReport.setOnClickListener(this);
        atVideoSpeed.setOnClickListener(this);
        atVideoMirror.setOnClickListener(this);
        atVideoSave.setOnClickListener(this);
        atVideoSaveClose.setOnClickListener(this);
        atVideoRew.setOnClickListener(this);
        atVideoSeek.setOnSeekBarChangeListener(this);
        this.setOnClickListener(this);
        /**
         * 点击录制
         */
        atVideoCut.setOnTouchEventListener(new TouchView.OnTouchEventListener() {
            @Override public void onTouchDown() {
                //显示
                touchDown();
            }

            @Override public void onTouchCancel() {
                //隐藏
                touchCancel(false);
            }
        });
    }

    private void touchDown(){
        cutAnimator.start();
        cutShow();
        cutStartTime = mOnVideoPlayerEventListener.getCurrentPosition();
        isABCirculation = false;
    }

    private void touchCancel(boolean isEnd){
        if (!isEnd){
            cutStopTime = mOnVideoPlayerEventListener.getCurrentPosition();
        }else {
            cutStopTime = mOnVideoPlayerEventListener.getDuration();
        }
        stopTime = (int) (cutStopTime / 1000);
        cutAnimator.cancel();

        if (currentCutTime >= VIDEO_CUT_MAX - VIDEO_CUT_MIN) {
            //小于1秒
            isABCirculation = false;
            Toast.makeText(mContext, "不能小于一秒", Toast.LENGTH_SHORT).show();
            saveCloseShow(false);
        } else if (currentCutTime < VIDEO_CUT_MAX - VIDEO_CUT_MIN || currentCutTime == 0) {
            //1~15秒
            saveShow();
            isABCirculation = true;
            mOnVideoPlayerEventListener.seekTo(cutStartTime);
        }
    }


    @Override public void onClick(View v) {
        if (v == atVideoBack) {
            if (mOnVideoPlayerEventListener.isFullScreen()) {
                mOnVideoPlayerEventListener.exitFullScreen();
            } else {
                if (null != onNoticeActivityListener) {
                    onNoticeActivityListener.onEventforATController(BACK_EVENT, null);
                }
            }
        } else if (v == atVideoRestartOrPause) {
            if (mOnVideoPlayerEventListener.isPlaying() ||
                mOnVideoPlayerEventListener.isBufferingPlaying()) {
                mOnVideoPlayerEventListener.pause();
            } else if (mOnVideoPlayerEventListener.isPaused() ||
                mOnVideoPlayerEventListener.isBufferingPaused()) {
                mOnVideoPlayerEventListener.restart();
            }
        } else if (v == atVideoFullScreen) {
            if (mOnVideoPlayerEventListener.isNormal()) {
                mOnVideoPlayerEventListener.enterFullScreen();
            } else if (mOnVideoPlayerEventListener.isFullScreen()) {
                mOnVideoPlayerEventListener.exitFullScreen();
            }
        } else if (v == atVideoRetry) {//重试
            mOnVideoPlayerEventListener.restart();
        } else if (v == atVideoReport) {//举报
            if (null != onNoticeActivityListener) {
                onNoticeActivityListener.onEventforATController(INFORM_EVENT, null);
            }
        } else if (v == atVideoSpeed) {//速度调节
            setSpeed();
        } else if (v == atVideoMirror) {//镜像
            isMirror = !isMirror;
            mOnVideoPlayerEventListener.setMirror(isMirror);
        } else if (v == atVideoSave) {//保存
            saveCloseShow(true);
            if (null != onNoticeActivityListener) {
                mEventBundle = new Bundle();
                mEventBundle.putLong("CUT_START_TIME", cutStartTime);
                mEventBundle.putLong("CUT_STOP_TIME", cutStopTime);
                onNoticeActivityListener.onEventforATController(SAVE_CUT_EVENT, mEventBundle);
            }
        } else if (v == atVideoSaveClose) {//取消保存
            saveCloseShow(true);
        } else if (v == atVideoRew) {//倒退
            mOnVideoPlayerEventListener.setRewind(BACK_TIME);
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
     * 录制样式
     */
    private void cutShow() {
        setTopBottomVisible(false);
        atVideoTop.setVisibility(GONE);
        atVideoBottom.setVisibility(GONE);
        atVideoRew.setVisibility(GONE);
        atVideoSpeed.setVisibility(GONE);
        atVideoMirror.setVisibility(GONE);
        atVideoCutLayout.setVisibility(GONE);
        atVideoRight.setVisibility(VISIBLE);
        atVideoCut.setVisibility(VISIBLE);
        atVideoCutLayout.setVisibility(VISIBLE);

    }


    /**
     * 保存样式
     */
    private void saveShow() {
        atVideoRight.setVisibility(GONE);
        atVideoRew.setVisibility(GONE);
        atVideoCut.setVisibility(GONE);
        atVideoCutLayout.setVisibility(GONE);
        atVideoSaveClose.setVisibility(VISIBLE);
        atVideoSave.setVisibility(VISIBLE);
        this.setOnClickListener(null);
        mOnVideoPlayerEventListener.isVideoScaling(true);
    }


    /**
     * 结束保存样式
     */
    private void saveCloseShow(boolean isAnim) {
        atVideoSaveClose.setVisibility(GONE);
        atVideoSave.setVisibility(GONE);
        atVideoRew.setVisibility(VISIBLE);
        atVideoCutLayout.setVisibility(GONE);
        atVideoMirror.setVisibility(VISIBLE);
        atVideoSpeed.setVisibility(VISIBLE);
        atVideoCut.setVisibility(VISIBLE);
        this.setOnClickListener(this);
        setTopBottomVisible(true);
        mOnVideoPlayerEventListener.start();
        isABCirculation = false;
        if (isAnim) {
            mOnVideoPlayerEventListener.isVideoScaling(false);
        }
    }


    /**
     * 设置速度
     */
    private void setSpeed() {
        float speed = 1;
        if (speedType >= 5) {
            speedType = 1;
        } else {
            speedType += 1;
        }
        switch (speedType) {
            case 1:
                speed = SPEED_050;
                atVideoSpeed.setImageResource(R.mipmap.ic_wujike_media_speed_very_slow);
                break;
            case 2:
                speed = SPEED_075;
                atVideoSpeed.setImageResource(R.mipmap.ic_wujike_media_speed_slow);
                break;
            case 3:
                speed = SPEED_100;
                atVideoSpeed.setImageResource(R.mipmap.ic_wujike_media_speed_normal);
                break;
            case 4:
                speed = SPEED_150;
                atVideoSpeed.setImageResource(R.mipmap.ic_wujike_media_speed_fast);
                break;
            case 5:
                speed = SPEED_200;
                atVideoSpeed.setImageResource(R.mipmap.ic_wujike_media_speed_very_fast);
                break;
        }
        mOnVideoPlayerEventListener.setSpeed(speed);
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
        int pro = (int) mOnVideoPlayerEventListener.getCurrentPosition() / 1000;
        if (isABCirculation && pro >= stopTime) {
            mOnVideoPlayerEventListener.seekTo(cutStartTime);
        }
        long duration = mOnVideoPlayerEventListener.getDuration();
        showChangePosition(duration, progress, false);

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


    /**
     * @param playState 播放状态：
     * <ul>
     * <li>{@link ChouVideoPlayer#STATE_IDLE}</li>
     * <li>{@link ChouVideoPlayer#STATE_PREPARING}</li>
     * <li>{@link ChouVideoPlayer#STATE_PREPARED}</li>
     * <li>{@link ChouVideoPlayer#STATE_PLAYING}</li>
     * <li>{@link ChouVideoPlayer#STATE_PAUSED}</li>
     * <li>{@link ChouVideoPlayer#STATE_BUFFERING_PLAYING}</li>
     * <li>{@link ChouVideoPlayer#STATE_BUFFERING_PAUSED}</li>
     * <li>{@link ChouVideoPlayer#STATE_ERROR}</li>
     * <li>{@link ChouVideoPlayer#STATE_COMPLETED}</li>
     */
    @Override protected void onPlayStateChanged(int playState) {
        switch (playState) {
            case ChouVideoPlayer.STATE_IDLE:
                break;
            case ChouVideoPlayer.STATE_PREPARING:

                atVideoLoading.setVisibility(View.VISIBLE);
                atVideoLoadText.setText("正在准备...");
                atVideoError.setVisibility(View.GONE);
                atVideoTop.setVisibility(View.GONE);
                break;
            case ChouVideoPlayer.STATE_PREPARED:
                startUpdateProgressTimer();
                break;
            case ChouVideoPlayer.STATE_PLAYING:
                atVideoLoading.setVisibility(View.GONE);
                atVideoRestartOrPause.setImageResource(R.mipmap.ic_video_pause);
                startDismissTopBottomTimer();
                break;
            case ChouVideoPlayer.STATE_PAUSED:
                atVideoLoading.setVisibility(View.GONE);
                atVideoRestartOrPause.setImageResource(R.mipmap.ic_video_play);
                cancelDismissTopBottomTimer();
                break;
            case ChouVideoPlayer.STATE_BUFFERING_PLAYING:
                if (isABCirculation) {
                    atVideoLoading.setVisibility(View.GONE);
                } else {
                    atVideoLoading.setVisibility(View.VISIBLE);
                }
                atVideoRestartOrPause.setImageResource(R.mipmap.ic_video_pause);
                atVideoLoadText.setText("正在缓冲...");
                startDismissTopBottomTimer();
                break;
            case ChouVideoPlayer.STATE_BUFFERING_PAUSED:
                if (isABCirculation) {
                    atVideoLoading.setVisibility(View.GONE);
                } else {
                    atVideoLoading.setVisibility(View.VISIBLE);
                }
                atVideoRestartOrPause.setImageResource(R.mipmap.ic_video_play);
                atVideoLoadText.setText("正在缓冲...");
                cancelDismissTopBottomTimer();
                break;
            case ChouVideoPlayer.STATE_ERROR:
                cancelUpdateProgressTimer();
                setTopBottomVisible(false);
                atVideoTop.setVisibility(View.VISIBLE);
                atVideoError.setVisibility(View.VISIBLE);
                break;
            case ChouVideoPlayer.STATE_COMPLETED:
                cancelUpdateProgressTimer();
                setTopBottomVisible(false);
                touchCancel(true);
                mOnVideoPlayerEventListener.restart();
                //隐藏
                break;
        }

    }


    /**
     * @param playMode 播放器的模式：横竖屏
     * <ul>
     * <li>{@link ChouVideoPlayer#MODE_NORMAL}</li>
     * <li>{@link ChouVideoPlayer#MODE_FULL_SCREEN}</li>
     */
    @Override protected void onPlayModeChanged(int playMode) {

        switch (playMode) {
            case ChouVideoPlayer.MODE_NORMAL:
                atVideoBack.setVisibility(View.VISIBLE);
                atVideoRight.setVisibility(View.GONE);
                atVideoRew.setVisibility(GONE);
                atVideoSave.setVisibility(GONE);
                atVideoCutLayout.setVisibility(GONE);
                atVideoReport.setVisibility(View.VISIBLE);
                atVideoFullScreen.setImageResource(R.mipmap.ic_video_enlarge);
                atVideoReport.setVisibility(VISIBLE);
                isShowRight = false;
                break;
            case ChouVideoPlayer.MODE_FULL_SCREEN:
                atVideoBack.setVisibility(View.VISIBLE);
                atVideoRight.setVisibility(View.VISIBLE);
                atVideoRew.setVisibility(VISIBLE);
                atVideoReport.setVisibility(View.GONE);
                atVideoFullScreen.setImageResource(R.mipmap.ic_video_shrink);
                atVideoReport.setVisibility(GONE);
                isShowRight = true;
                break;
        }
    }


    /**
     * 设置top、bottom的显示和隐藏
     *
     * @param visible true显示，false隐藏.
     */
    private void setTopBottomVisible(boolean visible) {
        showTop = AnimationUtils.loadAnimation(mContext, R.anim.top_to_bottom_mirror);
        hindTop = AnimationUtils.loadAnimation(mContext, R.anim.bottom_to_top_mirror);
        showBottom = AnimationUtils.loadAnimation(mContext, R.anim.bottom_to_top);
        hindBottom = AnimationUtils.loadAnimation(mContext, R.anim.top_to_bottom);
        showRight = AnimationUtils.loadAnimation(mContext, R.anim.right_to_left);
        hindRight = AnimationUtils.loadAnimation(mContext, R.anim.left_to_right);
        if (visible) {
            atVideoTop.setAnimation(showTop);
            atVideoBottom.setAnimation(showBottom);
            atVideoRight.setAnimation(showRight);
        } else {
            atVideoTop.setAnimation(hindTop);
            atVideoBottom.setAnimation(hindBottom);
            atVideoRight.setAnimation(hindRight);
        }
        atVideoTop.setVisibility(visible ? View.VISIBLE : View.GONE);
        atVideoBottom.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (isShowRight) {
            atVideoRight.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
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
        atVideoSeek.setProgress(0);
        atVideoSeek.setSecondaryProgress(0);
        atVideoBottom.setVisibility(View.GONE);
        atVideoFullScreen.setImageResource(R.mipmap.ic_video_enlarge);
        atVideoTop.setVisibility(View.VISIBLE);
        atVideoBack.setVisibility(View.VISIBLE);
        atVideoLoading.setVisibility(View.GONE);
        atVideoError.setVisibility(View.GONE);
        atVideoRight.setVisibility(GONE);
        atVideoRew.setVisibility(GONE);
        atVideoSave.setVisibility(GONE);
        atVideoSaveClose.setVisibility(GONE);
    }


    @Override protected void updateProgress() {
        long position = mOnVideoPlayerEventListener.getCurrentPosition();
        long duration = mOnVideoPlayerEventListener.getDuration();
        int bufferPercentage = mOnVideoPlayerEventListener.getBufferPercentage();
        atVideoSeek.setSecondaryProgress(bufferPercentage);
        int progress = (int) (100f * position / duration);
        atVideoSeek.setProgress(progress);
        atVideoPosition.setText(ChouPlayerUtil.formatTime(position));
        atVideoDuration.setText(ChouPlayerUtil.formatTime(duration));

    }


    @Override
    protected void showChangePosition(long duration, int newPositionProgress, boolean isShow) {
        if (isShow) {
            atVideoChangePosition.setVisibility(View.VISIBLE);
        }
        long newPosition = (long) (duration * newPositionProgress / 100f);
        atVideoChangePositionCurrent.setText(ChouPlayerUtil.formatTime(newPosition));
        atVideoChangePositionProgress.setProgress(newPositionProgress);
        atVideoSeek.setProgress(newPositionProgress);
        atVideoPosition.setText(ChouPlayerUtil.formatTime(newPosition));
    }


    @Override protected void hideChangePosition() {
        atVideoChangePosition.setVisibility(View.GONE);
    }


    @Override protected void showChangeVolume(int newVolumeProgress) {
        atVideoChangeVolume.setVisibility(View.VISIBLE);
        atVideoChangeVolumeProgress.setProgress(newVolumeProgress);
    }


    @Override protected void hideChangeVolume() {
        atVideoChangeVolume.setVisibility(View.GONE);
    }


    @Override protected void showChangeBrightness(int newBrightnessProgress) {
        atVideoChangeBrightness.setVisibility(View.VISIBLE);
        atVideoChangeBrightnessProgress.setProgress(newBrightnessProgress);
    }


    @Override protected void hideChangeBrightness() {
        atVideoChangeBrightness.setVisibility(View.GONE);
    }
}
