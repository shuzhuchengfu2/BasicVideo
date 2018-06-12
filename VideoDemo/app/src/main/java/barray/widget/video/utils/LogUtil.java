package barray.widget.video.utils;

import android.util.Log;

/**
 * Created by 熊德进 on 2018/6/12.
 */

public class LogUtil {
    public static int level = 0;
    public static final int V = 1;
    public static final int D = 2;
    public static final int I = 3;
    public static final int W = 4;
    public static final int E = 5;

    public static void setLevel(int level) {
        LogUtil.level = level;
    }

    public static void v(String tag, String log) {
        if (level <= V) {
            Log.v(tag, log);
        }
    }

    public static void d(String tag, String log) {
        if (level <= D) {
            Log.d(tag, log);
        }
    }

    public static void i(String tag, String log) {
        if (level <= I) {
            Log.i(tag, log);
        }
    }

    public static void w(String tag, String log) {
        if (level <= W) {
            Log.w(tag, log);
        }
    }

    public static void e(String tag, String log) {
        if (level <= E) {
            Log.e(tag, log);
        }
    }
}
