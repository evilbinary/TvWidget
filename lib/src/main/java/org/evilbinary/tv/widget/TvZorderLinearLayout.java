package org.evilbinary.tv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 作者:evilbinary on 2/2/16.
 * 邮箱:rootdebug@163.com
 */
public class TvZorderLinearLayout  extends LinearLayout {
        private int position = 0;

        public TvZorderLinearLayout(Context context) {
            super(context);

        }

        public TvZorderLinearLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.setChildrenDrawingOrderEnabled(true);
        }


        public void setCurrentPosition(int pos) {
            this.position = pos;
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
