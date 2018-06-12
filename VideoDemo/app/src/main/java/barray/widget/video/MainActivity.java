package barray.widget.video;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import barray.widget.video.utils.LogUtil;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "MainActivity";
    private VideoView vvPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vvPlayer = findViewById(R.id.vv_player);
        CopyVideoTask copyVideoTask = new CopyVideoTask();
        copyVideoTask.execute("");
    }


    public class CopyVideoTask extends AsyncTask<String, Integer, Boolean> {
        private String filePath;
        @Override
        protected Boolean doInBackground(String... strings) {
            if (strings == null) return false;
            try {
                InputStream inputStream = getAssets().open("douyin.mp4");
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/video";
                } else {
                    filePath = getFilesDir().getAbsolutePath() + "/video";
                }
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                OutputStream outputStream = new FileOutputStream(filePath + "/douyin.mp4");
                long total = inputStream.available();
                long current = 0;
                int len;
                byte[] bytes = new byte[1024 * 4];
                while ((len = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                    current += len;
                    publishProgress((int) (current * 100 / total));
                }
                outputStream.flush();
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LogUtil.d(TAG,"开始 copy...");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            LogUtil.d(TAG,"copy 中... "+values[0]+" %");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                LogUtil.d(TAG,"copy 成功");
//                sysPlayer(filePath+"/douyin.mp4");
                playWithVideoView(filePath+"/douyin.mp4");
            }else{
                LogUtil.d(TAG,"copy 失败");
            }
        }
    }

    public void playWithVideoView(String filePath){
        Uri uri = Uri.parse(filePath);
        vvPlayer.setMediaController(new MediaController(this));
        vvPlayer.setVideoURI(uri);
        vvPlayer.start();
        vvPlayer.requestFocus();
    }



    /**
     * 系统播放器
     * @param path
     */
    public void sysPlayer(String path){
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"video/*");
        startActivity(intent);
    }
}
