package barray.widget.video.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import barray.widget.video.utils.LogUtil;

/**
 * Created by 熊德进 on 2018/6/13.
 * 复制本地视频
 */

public class CopyVideoTask extends AsyncTask<String, Integer, Boolean> {

    public interface CopyResultListener{
        void copySuccess(String filePath);
        void copyFail();
    }

    public CopyResultListener copyResultListener;
    public static String TAG = "CopyVideoTask";
    private String filePath;
    private Context mContext;

    public CopyVideoTask(Context context,CopyResultListener copyResultListener){
        this.mContext = context.getApplicationContext();
        this.copyResultListener = copyResultListener;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        if (mContext == null ) return false;
        try {
            InputStream inputStream = mContext.getAssets().open("douyin.mp4");
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/video";
            } else {
                filePath = mContext.getFilesDir().getAbsolutePath() + "/video";
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
            if(copyResultListener != null){
                copyResultListener.copySuccess(filePath+"/douyin.mp4");
            }
        }else{
            LogUtil.d(TAG,"copy 失败");
        }
    }
}
