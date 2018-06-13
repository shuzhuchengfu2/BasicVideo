package barray.widget.video.ui;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

import barray.widget.video.R;
import barray.widget.video.utils.LogUtil;

public class MediaPlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener,SurfaceHolder.Callback {
    private SurfaceView surfaceVideo;
    private MediaPlayer player;
    private Display currDisplay;

    private static String TAG = "MediaPlayerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        String videoPath = getIntent().getStringExtra("videoPath");
        if (TextUtils.isEmpty(videoPath)) {
            Toast.makeText(getApplicationContext(), "播放失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        surfaceVideo = findViewById(R.id.video_surface);
        SurfaceHolder surfaceHolder = surfaceVideo.getHolder();
        // 设置监听
        surfaceHolder.addCallback(this);
        //为了可以播放视频或者使用Camera预览，我们需要指定其Buffer类型
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //实例化MediaPlayer对象
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnInfoListener(this);
        player.setOnPreparedListener(this);
        player.setOnSeekCompleteListener(this);
        player.setOnVideoSizeChangedListener(this);
        try {
            player.setDataSource(videoPath);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setScreenOnWhilePlaying(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        currDisplay = this.getWindowManager().getDefaultDisplay();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // 当MediaPlayer播放完成后触发
        LogUtil.d(TAG, "::   onCompletion");
        this.finish();
    }

    @Override
    public boolean onError(MediaPlayer mp, int whatError, int extra) {
        LogUtil.d(TAG, "::   onError");
        switch (whatError) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                LogUtil.d(TAG, "::   onError [ MEDIA_ERROR_SERVER_DIED ]");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                LogUtil.d(TAG, "::   onError [ MEDIA_ERROR_UNKNOWN ]");
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int whatInfo, int extra) {
        // 当一些特定信息出现或者警告时触发
        LogUtil.d(TAG, "::   onInfo");
        switch(whatInfo){
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                LogUtil.d(TAG, "::   onError [ MEDIA_INFO_BAD_INTERLEAVING ]");
                break;
            case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                LogUtil.d(TAG, "::   onError [ MEDIA_INFO_METADATA_UPDATE ]");
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                LogUtil.d(TAG, "::   onError [ MEDIA_INFO_VIDEO_TRACK_LAGGING ]");
                break;
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                LogUtil.d(TAG, "::   onError [ MEDIA_INFO_NOT_SEEKABLE ]");
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        LogUtil.d(TAG, "::   onPrepared");
        // 当prepare完成后，该方法触发，在这里我们播放视频

        //首先取得video的宽和高
        int vWidth = player.getVideoWidth();
        int vHeight = player.getVideoHeight();

        if(vWidth > currDisplay.getWidth() || vHeight > currDisplay.getHeight()){
            //如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
            float wRatio = (float)vWidth/(float)currDisplay.getWidth();
            float hRatio = (float)vHeight/(float)currDisplay.getHeight();

            //选择大的一个进行缩放
            float ratio = Math.max(wRatio, hRatio);

            vWidth = (int)Math.ceil((float)vWidth/ratio);
            vHeight = (int)Math.ceil((float)vHeight/ratio);

            //设置surfaceView的布局参数
            surfaceVideo.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight));
        }

        //然后开始播放视频
        player.start();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
            // seek操作完成时触发
        LogUtil.d(TAG, "::   onSeekComplete");
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        // 当video大小改变时触发
        //这个方法在设置player的source后至少触发一次
        LogUtil.d(TAG, "::   onVideoSizeChanged [ width:" + width + ",height: " + height + " ]");
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 当SurfaceView中的Surface被创建的时候被调用
        LogUtil.d(TAG, "::   surfaceCreated");
        //在这里我们指定MediaPlayer在当前的Surface中进行播放
        player.setDisplay(holder);
        //在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了
        player.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 当Surface尺寸等参数改变时触发
        LogUtil.d(TAG, "::   surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.d(TAG, "::   surfaceDestroyed");
    }
}
