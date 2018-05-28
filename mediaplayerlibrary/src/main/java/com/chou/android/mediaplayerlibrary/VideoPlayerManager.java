package com.chou.android.mediaplayerlibrary;

import android.content.Context;

/**
 * 视频播放器管理器.
 */
public class VideoPlayerManager {

    private ChouVideoPlayer mVideoPlayer;

    private VideoPlayerManager() {
    }

    private static VideoPlayerManager sInstance;

    public static synchronized VideoPlayerManager instance() {
        if (sInstance == null) {
            sInstance = new VideoPlayerManager();
        }
        return sInstance;
    }

    /**
     * 获取当前的player
     */
    public ChouVideoPlayer getCurrentVideoPlayer(Context context) {
        if (null == mVideoPlayer){
            mVideoPlayer = new ChouVideoPlayer(context);
        }
        return mVideoPlayer;
    }

    public void setCurrentVideoPlayer(ChouVideoPlayer videoPlayer) {
        if (mVideoPlayer != videoPlayer) {
            releaseVideoPlayer();
            mVideoPlayer = videoPlayer;
        }
    }

    /**
     * 暂停player
     */
    public void pauseVideoPlayer() {
        if (mVideoPlayer != null && (mVideoPlayer.isPlaying() || mVideoPlayer.isBufferingPlaying())) {
            mVideoPlayer.pause();
        }
    }

    /**
     * 重新开始player
     */
    public void resumeVideoPlayer() {
        if (mVideoPlayer != null && (mVideoPlayer.isPaused() || mVideoPlayer.isBufferingPaused())) {
            mVideoPlayer.restart();
        }
    }

    /**
     * 释放player
     */
    public void releaseVideoPlayer() {
        if (mVideoPlayer != null) {
            mVideoPlayer.release();
            mVideoPlayer = null;
        }
    }

    /**
     * 返回键
     */
    public boolean onBackspace() {
        if (mVideoPlayer != null) {
            if (mVideoPlayer.isFullScreen()) {
                return mVideoPlayer.exitFullScreen();
            }
        }
        return false;
    }
}
