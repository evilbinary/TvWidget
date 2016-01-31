package evilbinary.org.tv.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import evilbinary.org.tv.util.AnimateFactory;

/**
 * 作者:evilbinary on 2/1/16.
 * 邮箱:rootdebug@163.com
 */
public abstract class BorderBaseEffect {
    protected boolean mScalable = true;
    protected float mScale = 1.1f;
    protected long mDurationLarge = 200;
    protected long mDurationSmall = 200;
    protected long mDurationTraslate = 200;

    private AnimatorSet mAnimatorSet;
    {
        mAnimatorSet = new AnimatorSet();
    }

    public static BorderBaseEffect getDefault() {
        BorderBaseEffect b = new BorderBaseEffect() {
            @Override
            protected void setupAnimation(View view, View oldFocus, View newFocus) {
                View v = newFocus;
                if (v == null) v = oldFocus;
                if (v != null) {
                    View focused = v;

                    if (this.mScalable) {
                        AnimateFactory.zoomInView(newFocus, this.mScale, (int) this.mDurationLarge);
                        AnimateFactory.zoomOutView(oldFocus, this.mScale, (int) this.mDurationSmall);
                    }
                    ViewGroup.LayoutParams params = view.getLayoutParams();

                    params.height = view.getMeasuredHeight();
                    params.width = view.getMeasuredWidth();

                    view.setVisibility(View.VISIBLE);

                    ValueAnimator transAnimatorX = ObjectAnimator.ofFloat(view,
                            "x", view.getLeft(), focused.getLeft());
                    ValueAnimator transAnimatorY = ObjectAnimator.ofFloat(view,
                            "y", view.getTop(), focused.getTop());

                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, this.mScale);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, this.mScale);


                    getAnimatorSet().play(transAnimatorX).with(transAnimatorY).with(scaleX).with(scaleY);
                    getAnimatorSet().setDuration(this.mDurationTraslate);
                    getAnimatorSet().setInterpolator(new DecelerateInterpolator(1));
                }
            }
        };
        return b;
    }

    public boolean isScalable() {
        return mScalable;
    }

    public void setScalable(boolean scalable) {
        this.mScalable = scalable;
    }

    public float getScale() {
        return mScale;
    }

    public void setScale(float scale) {
        this.mScale = scale;
    }


    protected abstract void setupAnimation(View view, View oldFocus, View newFocus);

    public void start(View view, View oldFocus, View newFocus) {
        setupAnimation(view, oldFocus, newFocus);
        mAnimatorSet.start();
    }

    public AnimatorSet getAnimatorSet() {
        return mAnimatorSet;
    }

    public void setDuration(long duration) {
        mDurationTraslate = duration;
        mDurationLarge = duration;
        mDurationSmall = duration;
    }


}
