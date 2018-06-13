package barray.widget.video.jsplayer.danmu;

import android.view.View;

/**
 * Created by 熊德进 on 2018/6/13.
 */

public abstract class DanmuAdapter<M> {
    /**
     * 获取类型数组
     *
     * @return
     */
    public abstract int[] getViewTypeArray();

    /**
     * 获取单行弹幕 高度
     *
     * @return
     */
    public abstract int getSingleLineHeight();

    /**
     * 获取itemView
     *
     * @param entry
     * @param convertView
     * @return
     */
    public abstract View getView(M entry, View convertView);
}
