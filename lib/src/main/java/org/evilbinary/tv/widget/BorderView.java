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
    private boolean mFirstFocus = true;

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
        try {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void detachFrom(ViewGroup viewGroup) {
        try {
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
        } catch (Exception ex) {
            ex.printStackTrace();
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
        try {
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
            if (mViewGroup.getRootView() instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) mViewGroup.getRootView();
                if (this.getParent() != viewGroup) {
                    ViewGroup vg = (ViewGroup) this.getParent();
                    if (vg != null) {
                        Log.d(TAG, "removeView");
                        detachFrom(vg);
                        vg.removeView(this);
                        oldFocus = null;
                    }
                    viewGroup.addView(this);
                }
            }

            if (mViewGroup instanceof AbsListView) {
                AbsListView gridView = (AbsListView) mViewGroup;
                if (gridView.getSelectedView() != null && mFirstFocus) {
                    newFocus = gridView.getSelectedView();
                    mEffect.start(this, null, newFocus);
                }

                if (mOnItemSelectedListener == null) {
                    mOnItemSelectedListener = gridView.getOnItemSelectedListener();
                    final View tempFocus = newFocus;
                    gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        private View oldFocus = tempFocus;
                        private View newFocus = null;

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            newFocus = view;
                            mEffect.start(BorderView.this, oldFocus, newFocus);
                            oldFocus = newFocus;
                            if (mOnItemSelectedListener != null)
                                mOnItemSelectedListener.onItemSelected(parent, view, position, id);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            if (mOnItemSelectedListener != null)
                                mOnItemSelectedListener.onNothingSelected(parent);

                        }
                    });
                }

            } else {
                mEffect.cancle();
                mEffect.start(this, oldFocus, newFocus);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setFirstFocus(boolean b) {
        this.mFirstFocus = b;
    }

    @Override
    public void onScrollChanged() {
        try {
//        Log.d(TAG, "onScrollChanged");

            if (mViewGroup instanceof AbsListView) {

            } else {
                mEffect.notifyChangeAnimation();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onGlobalLayout() {
        try {
            //Log.d(TAG, "onGlobalLayout");
            ViewGroup viewGroup = (ViewGroup) mViewGroup.getRootView();
            if (this.getParent() != viewGroup) {
                this.setVisibility(GONE);
                if (mFirstFocus)
                    viewGroup.requestFocus();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
