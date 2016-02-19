package org.evilbinary.tv.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import org.evilbinary.tv.util.AnimateFactory;


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
    protected int mMargin=0;

    private AnimatorSet mAnimatorSet;

    {
        mAnimatorSet = new AnimatorSet();
    }

    public static BorderBaseEffect getDefault() {
        BorderBaseEffect borderBaseEffect = new BorderBaseEffect() {
            @Override
            protected void setupAnimation(View view, View oldFocus, View newFocus) {


                int newX = 0;
                int newY = 0;
                int newWidth=0;
                int newHeight=0;
                if (newFocus != null) {
                    int[] newLocation = new int[2];
                    newFocus.getLocationInWindow(newLocation);
                    if(mScalable) {
                        newWidth = (int) ((float) newFocus.getMeasuredWidth() * mScale);
                        newHeight = (int) ((float) newFocus.getMeasuredHeight() * mScale);
                        newX = newLocation[0] + (newFocus.getMeasuredWidth() - newWidth) / 2;
                        newY = newLocation[1] + (newFocus.getMeasuredHeight() - newHeight) / 2;
                    }else{
                        newWidth=newFocus.getMeasuredWidth();
                        newHeight=newFocus.getMeasuredHeight();
                        newX=newLocation[0];
                        newY=newLocation[1];
                    }

                }
                int oldX = 0;
                int oldY = 0;

                int oldWidth = 0;
                int oldHeight = 0;

                if (oldFocus != null) {
                    int[] oldLocation = new int[2];
                    oldFocus.getLocationInWindow(oldLocation);
                    oldX = oldLocation[0];
                    oldY = oldLocation[1];

                    oldWidth = oldFocus.getMeasuredWidth();
                    oldHeight = oldFocus.getMeasuredHeight();
                } else {
                    if(mScalable) {
                        oldWidth = (int) ((float) newFocus.getMeasuredWidth() * mScale);
                        oldHeight = (int) ((float) newFocus.getMeasuredHeight() * mScale);
                    }else{
                        oldWidth=newFocus.getMeasuredWidth();
                        oldHeight=newFocus.getMeasuredHeight();
                    }

                }



                if (newFocus.getRootView() instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) newFocus.getRootView();
                    if (view.getParent() != viewGroup) {
                        viewGroup.addView(view);
                    }
                    oldWidth+=mMargin*2;
                    oldHeight+=mMargin*2;
                    newWidth+=mMargin*2;
                    newHeight+=mMargin*2;
                    newX=newX-mMargin;
                    newY=newY-mMargin;
                    oldX=oldX-mMargin;
                    oldY=oldY-mMargin;


                    ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(view,
                            "x", oldX, newX);
                    ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(view,
                            "y", oldY, newY);

                    ObjectAnimator scaleX = ObjectAnimator.ofInt(new WrapView(view),
                            "width", oldWidth , newWidth);
                    ObjectAnimator scaleY = ObjectAnimator.ofInt(new WrapView(view),
                            "height", oldHeight, newHeight );


                    getAnimatorSet().playTogether(transAnimatorX, transAnimatorY, scaleX, scaleY);
                    getAnimatorSet().setDuration(this.mDurationTraslate);
                    getAnimatorSet().setInterpolator(new DecelerateInterpolator(1));

                }
                if (this.mScalable) {
                    AnimateFactory.zoomInView(newFocus, this.mScale, (int) this.mDurationLarge);
                    AnimateFactory.zoomOutView(oldFocus, this.mScale, (int) this.mDurationSmall);
                }

            }
        };
        return borderBaseEffect;
    }

    private class WrapView {
        private View view;
        private int width;
        private int height;

        public WrapView(View view) {
            this.view = view;
        }

        public int getWidth() {
            return view.getLayoutParams().width;
        }

        public void setWidth(int width) {
            this.width = width;
            view.getLayoutParams().width = width;
            view.requestLayout();
        }

        public int getHeight() {
            return view.getLayoutParams().height;
        }

        public void setHeight(int height) {
            this.height = height;
            view.getLayoutParams().height = height;
            view.requestLayout();
        }
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

    public int getMargin() {
        return mMargin;
    }

    public void setMargin(int mMargin) {
        this.mMargin = mMargin;
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
