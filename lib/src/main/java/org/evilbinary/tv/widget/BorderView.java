package org.evilbinary.tv.widget;

/**
 * 作者:evilbinary on 3/25/16.
 * 邮箱:rootdebug@163.com
 */

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;


/**
 * 作者:evilbinary on 3/21/16.
 * 邮箱:rootdebug@163.com
 */
public class BorderView extends View implements ViewTreeObserver.OnGlobalFocusChangeListener, ViewTreeObserver.OnScrollChangedListener, ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnTouchModeChangeListener {
    private String TAG = BorderView.class.getSimpleName();

    private ViewGroup mViewGroup;
    private Effect borderEffect;


    public interface Effect {
        public void onFocusChanged(View target, View oldFocus, View newFocus);
        public void onScrollChanged(View target,View attachView);
        public void onLayout(View target,View attachView);
        public void onTouchModeChanged(View target,View attachView,boolean isInTouchMode);
        public void onAttach(View target,View attachView);

    }

    public BorderView(Context context) {
        this(context, null, 0);
    }

    public BorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        borderEffect = new BorderEffect();
    }

    @Override
    public void onScrollChanged() {
        borderEffect.onScrollChanged(this,mViewGroup);
    }

    @Override
    public void onGlobalLayout() {
        borderEffect.onLayout(this,mViewGroup);
    }

    @Override
    public void onTouchModeChanged(boolean isInTouchMode) {
        borderEffect.onTouchModeChanged(this, mViewGroup, isInTouchMode);
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {

        if (borderEffect != null)
            borderEffect.onFocusChanged(this, oldFocus, newFocus);

    }

    public Effect getEffect() {
        return borderEffect;
    }

    public <T> T getEffect(Class<T> t) {
        return (T) borderEffect;
    }

    public void setEffect(Effect borderEffect) {
        this.borderEffect = borderEffect;
    }

    public void attach() {
        attachTo(null);
    }

    public void attachTo(ViewGroup viewGroup) {
        try {
            if (viewGroup == null) {
                if (this.getContext() instanceof Activity) {
                    Activity activity = (Activity) this.getContext();
                    viewGroup = (ViewGroup) activity.getWindow().getDecorView().getRootView();

                }
            }

            if (mViewGroup != viewGroup) {
                mViewGroup = viewGroup;

                if (this.getParent() != null && (this.getParent() instanceof ViewGroup)) {
                    ViewGroup vg = (ViewGroup) this.getParent();
                    vg.removeView(this);
                }
                ViewGroup vg = (ViewGroup) viewGroup.getRootView();
                vg.addView(this);
                ViewTreeObserver viewTreeObserver = mViewGroup.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalFocusChangeListener(this);
                    viewTreeObserver.addOnScrollChangedListener(this);
                    viewTreeObserver.addOnGlobalLayoutListener(this);
                    viewTreeObserver.addOnTouchModeChangeListener(this);
                }
                borderEffect.onAttach(this,mViewGroup);

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
