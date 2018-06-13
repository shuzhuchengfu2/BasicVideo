package barray.widget.video.ui;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import barray.widget.video.R;

public class VideoViewActivity extends AppCompatActivity {
    private VideoView vvPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        vvPlayer = findViewById(R.id.vv_player);
        String videoPath = getIntent().getStringExtra("videoPath");
        playWithVideoView(videoPath);
    }


    public void playWithVideoView(String filePath) {
        Uri uri = Uri.parse(filePath);
        vvPlayer.setMediaController(new MediaController(this));
        vvPlayer.setVideoURI(uri);
        vvPlayer.start();
        vvPlayer.requestFocus();
    }
}
