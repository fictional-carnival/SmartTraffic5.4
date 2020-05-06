package com.lenovo.smarttraffic.ui.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;

public class VideoActivity extends BaseActivity {
    private ImageView image;
    private VideoView video;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_video;
    }

    private void initView() {
        initToolBar(findViewById(R.id.toolbar), true, "视频播放");

        video = (VideoView) findViewById(R.id.video);
    }

    private void initData() {
        MediaController mediaController = new MediaController(this);
        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.a));
        mediaController.setMediaPlayer(video);
        video.setMediaController(mediaController);
        video.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mediaController.isShowing()) {
                    mediaController.show(0);
                }
            }
        }, 500);

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InitApp.toast("视频播放结束");
                        finish();
                    }
                }, 1500);
            }
        });
    }
}
