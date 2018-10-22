package com.chou.android.mediaplayerlibrary;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import android.widget.Toast;
import com.chou.android.mediaplayerlibrary.utils.ChouPlayerUtil;
import com.chou.android.mediaplayerlibrary.utils.LogUtil;
import com.chou.android.mediaplayerlibrary.view.VideoTextureView;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class ChouVideoPlayer extends FrameLayout implements OnVideoPlayerEventListener,
    TextureView.SurfaceTextureListener {
    /**
     * 播放状态
     **/
    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_BUFFERING_PLAYING = 5;
    public static final int STATE_BUFFERING_PAUSED = 6;
    public static final int STATE_COMPLETED = 7;

    /**
     * 播放模式
     **/
    public static final int MODE_NORMAL = 10;
    public static final int MODE_FULL_SCREEN = 11;
    /**
     * 视频样式（1 横屏 ， 0竖屏）
     */
    private static final int VIDEO_LAND = 1;
    private static final int VIDEO_VERTICAL = 0;
    private int videoViewType = VIDEO_LAND;

    /**
     * 设置默认
     */
    private int mCurrentState = STATE_IDLE;
    private int mCurrentMode = MODE_NORMAL;

    /**
     * 是否开启手势
     */
    public boolean isOpenGesture = true;

    private Context mContext;
    private AudioManager mAudioManager;
    /**
     * Ijk播放器
     */
    private IjkMediaPlayer mMediaPlayer;
    private FrameLayout mContainer;
    private VideoTextureView mTextureView;
    private VideoPlayerBaseController mController;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private String mVideoPath;
    private int mBufferPercentage;
    private long skipToPosition;
    private long savedPlayPosition;
    private boolean continueFromLastPosition = true;

    public ChouVideoPlayer(@NonNull Context context) {
        this(context, null);
    }


    public ChouVideoPlayer(
        @NonNull Context context,
        @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }


    private void init() {
        mContainer = new FrameLayout(mContext);
        mContainer.setBackgroundColor(Color.BLACK);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mContainer, params);
    }


    public void setController(VideoPlayerBaseController controller) {
        mContainer.removeView(mContainer);
        mController = controller;
        mController.reset();
        mController.setVideoPlayerView(this);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(mController, params);
    }


    /**
     * 设置播放地址
     */
    @Override
    public void setVideoPath(String pathUrl) {
        this.mVideoPath = pathUrl;
    }


    /**
     * 视频的横竖屏样式
     * @param type
     */
    @Override public void setVideoViewType(int type) {
        videoViewType = type;
    }

    @Override
    public void start() {
        if (mCurrentState == STATE_IDLE) {
            VideoPlayerManager.instance().setCurrentVideoPlayer(this);
            initAudioManager();
            initMediaPlayer();
            initTextureView();
            addTextureView();
        } else if (mCurrentState == STATE_PAUSED || mCurrentState == STATE_BUFFERING_PAUSED) {
            this.mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
        } else if (mCurrentState == STATE_COMPLETED) {//AB循环结束自动停掉并播放
            this.mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
        } else {
            Log.d("videoLog====", "只有在mCurrentState == STATE_IDLE时才能调用start方法.");
        }
    }


    private void initAudioManager() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        }
    }


    private void initMediaPlayer() {
        mMediaPlayer = new IjkMediaPlayer();
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }


    private void initTextureView() {
        if (mTextureView == null) {
            mTextureView = new VideoTextureView(mContext);
            mTextureView.setSurfaceTextureListener(this);
        }
    }


    private void addTextureView() {
        mContainer.removeView(mTextureView);
        LayoutParams params = new LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            Gravity.CENTER);
        mContainer.addView(mTextureView, 0, params);
    }


    @Override
    public void start(long position) {
        skipToPosition = position;
        start();
    }


    @Override
    public void restart() {
        if (mCurrentState == STATE_PAUSED) {
            mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
            mController.onPlayStateChanged(mCurrentState);
            LogUtil.d("STATE_PLAYING");
        } else if (mCurrentState == STATE_BUFFERING_PAUSED) {
            mMediaPlayer.start();
            mCurrentState = STATE_BUFFERING_PLAYING;
            mController.onPlayStateChanged(mCurrentState);
            LogUtil.d("STATE_BUFFERING_PLAYING");
        } else if (mCurrentState == STATE_COMPLETED || mCurrentState == STATE_ERROR) {
            continueFromLastPosition(false);
            mMediaPlayer.reset();
            openMediaPlayer();
        } else {
            LogUtil.d("在mCurrentState == " + mCurrentState + "时不能调用restart()方法.");
        }
    }


    @Override
    public void pause() {
        if (mCurrentState == STATE_PLAYING) {
            mMediaPlayer.pause();
            mCurrentState = STATE_PAUSED;
            mController.onPlayStateChanged(mCurrentState);
            LogUtil.d("STATE_PAUSED");
        }
        if (mCurrentState == STATE_BUFFERING_PLAYING) {
            mMediaPlayer.pause();
            mCurrentState = STATE_BUFFERING_PAUSED;
            mController.onPlayStateChanged(mCurrentState);
            LogUtil.d("STATE_BUFFERING_PAUSED");
        }
    }


    @Override
    public void seekTo(long pos) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(pos);
        }
    }


    /**
     * 设置视频缩放
     */
    @Override public void isVideoScaling(boolean isScaling) {
        AnimatorSet animatorSetsuofang = new AnimatorSet();//组合动画
        ObjectAnimator scaleX;
        ObjectAnimator scaleY;
        if (isScaling) {
            scaleX = ObjectAnimator.ofFloat(mContainer, "scaleX", 1f, 0.9f);
            scaleY = ObjectAnimator.ofFloat(mContainer, "scaleY", 1f, 0.9f);
        } else {
            scaleX = ObjectAnimator.ofFloat(mContainer, "scaleX", 0.9f, 1.0f);
            scaleY = ObjectAnimator.ofFloat(mContainer, "scaleY", 0.9f, 1.0f);
        }
        animatorSetsuofang.setDuration(1000);
        animatorSetsuofang.setInterpolator(new DecelerateInterpolator());
        animatorSetsuofang.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSetsuofang.start();
    }


    @Override
    public void setVolume(int volume) {
        if (mAudioManager != null) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        }
    }


    @Override
    public void setSpeed(float speed) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setSpeed(speed);
        }
    }
    /**
     * 是否从上一次的位置继续播放
     *
     * @param continueFromLastPosition true从上一次的位置继续播放
     */
    @Override
    public void continueFromLastPosition(boolean continueFromLastPosition) {
        this.continueFromLastPosition = continueFromLastPosition;
    }


    /**
     * 清除播放位置
     */
    @Override public void cleanLastPosition() {
        ChouPlayerUtil.cleanSavePlayPosition(mContext);
    }


    @Override
    public boolean isIdle() {
        return mCurrentState == STATE_IDLE;
    }


    @Override
    public boolean isPreparing() {
        return mCurrentState == STATE_PREPARING;
    }


    @Override
    public boolean isPrepared() {
        return mCurrentState == STATE_PREPARED;
    }


    @Override
    public boolean isBufferingPlaying() {
        return mCurrentState == STATE_BUFFERING_PLAYING;
    }


    @Override
    public boolean isBufferingPaused() {
        return mCurrentState == STATE_BUFFERING_PAUSED;
    }


    @Override
    public boolean isPlaying() {
        return mCurrentState == STATE_PLAYING;
    }


    @Override
    public boolean isPaused() {
        return mCurrentState == STATE_PAUSED;
    }


    @Override
    public boolean isError() {
        return mCurrentState == STATE_ERROR;
    }


    @Override
    public boolean isCompleted() {
        return mCurrentState == STATE_COMPLETED;
    }


    @Override
    public boolean isFullScreen() {
        return mCurrentMode == MODE_FULL_SCREEN;
    }


    @Override
    public boolean isNormal() {
        return mCurrentMode == MODE_NORMAL;
    }


    @Override
    public void isOpenGesture(Boolean gesture) {
        this.isOpenGesture = gesture;
    }


    @Override
    public boolean isGesture() {
        return isOpenGesture;
    }


    @Override
    public int getMaxVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        return 0;
    }


    @Override
    public int getVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        return 0;
    }


    @Override
    public long getDuration() {
        return mMediaPlayer != null ? mMediaPlayer.getDuration() : 0;
    }


    @Override
    public long getCurrentPosition() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
    }


    @Override
    public int getBufferPercentage() {
        return mBufferPercentage;
    }


    /**
     * 设置镜像
     */
    @Override
    public void setMirror(boolean mirror) {
          if (mTextureView != null) {
            mTextureView.setScaleX(mirror ? -1 : 1);
        }
    }


    /**
     * 倒退时间
     *
     * @param backTime 毫秒
     */
    @Override public void setRewind(long backTime) {
        if (mMediaPlayer != null) {
            long time = mMediaPlayer.getCurrentPosition() - backTime;
            mMediaPlayer.seekTo(time);
            Toast.makeText(mContext, "视频快退5s成功", Toast.LENGTH_SHORT).show();
        }
    }


    private void openMediaPlayer() {
        // 屏幕常亮
        mContainer.setKeepScreenOn(true);
        // 设置监听
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mMediaPlayer.setOnErrorListener(mOnErrorListener);
        mMediaPlayer.setOnInfoListener(mOnInfoListener);
        mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        // 设置dataSource
        try {
            mMediaPlayer.setDataSource(mVideoPath);
            if (mSurface == null) {
                mSurface = new Surface(mSurfaceTexture);
            }
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
            mController.onPlayStateChanged(mCurrentState);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e("打开播放器发生错误");
        }
    }


    private IjkMediaPlayer
        .OnPreparedListener mOnPreparedListener
        = new IjkMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            mCurrentState = STATE_PREPARED;
            mController.onPlayStateChanged(mCurrentState);
            LogUtil.d("onPrepared ——> STATE_PREPARED");
            // 从上次的保存位置播放
            mp.start();
            if (continueFromLastPosition) {
                savedPlayPosition = ChouPlayerUtil.getSavedPlayPosition(mContext, mVideoPath);
                mp.seekTo(savedPlayPosition);
            }
            // 跳到指定位置播放
            if (skipToPosition != 0) {
                mp.seekTo(skipToPosition);
            }
        }

    };
    private IjkMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener
        = new IjkMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
            mTextureView.adaptVideoSize(width, height);
        }
    };

    private IjkMediaPlayer.OnCompletionListener mOnCompletionListener
        = new IjkMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {
            mCurrentState = STATE_COMPLETED;
            mController.onPlayStateChanged(mCurrentState);
            // 清除屏幕常亮
            mContainer.setKeepScreenOn(false);
        }
    };

    private IjkMediaPlayer.OnErrorListener mOnErrorListener
        = new IjkMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            mCurrentState = STATE_ERROR;
            mController.onPlayStateChanged(mCurrentState);
            return true;
        }
    };

    private IjkMediaPlayer.OnInfoListener mOnInfoListener
        = new IjkMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {
            if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                // 播放器开始渲染
                mCurrentState = STATE_PLAYING;
                mController.onPlayStateChanged(mCurrentState);
            } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                // MediaPlayer暂时不播放，以缓冲更多的数据
                if (mCurrentState == STATE_PAUSED || mCurrentState == STATE_BUFFERING_PAUSED) {
                    mCurrentState = STATE_BUFFERING_PAUSED;
                } else {
                    mCurrentState = STATE_BUFFERING_PLAYING;
                }
                mController.onPlayStateChanged(mCurrentState);
            } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                // 填充缓冲区后，MediaPlayer恢复播放/暂停
                if (mCurrentState == STATE_BUFFERING_PLAYING) {
                    mCurrentState = STATE_PLAYING;
                    mController.onPlayStateChanged(mCurrentState);
                }
                if (mCurrentState == STATE_BUFFERING_PAUSED) {
                    mCurrentState = STATE_PAUSED;
                    mController.onPlayStateChanged(mCurrentState);
                }
            } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
                // 视频旋转了extra度，需要恢复
                if (mTextureView != null) {
                    mTextureView.setRotation(extra);
                    LogUtil.d("视频旋转角度：" + extra);
                }
            } else if (what == IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
                LogUtil.d("视频不能seekTo，为直播视频");
            } else {
                LogUtil.d("onInfo ——> what：" + what);
            }
            return true;
        }

    };

    private IjkMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener
        = new IjkMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            mBufferPercentage = percent;
        }
    };


    /**
     * 全屏
     */
    @Override
    public void enterFullScreen() {
        if (mCurrentMode == MODE_FULL_SCREEN) {
            return;
        }

        // 隐藏ActionBar、状态栏，并横屏
        ChouPlayerUtil.hideActionBar(mContext);
        if (videoViewType == VIDEO_LAND) {
            ChouPlayerUtil.scanForActivity(mContext)
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        ViewGroup contentView = (ViewGroup) ChouPlayerUtil.scanForActivity(mContext)
            .findViewById(android.R.id.content);
        contentView.setBackgroundColor(Color.BLACK);
        this.removeView(mContainer);
        LayoutParams params = new LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
        if (videoViewType == VIDEO_VERTICAL){
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,getWidth()/2,0);
            scaleAnimation.setDuration(300);
            mContainer.setAnimation(scaleAnimation);
        }
        contentView.addView(mContainer, params);

        mCurrentMode = MODE_FULL_SCREEN;
        mController.onPlayModeChanged(mCurrentMode);
    }


    /**
     * 退出全屏
     */
    @Override
    public boolean exitFullScreen() {
        if (mCurrentMode == MODE_FULL_SCREEN) {
            ChouPlayerUtil.showActionBar(mContext);
            if (videoViewType == VIDEO_LAND) {
                ChouPlayerUtil.scanForActivity(mContext)
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            ViewGroup contentView = (ViewGroup) ChouPlayerUtil.scanForActivity(mContext)
                .findViewById(android.R.id.content);
            contentView.setBackgroundColor(Color.WHITE);
            contentView.removeView(mContainer);
            LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
            if (videoViewType == VIDEO_VERTICAL){
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1f,getWidth()/2,0);
                scaleAnimation.setDuration(300);
                mContainer.setAnimation(scaleAnimation);
            }
            this.addView(mContainer, params);
            mCurrentMode = MODE_NORMAL;
            mController.onPlayModeChanged(mCurrentMode);
            return true;
        }
        return false;
    }


    @Override
    public void releasePlayer() {
        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(null);
            mAudioManager = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mContainer.removeView(mTextureView);
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        mCurrentState = STATE_IDLE;
    }


    @Override
    public void release() {
        // 保存播放位置
        if (isPlaying() || isBufferingPlaying() || isBufferingPaused() || isPaused()) {
            ChouPlayerUtil.savePlayPosition(mContext, mVideoPath, getCurrentPosition());
        } else if (isCompleted()||isError()) {
            ChouPlayerUtil.savePlayPosition(mContext, mVideoPath, 0);
        }
        // 退出全屏
        if (isFullScreen()) {
            exitFullScreen();
        }
        mCurrentMode = MODE_NORMAL;

        // 释放播放器
        releasePlayer();

        // 恢复控制器
        if (mController != null) {
            mController.reset();
        }
        Runtime.getRuntime().gc();
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surfaceTexture;
            openMediaPlayer();
        } else {
            mTextureView.setSurfaceTexture(mSurfaceTexture);
        }
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }


    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return mSurfaceTexture == null;
    }


    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

}
