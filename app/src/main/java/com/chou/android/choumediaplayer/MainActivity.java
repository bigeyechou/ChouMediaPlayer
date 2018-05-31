package com.chou.android.choumediaplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chou.android.mediaplayerlibrary.ChouVideoPlayer;
import com.chou.android.mediaplayerlibrary.VideoPlayerManager;
import com.chou.android.mediaplayerlibrary.controllers.AtVideoPlayerController;
import com.danikula.videocache.HttpProxyCacheServer;

import static com.chou.android.choumediaplayer.App.getProxy;

public class MainActivity extends AppCompatActivity {



    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        ButterKnife.bind(this);

    }


    @Override protected void onResume() {
        super.onResume();
    }


    @Override protected void onPause() {
        super.onPause();
    }


    @Override protected void onDestroy() {
        super.onDestroy();

    }

}
