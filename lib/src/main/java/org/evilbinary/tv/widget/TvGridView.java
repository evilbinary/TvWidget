package org.evilbinary.tv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 作者:evilbinary on 4/2/16.
 * 邮箱:rootdebug@163.com
 */
public class TvGridView extends GridView {
    private int position=0;

    public TvGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    protected void setChildrenDrawingOrderEnabled(boolean enabled) {
        super.setChildrenDrawingOrderEnabled(enabled);
    }
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        position = getSelectedItemPosition() - getFirstVisiblePosition();
        if(position<0){
            return i;
        }else{
            if(i == childCount - 1){
                if(position>i){
                    position=i;
                }
                return position;
            }
            if(i == position){
                return childCount - 1;
            }
        }
        return i;
    }

}
