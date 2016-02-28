package org.evilbinary.tv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;


/**
 * 作者:evilbinary on 1/31/16.
 * 邮箱:rootdebug@163.com
 */

public class BorderView extends View implements ViewTreeObserver.OnGlobalFocusChangeListener, ViewTreeObserver.OnScrollChangedListener, ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnTouchModeChangeListener {

    private static String TAG = "BorderView";


    private BorderBaseEffect mEffect;


    private BorderView mBorderView;
    private boolean mEnableBorder = true;
    private ViewGroup mViewGroup;
    private boolean mInTouchMode = false;
    private boolean mFocusLimit = false;

    private AdapterView.OnItemSelectedListener mOnItemSelectedListener;

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
        if (mViewGroup != viewGroup) {
            mViewGroup = viewGroup;
            ViewTreeObserver viewTreeObserver = mViewGroup.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalFocusChangeListener(this);
                viewTreeObserver.addOnScrollChangedListener(this);
                viewTreeObserver.addOnGlobalLayoutListener(this);
                viewTreeObserver.addOnTouchModeChangeListener(this);
            }
        }

    }

    public void detachFrom(ViewGroup viewGroup) {
        if (viewGroup == mViewGroup) {
            ViewTreeObserver viewTreeObserver = mViewGroup.getViewTreeObserver();
            viewTreeObserver.removeOnGlobalFocusChangeListener(this);
            viewTreeObserver.removeOnScrollChangedListener(this);
            viewTreeObserver.removeOnGlobalLayoutListener(this);
            viewTreeObserver.removeOnTouchModeChangeListener(this);
            if (getParent() == mViewGroup) {
                mViewGroup.removeView(this);
            }
        }
    }

    public boolean isFocusLimit() {
        return mFocusLimit;
    }

    public void setFocusLimit(boolean focusLimit) {
        this.mFocusLimit = focusLimit;
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//        Log.d(TAG, "onGlobalFocusChanged");
        if (!mEnableBorder) return;
        if (mInTouchMode) return;
        if (mFocusLimit) {
            if (mViewGroup.indexOfChild(newFocus) < 0) {
                mEffect.end();
                return;
            }
            if (mViewGroup.indexOfChild(oldFocus) < 0) {
                oldFocus = null;
            }
        }

        if (mViewGroup instanceof GridView || mViewGroup instanceof ListView) {
            AbsListView gridView = (AbsListView) mViewGroup;
            if (mOnItemSelectedListener == null) {
                mOnItemSelectedListener = gridView.getOnItemSelectedListener();
                if (gridView.getSelectedView() != null) {
                    newFocus = gridView.getSelectedView();
                }
                final View tempFocus = newFocus;
                gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    private View oldFocus = tempFocus;
                    private View newFocus = null;

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        newFocus = view;
                        mEffect.cancle();
                        mEffect.start(mBorderView, oldFocus, newFocus);
                        oldFocus = newFocus;
                        if (mOnItemSelectedListener != null)
                            mOnItemSelectedListener.onItemSelected(parent, view, position, id);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        mOnItemSelectedListener.onNothingSelected(parent);

                    }
                });
            }
        }
            mEffect.cancle();
            mEffect.start(this, oldFocus, newFocus);


    }

    @Override
    public void onScrollChanged() {
//        Log.d(TAG, "onScrollChanged");

        if (mViewGroup instanceof GridView || mViewGroup instanceof ListView) {

        }else{
            mEffect.notifyChangeAnimation();
        }

    }

    @Override
    public void onGlobalLayout() {
//        Log.d(TAG, "onGlobalLayout");


    }

    @Override
    public void onTouchModeChanged(boolean isInTouchMode) {
//        Log.d(TAG, "onTouchModeChanged=" + isInTouchMode);
        if (mViewGroup != null) {
            if (isInTouchMode) {
                mInTouchMode = true;
                mEffect.end();

            } else {
                mInTouchMode = false;
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown");

        return super.onKeyDown(keyCode, event);
    }

}
