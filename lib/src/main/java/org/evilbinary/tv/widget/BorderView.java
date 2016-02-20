package org.evilbinary.tv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

/**
 * 作者:evilbinary on 1/31/16.
 * 邮箱:rootdebug@163.com
 */

public class BorderView extends RelativeLayout implements ViewTreeObserver.OnGlobalFocusChangeListener, ViewTreeObserver.OnScrollChangedListener, ViewTreeObserver.OnGlobalLayoutListener {

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
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(this);

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
        mEffect.notifyChangeAnimation();


    }

    @Override
    public void onGlobalLayout() {
        Log.d(TAG, "onGlobalLayout");
        //mEffect.notifyChangeAnimation();
    }
}
