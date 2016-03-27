package org.evilbinary.tv.widget;

/**
 * 作者:evilbinary on 3/25/16.
 * 邮箱:rootdebug@163.com
 */

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;


/**
 * 作者:evilbinary on 3/21/16.
 * 邮箱:rootdebug@163.com
 */
public class BorderView<X extends View> implements ViewTreeObserver.OnGlobalFocusChangeListener, ViewTreeObserver.OnScrollChangedListener, ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnTouchModeChangeListener {
    private String TAG = BorderView.class.getSimpleName();

    private ViewGroup mViewGroup;
    private Effect borderEffect;

    private X mView;

    public interface Effect {
        public void onFocusChanged(View target, View oldFocus, View newFocus);

        public void onScrollChanged(View target, View attachView);

        public void onLayout(View target, View attachView);

        public void onTouchModeChanged(View target, View attachView, boolean isInTouchMode);

        public void onAttach(View target, View attachView);

        public void OnDetach(View targe, View view);
    }

    public BorderView(Context context) {
        this(context, null, 0);
    }

    public BorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        borderEffect = new BorderEffect();
        mView = (X) new View(context, attrs, defStyleAttr);
    }

    public BorderView(X view) {
        this.mView = view;
        borderEffect = new BorderEffect();
    }
    public BorderView(Context context,int resId) {
        this((X) LayoutInflater.from(context).inflate(resId,null,false));
    }

    public X getView() {
        return mView;
    }


    public void setBackgroundResource(int resId) {
        if(mView!=null)
            mView.setBackgroundResource(resId);
    }

    @Override
    public void onScrollChanged() {
        borderEffect.onScrollChanged(mView, mViewGroup);
    }

    @Override
    public void onGlobalLayout() {
        borderEffect.onLayout(mView, mViewGroup);
    }

    @Override
    public void onTouchModeChanged(boolean isInTouchMode) {
        borderEffect.onTouchModeChanged(mView, mViewGroup, isInTouchMode);
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {

        if (borderEffect != null)
            borderEffect.onFocusChanged(mView, oldFocus, newFocus);

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


    public void attachTo(ViewGroup viewGroup) {
        try {
            if (viewGroup == null) {
                if (mView.getContext() instanceof Activity) {
                    Activity activity = (Activity) mView.getContext();
                    viewGroup = (ViewGroup) activity.getWindow().getDecorView().getRootView();
                }
            }

            if (mViewGroup != viewGroup) {
                mViewGroup = viewGroup;

                if (mView.getParent() != null && (mView.getParent() instanceof ViewGroup)) {
                    ViewGroup vg = (ViewGroup) mView.getParent();
                    vg.removeView(mView);
                }
                ViewGroup vg = (ViewGroup) viewGroup.getRootView();
                vg.addView(mView);
                ViewTreeObserver viewTreeObserver = mViewGroup.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalFocusChangeListener(this);
                    viewTreeObserver.addOnScrollChangedListener(this);
                    viewTreeObserver.addOnGlobalLayoutListener(this);
                    viewTreeObserver.addOnTouchModeChangeListener(this);
                }
            }
            borderEffect.onAttach(mView, viewGroup);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void detach() {
        detachFrom(mViewGroup);
    }

    public void detachFrom(ViewGroup viewGroup) {
        try {
            if (viewGroup == mViewGroup) {
                ViewTreeObserver viewTreeObserver = mViewGroup.getViewTreeObserver();
                viewTreeObserver.removeOnGlobalFocusChangeListener(this);
                viewTreeObserver.removeOnScrollChangedListener(this);
                viewTreeObserver.removeOnGlobalLayoutListener(this);
                viewTreeObserver.removeOnTouchModeChangeListener(this);
                if (mView.getParent() == mViewGroup) {
                    mViewGroup.removeView(mView);
                }
                borderEffect.OnDetach(mView, viewGroup);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
