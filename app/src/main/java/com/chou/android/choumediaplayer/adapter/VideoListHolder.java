package com.chou.android.choumediaplayer.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chou.android.choumediaplayer.R;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.controllers.FocusVideoPlayerController;
import com.chou.android.mediaplayerlibrary.utils.ChouPlayerUtil;

/**
 * @author : zgz
 * @time :  2018/9/26 0026 13:39
 * @describe :列表播放holder
 **/
public class VideoListHolder extends BaseViewHolder
    implements FocusVideoPlayerController.OnFollowListener {

    private ImageView playView;

    private ImageView coverView;

    private String videoUrl = "";
    private Context mContext;
    private ChouVideoPlayer chouVideoPlayer;

    private boolean isSetTag;


    public VideoListHolder(View view, final Context context) {
        super(view);

        mContext = context;
        playView = (ImageView) itemView.findViewById(R.id.iv_start_video_list);
        coverView = (ImageView) itemView.findViewById(R.id.iv_bg_video_list);
        chouVideoPlayer = (ChouVideoPlayer) itemView.findViewById(R.id.video_video_list);
        chouVideoPlayer.isOpenGesture(false);
        FocusVideoPlayerController controller = new FocusVideoPlayerController(mContext);
        chouVideoPlayer.setController(controller);
        controller.setOnFollowListener(this);
        chouVideoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (videoUrl == null || videoUrl.isEmpty()) {
                    Toast.makeText(mContext, "空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (chouVideoPlayer.isPlaying()) {
                    chouVideoPlayer.pause();
                    playView.setImageResource(R.mipmap.bg_video_play_button);
                } else {
                    playerStart();
                }
            }
        });

    }


    public void setDataTag(int position, String videoUrl) {
        isSetTag = true;
        chouVideoPlayer.setVideoPath(videoUrl);
        if (chouVideoPlayer.isPlaying()) {
        } else {
            this.videoUrl = videoUrl;
            viewStopped();
        }

    }


    public boolean isSetTag() {
        return isSetTag;
    }


    public void playerStopped() {
        viewStopped();
    }


    public void playerStart() {
        playView.setVisibility(View.GONE);
        coverView.setVisibility(View.GONE);
        chouVideoPlayer.start();
    }


    private void viewStopped() {
        playView.setVisibility(View.VISIBLE);
        coverView.setVisibility(View.VISIBLE);
        chouVideoPlayer.pause();
    }


    public String getVideoUrl() {
        return videoUrl;
    }


    @Override public void pause() {
        playView.setVisibility(View.VISIBLE);
    }


    @Override public void start() {
        playView.setVisibility(View.GONE);
        coverView.setVisibility(View.GONE);
    }


    @Override public void reset() {
        playView.setVisibility(View.VISIBLE);
        coverView.setVisibility(View.VISIBLE);
    }

}
