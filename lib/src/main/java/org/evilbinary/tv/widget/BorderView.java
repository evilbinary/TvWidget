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
import android.widget.RelativeLayout;


/**
 * 作者:evilbinary on 1/31/16.
 * 邮箱:rootdebug@163.com
 */

public class BorderView extends RelativeLayout implements ViewTreeObserver.OnGlobalFocusChangeListener, ViewTreeObserver.OnScrollChangedListener, ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnTouchModeChangeListener {

    private static String TAG = "BorderView";


    private BorderBaseEffect mEffect;


    private BorderView mBorderView;
    private boolean mEnableBorder = true;
    private ViewGroup mViewGroup;
    private boolean mInTouchMode = false;
    private boolean mFocusLimit=false;

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
        mViewGroup = viewGroup;
        mViewGroup.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        mViewGroup.getViewTreeObserver().addOnScrollChangedListener(this);
        mViewGroup.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mViewGroup.getViewTreeObserver().addOnTouchModeChangeListener(this);

    }

    public void detachFrom(ViewGroup viewGroup) {
        if (viewGroup == mViewGroup) {
            mViewGroup.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
            mViewGroup.getViewTreeObserver().removeOnScrollChangedListener(this);
            mViewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            mViewGroup.getViewTreeObserver().removeOnTouchModeChangeListener(this);
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
        if(mFocusLimit){
            if(mViewGroup.indexOfChild(newFocus)<0 ){
                return ;
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
        mEffect.getAnimatorSet().cancel();
        mEffect.start(this, oldFocus, newFocus);


    }

    @Override
    public void onScrollChanged() {
//        Log.d(TAG, "onScrollChanged");
        //if (mViewGroup instanceof RecyclerView) {
        mEffect.notifyChangeAnimation();
        //}

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
                mEffect.end(this);

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
