package com.chou.android.mediaplayerlibrary.controllers;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private ImageView atVideoReport;
    private TextView atVideoSpeed;
    private TextView atVideoMirror;

    private Context mContext;
    /**
     * 倒计时
     */
    private CountDownTimer mDismissTopBottomCountDownTimer;
    /**
     * 隐藏显示的动画
     */
    private Animation showTop, showBottom, hindTop, hindBottom;
    private boolean topBottomVisible;

    /**
     * 速度调节
     */
    private int speedType = 3;
    private static final float SPEED_050 = 0.50f;
    private static final float SPEED_075 = 0.75f;
    private static final float SPEED_100 = 1f;
    private static final float SPEED_150 = 1.50f;
    private static final float SPEED_200 = 2.0f;

    private boolean isMirror = false;

    private String videoUrl;
    /**
     * AB循环相关
     */
    private long mProgress;
    /**
     * 相关接口
     */
    private OnVideoDetailListener onVideoDetailListener;


    public interface OnVideoDetailListener {
        /**
         * 后退
         */
        void onVideoBack();
        /**
         * 通知举报视频
         */
        void onVideoInform();
        /**
         * 保存剪裁的视频
         */
        void onVideoSaveCut(long startTime, long stopTime);
        /**
         * 横竖屏变化
         */
        void onVideoChange(boolean isNormal);
    }


    public void setOnVideoDetailListener(OnVideoDetailListener listener) {
        this.onVideoDetailListener = listener;
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
        atVideoLoadText = (TextView) findViewById(R.id.at_video_load_text);
        atVideoLoading = (LinearLayout) findViewById(R.id.at_video_loading);
        atVideoChangePositionCurrent = (TextView) findViewById(
            R.id.at_video_change_position_current);
        atVideoChangePositionProgress = (ProgressBar) findViewById(
            R.id.at_video_change_position_progress);
        atVideoChangePosition = (LinearLayout) findViewById(R.id.at_video_change_position);
        atVideoChangeBrightnessProgress = (ProgressBar) findViewById(
            R.id.at_video_change_brightness_progress);
        atVideoChangeBrightness = (LinearLayout) findViewById(R.id.at_video_change_brightness);
        atVideoChangeVolumeProgress = (ProgressBar) findViewById(
            R.id.at_video_change_volume_progress);
        atVideoChangeVolume = (LinearLayout) findViewById(R.id.at_video_change_volume);
        atVideoRetry = (TextView) findViewById(R.id.at_video_retry);
        atVideoError = (LinearLayout) findViewById(R.id.at_video_error);
        atVideoBack = (ImageView) findViewById(R.id.at_video_back);
        atVideoTop = (RelativeLayout) findViewById(R.id.at_video_top);
        atVideoRestartOrPause = (ImageView) findViewById(R.id.at_video_restart_or_pause);
        atVideoPosition = (TextView) findViewById(R.id.at_video_position);
        atVideoSeek = (SeekBar) findViewById(R.id.at_video_seek);
        atVideoDuration = (TextView) findViewById(R.id.at_video_duration);
        atVideoFullScreen = (ImageView) findViewById(R.id.at_video_full_screen);
        atVideoBottom = (LinearLayout) findViewById(R.id.at_video_bottom);
        atVideoReport = (ImageView) findViewById(R.id.at_video_report);
        atVideoSpeed = (TextView) findViewById(R.id.at_video_speed);
        atVideoMirror = (TextView) findViewById(R.id.at_video_mirror);


        atVideoBack.setOnClickListener(this);
        atVideoRestartOrPause.setOnClickListener(this);
        atVideoFullScreen.setOnClickListener(this);
        atVideoRetry.setOnClickListener(this);
        atVideoReport.setOnClickListener(this);
        atVideoSpeed.setOnClickListener(this);
        atVideoMirror.setOnClickListener(this);
        atVideoSeek.setOnSeekBarChangeListener(this);
        this.setOnClickListener(this);

    }

    @Override public void onClick(View v) {
        if (v == atVideoBack) {
            if (mOnVideoPlayerEventListener.isFullScreen()) {
                mOnVideoPlayerEventListener.exitFullScreen();
            } else {
                if (null != onVideoDetailListener) {
                    onVideoDetailListener.onVideoBack();
                }
            }
        } else if (v == atVideoRestartOrPause) {
            if (mOnVideoPlayerEventListener.isPlaying() ||
                mOnVideoPlayerEventListener.isBufferingPlaying()) {
                mOnVideoPlayerEventListener.pause();

                setIsSpeed(false);
            } else if (mOnVideoPlayerEventListener.isPaused() ||
                mOnVideoPlayerEventListener.isBufferingPaused()) {
                mOnVideoPlayerEventListener.restart();

                setIsSpeed(true);
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
            if (null != onVideoDetailListener) {
                onVideoDetailListener.onVideoInform();
            }
        } else if (v == atVideoSpeed) {//速度调节
            setSpeed();
        } else if (v == atVideoMirror) {//镜像
            isMirror = !isMirror;
            if (isMirror){
                atVideoMirror.setTextColor(getResources().getColor(R.color.white40));
                atVideoMirror.setBackground(getResources().getDrawable(R.drawable.shape_white40_2));
            }else {
                atVideoMirror.setTextColor(getResources().getColor(R.color.white));
                atVideoMirror.setBackground(getResources().getDrawable(R.drawable.shape_white_2));
            }
            mOnVideoPlayerEventListener.setMirror(isMirror);
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
     * 速度样式
     */
    private void setIsSpeed(boolean isSpeed) {
        if (isSpeed) {
            atVideoSpeed.setTextColor(getResources().getColor(R.color.white));
            atVideoSpeed.setOnClickListener(this);
            switch (speedType) {
                case 1:
                    atVideoSpeed.setText("0.5X");
                    break;
                case 2:
                    atVideoSpeed.setText("0.75X");
                    break;
                case 3:
                    atVideoSpeed.setText("1.0X");
                    break;
                case 4:
                    atVideoSpeed.setText("1.5X");
                    break;
                case 5:
                    atVideoSpeed.setText("2.0X");
                    break;
            }
        } else {
            atVideoSpeed.setOnClickListener(null);
            atVideoSpeed.setTextColor(getResources().getColor(R.color.white40));
            switch (speedType) {
                case 1:
                    atVideoSpeed.setText("0.5X");
                    break;
                case 2:
                    atVideoSpeed.setText("0.75X");
                    break;
                case 3:
                    atVideoSpeed.setText("1.0X");
                    break;
                case 4:
                    atVideoSpeed.setText("1.5X");
                    break;
                case 5:
                    atVideoSpeed.setText("2.0X");
                    break;
            }
        }

    }


    /**
     * 设置速度
     */
    private void setSpeed() {
        float speed = 1;
        atVideoSpeed.setTextColor(getResources().getColor(R.color.white));
        if (speedType >= 5) {
            speedType = 1;
        } else {
            speedType += 1;
        }
        switch (speedType) {
            case 1:
                speed = SPEED_050;
                atVideoSpeed.setText("0.5X");
                break;
            case 2:
                speed = SPEED_075;
                atVideoSpeed.setText("0.75X");
                break;
            case 3:
                speed = SPEED_100;
                atVideoSpeed.setText("1.0X");
                break;
            case 4:
                speed = SPEED_150;
                atVideoSpeed.setText("1.5X");
                break;
            case 5:
                speed = SPEED_200;
                atVideoSpeed.setText("2.0X");
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


    public void startVideo() {
        if (mOnVideoPlayerEventListener != null) {
            mOnVideoPlayerEventListener.start();
            atVideoRestartOrPause.setImageResource(R.mipmap.ic_video_pause);
        }
    }

    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mProgress = (long) (progress * mOnVideoPlayerEventListener.getDuration() / 100f);
        long duration = mOnVideoPlayerEventListener.getDuration();
        showChangePosition(duration, progress, false);

    }


    @Override public void onStartTrackingTouch(SeekBar seekBar) {
        cancelUpdateProgressTimer();
    }


    @Override public void onStopTrackingTouch(SeekBar seekBar) {
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
                atVideoRestartOrPause.setImageResource(R.mipmap.ic_video_pause);
                atVideoLoadText.setText("正在缓冲...");
                startDismissTopBottomTimer();
                break;
            case ChouVideoPlayer.STATE_BUFFERING_PAUSED:
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
                // if (isReStart){
                cancelUpdateProgressTimer();
                setTopBottomVisible(false);
                mOnVideoPlayerEventListener.restart();
                // }else {
                //     autoIntoCut();
                // }
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
                atVideoSpeed.setVisibility(View.INVISIBLE);
                // atVideoRight.setVisibility(View.GONE);
                // atVideoRew.setVisibility(GONE);
                // atVideoSave.setVisibility(GONE);
                // atVideoCutLayout.setVisibility(GONE);
                atVideoReport.setVisibility(View.VISIBLE);
                atVideoFullScreen.setImageResource(R.mipmap.ic_video_enlarge);
                atVideoReport.setVisibility(VISIBLE);
                mOnVideoPlayerEventListener.setMirror(false);
                speedType = 3;
                setIsSpeed(false);
                mOnVideoPlayerEventListener.setSpeed(SPEED_100);
                if (null != onVideoDetailListener) {
                    onVideoDetailListener.onVideoChange(true);
                }
                break;
            case ChouVideoPlayer.MODE_FULL_SCREEN:
                atVideoBack.setVisibility(View.VISIBLE);
                atVideoSpeed.setVisibility(View.VISIBLE);
                // atVideoRight.setVisibility(View.VISIBLE);
                // atVideoRew.setVisibility(VISIBLE);
                atVideoReport.setVisibility(View.GONE);
                atVideoFullScreen.setImageResource(R.mipmap.ic_video_shrink);
                atVideoReport.setVisibility(GONE);
                setIsSpeed(true);
                if (null != onVideoDetailListener) {
                    onVideoDetailListener.onVideoChange(false);
                }
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
        if (visible) {
            atVideoTop.setAnimation(showTop);
            atVideoBottom.setAnimation(showBottom);
            // atVideoRight.setAnimation(showRight);
        } else {
            atVideoTop.setAnimation(hindTop);
            atVideoBottom.setAnimation(hindBottom);
            // atVideoRight.setAnimation(hindRight);
        }
        atVideoTop.setVisibility(visible ? View.VISIBLE : View.GONE);
        atVideoBottom.setVisibility(visible ? View.VISIBLE : View.GONE);
        // if (isShowRight) {
        //     atVideoRight.setVisibility(visible ? View.VISIBLE : View.GONE);
        // }
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
