package barray.widget.video.jsplayer.danmu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import barray.widget.video.R;
import barray.widget.video.jsplayer.bean.InnerEntity;
import barray.widget.video.jsplayer.listener.OnItemClickListener;

/**
 * Created by 熊德进 on 2018/6/13.
 * 弹幕
 */

public class DanmuView extends ViewGroup {
    // 移动速度
    public static final int LOWER_SPEED = 1;
    public static final int NORMAL_SPEED = 4;
    public static final int HIGH_SPEED = 8;

    // 出现位置
    public final static int GRAVITY_TOP = 1;    //001
    public final static int GRAVITY_CENTER = 2;  //010
    public final static int GRAVITY_BOTTOM = 4;  //100
    public final static int GRAVITY_FULL = 7;   //111


    private int gravity = GRAVITY_FULL;

    private int speed = 4;

    private int spanCount = 6;

    private int WIDTH, HEIGHT;

    private int singltLineHeight;

    private DanmuAdapter<DanmuModel> adapter;

    public List<View> spanList;

    private OnItemClickListener onItemClickListener;


    public DanmuView(Context context) {
        this(context, null);
    }

    public DanmuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DanmuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        spanList = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        WIDTH = MeasureSpec.getSize(widthMeasureSpec);
        HEIGHT = MeasureSpec.getSize(heightMeasureSpec);

        spanCount = HEIGHT / singltLineHeight;
        // 创建同样大小的view集合
        for (int i = 0; i < spanCount; i++) {
            if (spanList.size() < spanCount) {
                spanList.add(i, null);
            }
        }
    }

    /**
     * 添加弹幕view
     *
     * @param model
     */
    public void addDanmu(final DanmuModel model) {
        if (adapter == null) {
            throw new Error("DanmuAdapter(an interface need to be implemented) can't be null,you should call setAdapter firstly");
        }

        View dmView = adapter.getView(model, null);
        addTypeView(model, dmView, false);

        //添加监听
        dmView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(model);
            }
        });
    }

    private void addTypeView(DanmuModel model, View child, boolean b) {
        super.addView(child);
        child.measure(0, 0);
        //把宽高拿到，宽高都是包含ItemDecorate的尺寸
        int width = child.getMeasuredWidth();
        int height = child.getMeasuredHeight();

        //获取最佳行数
        int bestLine = getBestLine();
        // 设置子view位置
        child.layout(WIDTH, singltLineHeight * bestLine, WIDTH + width,
                singltLineHeight * bestLine + height);

        InnerEntity innerEntity = (InnerEntity) child.getTag(R.id.tag_inner_entity);
        //TODO
        if (!isReused || innerEntity == null) {
            innerEntity = new InnerEntity();
        }
        innerEntity.model = model;
        innerEntity.bestLine = bestLine;
        child.setTag(R.id.tag_inner_entity, innerEntity);

        spanList.set(bestLine, child);
    }


    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getScanCount() {
        return spanCount;
    }

    public void setScanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    /**
     * 计算最佳位置
     *
     * @return
     */
    private int getBestLine() {
        // 转换为2进制
        int gewei = gravity % 2;
        int temp = gravity / 2;
        int shiwei = temp % 2;
        temp = temp / 2;
        int baiwei = temp % 2;

        // 将所有的行分为三份,前两份行数相同,将第一份的行数四舍五入
        int firstLine = (int) (spanCount / 3.0 + 0.5);

        List<Integer> legalLines = new ArrayList<>();
        if (gewei == 1) {
            for (int i = 0; i < firstLine; i++) {
                legalLines.add(i);
            }
        }
        if (shiwei == 1) {
            for (int i = firstLine; i < firstLine * 2; i++) {
                legalLines.add(i);
            }
        }
        if (baiwei == 1) {
            for (int i = firstLine * 2; i < spanCount; i++) {
                legalLines.add(i);
            }
        }

        int bestLine = 0;
        // 如果有空行,将空行返回
        for (int i = 0; i < spanCount; i++) {
            if (spanList.get(i) == null) {
                bestLine = i;
                if (legalLines.contains(bestLine))
                    return bestLine;
            }
        }

        float minSpace = Integer.MAX_VALUE;
        // 没有空行，就找最大空间的
        for (int i = spanCount - 1; i >= 0; i--) {
            if (legalLines.contains(i)) {
                if (spanList.get(i).getX() + spanList.get(i).getWidth() <= minSpace) {
                    minSpace = spanList.get(i).getX() + spanList.get(i).getWidth();
                    bestLine = i;
                }
            }
        }

        return bestLine;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
