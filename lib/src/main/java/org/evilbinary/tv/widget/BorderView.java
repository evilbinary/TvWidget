package org.evilbinary.tv.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

/**
 * 作者:evilbinary on 1/31/16.
 * 邮箱:rootdebug@163.com
 */

public class BorderView extends RelativeLayout implements ViewTreeObserver.OnGlobalFocusChangeListener, ViewTreeObserver.OnScrollChangedListener  {

    private static String TAG = "BorderView";


    private BorderBaseEffect mEffect;


    private BorderView mBorderView;
    private boolean mEnableBorder = true;

    public BorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BorderView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mBorderView = this;
        if (mEffect == null)
            mEffect = BorderBaseEffect.getDefault();
        setVisibility(INVISIBLE);
    }

    public BorderBaseEffect getEffect() {
        return mEffect;
    }

    public void setEffect(BorderBaseEffect effect) {
        mEffect = effect;
    }

    public void attachTo(ViewGroup viewGroup) {
        viewGroup.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        viewGroup.getViewTreeObserver().addOnScrollChangedListener(this);


     }


    private boolean isLayoutManagerReversed(ViewGroup viewGroup) {
        boolean reversed = false;
        if (viewGroup instanceof RecyclerView) {
            RecyclerView.LayoutManager manager = ((RecyclerView) viewGroup).getLayoutManager();
            if (manager instanceof LinearLayoutManager) {
                reversed = ((LinearLayoutManager) manager).getReverseLayout();
            } else if (manager instanceof StaggeredGridLayoutManager) {
                reversed = ((StaggeredGridLayoutManager) manager).getReverseLayout();
            }
        }
        return reversed;
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//        Log.d(TAG, "onGlobalFocusChanged");
        if (!mEnableBorder) return;

        View v = newFocus;
        if (v == null) v = oldFocus;
        if (v != null) {
            setVisibility(View.VISIBLE);
            mEffect.start(this, oldFocus, newFocus);

        } else {
            setVisibility(INVISIBLE);
        }

    }

    @Override
    public void onScrollChanged() {
//        Log.d(TAG, "onScrollChanged");
        mEffect.notifyChangeAnimation(this);

    }

}
