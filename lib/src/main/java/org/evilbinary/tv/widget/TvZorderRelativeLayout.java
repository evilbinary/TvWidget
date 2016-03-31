package org.evilbinary.tv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 作者:evilbinary on 2/2/16.
 * 邮箱:rootdebug@163.com
 */
public class TvZorderRelativeLayout extends RelativeLayout {
    private int position = 0;

    public TvZorderRelativeLayout(Context context) {
        super(context);

    }

    public TvZorderRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setChildrenDrawingOrderEnabled(true);
    }


    public void setCurrentPosition(int pos) {
        this.position = pos;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
            View focused = findFocus();
            int pos = indexOfChild(focused);
            if (pos >= 0 && pos < getChildCount()) {
                setCurrentPosition(pos);
                postInvalidate();
            }

        return super.dispatchKeyEvent(event);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        View v = getFocusedChild();
        int pos = indexOfChild(v);
        if (pos >= 0 && pos < childCount)
            setCurrentPosition(pos);

        if (i == childCount - 1) {//这是最后一个需要刷新的item
            return position;
        }
        if (i == position) {//这是原本要在最后一个刷新的item
            return childCount - 1;
        }
        return i;//正常次序的item
    }

}
