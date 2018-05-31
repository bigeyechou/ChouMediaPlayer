package com.chou.android.mediaplayerlibrary.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private LinearLayout  boxVideoLoading;
    private LinearLayout boxVideoError;
    private TextView boxVideoRetry;
    private ImageView boxVideoPrevious;
    private ImageView boxVideoNext;
    private ImageView boxVideoFullScreen;

    private Context mContext;

    public BoxVideoPlayerController(
        @NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }


    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.at_video_player_controller, this, true);
        boxVideoBack.findViewById(R.id.box_video_back);
        boxVideoControl.findViewById(R.id.box_video_control);
        boxVideoMirror.findViewById(R.id.box_video_mirror);
        boxVideoSpeed.findViewById(R.id.box_video_speed);
        boxVideoLoading.findViewById(R.id.box_video_loading);
        boxVideoError.findViewById(R.id.box_video_error);
        boxVideoRetry.findViewById(R.id.box_video_retry);
        boxVideoPrevious.findViewById(R.id.box_video_previous);
        boxVideoNext.findViewById(R.id.box_video_next);
        boxVideoFullScreen.findViewById(R.id.box_video_full_screen);

        boxVideoBack.setOnClickListener(this);
        boxVideoMirror.setOnClickListener(this);
        boxVideoSpeed.setOnClickListener(this);
        boxVideoRetry.setOnClickListener(this);
        boxVideoPrevious.setOnClickListener(this);
        boxVideoNext.setOnClickListener(this);
        boxVideoFullScreen.setOnClickListener(this);
    }


    @Override public void onClick(View v) {
        switch (v.getId()){
            // case R.id.box_video_back:
            //
            //     break;
        }
    }


    @Override protected void onPlayStateChanged(int playState) {

    }


    @Override protected void onPlayModeChanged(int playMode) {

    }


    @Override protected void reset() {

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
