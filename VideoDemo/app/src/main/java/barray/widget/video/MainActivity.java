package barray.widget.video;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import barray.widget.video.task.CopyVideoTask;
import barray.widget.video.ui.VideoViewActivity;

public class MainActivity extends AppCompatActivity {

    private String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CopyVideoTask copyVideoTask = new CopyVideoTask(this, new CopyVideoTask.CopyResultListener() {
            @Override
            public void copySuccess(String filePath) {
                videoPath = filePath;
                Toast.makeText(getApplicationContext(),"视频初始化成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void copyFail() {
                Toast.makeText(getApplicationContext(),"视频初始化失败",Toast.LENGTH_SHORT).show();
            }
        });
        copyVideoTask.execute("");
    }

    /**
     * 系统播放器
     *
     * @param view
     */
    public void systemPlayer(View view) {
        if(videoPath == null) return;
        Uri uri = Uri.parse(videoPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "video/*");
        startActivity(intent);
    }

    /**
     * VideoView 播放
     * @param view
     */
    public void videoViewPlayer(View view){
        if(videoPath == null) return;
        Intent intent = new Intent(this, VideoViewActivity.class);
        intent.putExtra("videoPath",videoPath);
        startActivity(intent);
    }



}
