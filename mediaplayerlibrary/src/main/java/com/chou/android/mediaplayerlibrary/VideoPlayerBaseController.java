package com.chou.android.mediaplayerlibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.chou.android.mediaplayerlibrary.utils.ChouPlayerUtil;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * 继承此类 定制自定义的View
 */
public abstract class VideoPlayerBaseController extends FrameLayout  implements View.OnTouchListener {

    private Context mContext;
    protected OnVideoPlayerEventListener mOnVideoPlayerEventListener;

    private ScheduledExecutorService mUpdateProgressTimer;
    private TimerTask mUpdateProgressTimerTask;

    private float mDownX;
    private float mDownY;
    private boolean mNeedChangePosition;
    private boolean mNeedChangeVolume;
    private boolean mNeedChangeBrightness;
    private static final int THRESHOLD = 80;
    private long mGestureDownPosition;
    private float mGestureDownBrightness;
    private int mGestureDownVolume;
    private long mNewPosition;

    public VideoPlayerBaseController(
        @NonNull Context context) {
        super(context);
        mContext = context;
        this.setOnTouchListener(this);
    }
    public void setVideoPlayerView(OnVideoPlayerEventListener onVideoPlayerEventListener) {
        mOnVideoPlayerEventListener = onVideoPlayerEventListener;
    }

    /**
     * 当播放器的播放状态发生变化，在此方法中国你更新不同的播放状态的UI
     * @param playState 播放状态：
     *                  <ul>
     *                  <li>{@link ChouVideoPlayer#STATE_IDLE}</li>
     *                  <li>{@link ChouVideoPlayer#STATE_PREPARING}</li>
     *                  <li>{@link ChouVideoPlayer#STATE_PREPARED}</li>
     *                  <li>{@link ChouVideoPlayer#STATE_PLAYING}</li>
     *                  <li>{@link ChouVideoPlayer#STATE_PAUSED}</li>
     *                  <li>{@link ChouVideoPlayer#STATE_BUFFERING_PLAYING}</li>
     *                  <li>{@link ChouVideoPlayer#STATE_BUFFERING_PAUSED}</li>
     *                  <li>{@link ChouVideoPlayer#STATE_ERROR}</li>
     *                  <li>{@link ChouVideoPlayer#STATE_COMPLETED}</li>
     *                  </ul>
     */
    protected abstract void onPlayStateChanged(int playState);

    /**
     * 当播放器的播放模式发生变化，在此方法中更新不同模式下的控制器界面。
     * @param playMode 播放器的模式：
     *                 <ul>
     *                 <li>{@link ChouVideoPlayer#MODE_NORMAL}</li>
     *                 <li>{@link ChouVideoPlayer#MODE_FULL_SCREEN}</li>
     *                 </ul>
     */
    protected abstract void onPlayModeChanged(int playMode);

    /**
     * 重置控制器，将控制器恢复到初始状态
     */
    protected abstract void reset();

    /**
     * 开启更新进度的计时器（每秒）
     */
    protected void startUpdateProgressTimer() {
        cancelUpdateProgressTimer();
        if (mUpdateProgressTimer == null) {
            mUpdateProgressTimer = newScheduledThreadPool(1);
        }
        if (mUpdateProgressTimerTask == null) {
            mUpdateProgressTimerTask = new TimerTask() {
                @Override
                public void run() {
                    VideoPlayerBaseController.this.post(new Runnable() {
                        @Override
                        public void run() {
                            updateProgress();
                        }
                    });
                }
            };
        }
        mUpdateProgressTimer.scheduleAtFixedRate(mUpdateProgressTimerTask, 0,1000, TimeUnit.MICROSECONDS);
    }

    /**
     * 取消更新进度的计时器
     */
    protected void cancelUpdateProgressTimer() {
        if (mUpdateProgressTimer != null) {
            mUpdateProgressTimer.shutdown();
            mUpdateProgressTimer = null;
        }
        if (mUpdateProgressTimerTask != null) {
            mUpdateProgressTimerTask.cancel();
            mUpdateProgressTimerTask = null;
        }
    }
    /**
     * 更新进度，包括进度条进度，展示的当前播放位置时长，总时长等
     */
    protected abstract void updateProgress();
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //是否需要手势
        if (!mOnVideoPlayerEventListener.isGesture()){
            return false;
        }
        // 只有全屏的时候才能拖动位置、亮度、声音
        // if (!mOnVideoPlayerEventListener.isFullScreen()) {
        //     return false;
        // }
        // 只有在播放、暂停、缓冲的时候能够拖动改变位置、亮度和声音
        if (mOnVideoPlayerEventListener.isIdle()
            || mOnVideoPlayerEventListener.isError()
            || mOnVideoPlayerEventListener.isPreparing()
            || mOnVideoPlayerEventListener.isPrepared()
            || mOnVideoPlayerEventListener.isCompleted()) {
            hideChangePosition();
            hideChangeBrightness();
            hideChangeVolume();
            return false;
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                mNeedChangePosition = false;
                mNeedChangeVolume = false;
                mNeedChangeBrightness = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = x - mDownX;
                float deltaY = y - mDownY;
                float absDeltaX = Math.abs(deltaX);
                float absDeltaY = Math.abs(deltaY);
                if (!mNeedChangePosition && !mNeedChangeVolume && !mNeedChangeBrightness) {
                    // 只有在播放、暂停、缓冲的时候能够拖动改变位置、亮度和声音
                    if (absDeltaX >= THRESHOLD) {
                        cancelUpdateProgressTimer();
                        mNeedChangePosition = true;
                        mGestureDownPosition = mOnVideoPlayerEventListener.getCurrentPosition();
                    } else if (absDeltaY >= THRESHOLD) {
                        if (mDownX < getWidth() * 0.5f) {
                            // 左侧改变亮度
                            mNeedChangeBrightness = true;
                            mGestureDownBrightness = ChouPlayerUtil.scanForActivity(mContext)
                                .getWindow().getAttributes().screenBrightness;
                        } else {
                            // 右侧改变声音
                            mNeedChangeVolume = true;
                            mGestureDownVolume = mOnVideoPlayerEventListener.getVolume();
                        }
                    }
                }
                if (mNeedChangePosition) {
                    long duration = mOnVideoPlayerEventListener.getDuration();
                    long toPosition = (long) (mGestureDownPosition + duration * deltaX / getWidth());
                    mNewPosition = Math.max(0, Math.min(duration, toPosition));
                    int newPositionProgress = (int) (100f * mNewPosition / duration);
                    showChangePosition(duration, newPositionProgress,true);
                }
                if (mNeedChangeBrightness) {
                    deltaY = -deltaY;
                    float deltaBrightness = deltaY * 3 / getHeight();
                    float newBrightness = mGestureDownBrightness + deltaBrightness;
                    newBrightness = Math.max(0, Math.min(newBrightness, 1));
                    float newBrightnessPercentage = newBrightness;
                    WindowManager.LayoutParams params = ChouPlayerUtil.scanForActivity(mContext)
                        .getWindow().getAttributes();
                    params.screenBrightness = newBrightnessPercentage;
                    ChouPlayerUtil.scanForActivity(mContext).getWindow().setAttributes(params);
                    int newBrightnessProgress = (int) (100f * newBrightnessPercentage);
                    showChangeBrightness(newBrightnessProgress);
                }
                if (mNeedChangeVolume) {
                    deltaY = -deltaY;
                    int maxVolume = mOnVideoPlayerEventListener.getMaxVolume();
                    int deltaVolume = (int) (maxVolume * deltaY * 3 / getHeight());
                    int newVolume = mGestureDownVolume + deltaVolume;
                    newVolume = Math.max(0, Math.min(maxVolume, newVolume));
                    mOnVideoPlayerEventListener.setVolume(newVolume);
                    int newVolumeProgress = (int) (100f * newVolume / maxVolume);
                    showChangeVolume(newVolumeProgress);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mNeedChangePosition) {
                    mOnVideoPlayerEventListener.seekTo(mNewPosition);
                    hideChangePosition();
                    startUpdateProgressTimer();
                    return true;
                }
                if (mNeedChangeBrightness) {
                    hideChangeBrightness();
                    return true;
                }
                if (mNeedChangeVolume) {
                    hideChangeVolume();
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * 手势左右滑动改变播放位置时，显示控制器中间的播放位置变化视图，
     * 在手势滑动ACTION_MOVE的过程中，会不断调用此方法。
     *
     * @param duration            视频总时长ms
     * @param newPositionProgress 新的位置进度，取值0到100。
     */
    protected abstract void showChangePosition(long duration, int newPositionProgress , boolean isShow);

    /**
     * 手势左右滑动改变播放位置后，手势up或者cancel时，隐藏控制器中间的播放位置变化视图，
     * 在手势ACTION_UP或ACTION_CANCEL时调用。
     */
    protected abstract void hideChangePosition();

    /**
     * 手势在右侧上下滑动改变音量时，显示控制器中间的音量变化视图，
     * 在手势滑动ACTION_MOVE的过程中，会不断调用此方法。
     *
     * @param newVolumeProgress 新的音量进度，取值1到100。
     */
    protected abstract void showChangeVolume(int newVolumeProgress);

    /**
     * 手势在左侧上下滑动改变音量后，手势up或者cancel时，隐藏控制器中间的音量变化视图，
     * 在手势ACTION_UP或ACTION_CANCEL时调用。
     */
    protected abstract void hideChangeVolume();

    /**
     * 手势在左侧上下滑动改变亮度时，显示控制器中间的亮度变化视图，
     * 在手势滑动ACTION_MOVE的过程中，会不断调用此方法。
     *
     * @param newBrightnessProgress 新的亮度进度，取值1到100。
     */
    protected abstract void showChangeBrightness(int newBrightnessProgress);

    /**
     * 手势在左侧上下滑动改变亮度后，手势up或者cancel时，隐藏控制器中间的亮度变化视图，
     * 在手势ACTION_UP或ACTION_CANCEL时调用。
     */
    protected abstract void hideChangeBrightness();
}
