package org.evilbinary.tv.widget;

import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

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
    protected int mMargin = 0;
    protected View mView;

    private AnimatorSet mAnimatorSet;

    {
        mAnimatorSet = new AnimatorSet();
    }

    public static BorderBaseEffect getDefault() {
        BorderBaseEffect borderBaseEffect = new BorderBaseEffect() {
            private View mOldFocus;
            private View mNewFocus;
            private ObjectAnimator transAnimatorX;
            private ObjectAnimator transAnimatorY;
            private ObjectAnimator scaleX;
            private ObjectAnimator scaleY;


            @Override
            protected void setupAnimation(View view, View oldFocus, View newFocus) {
                mOldFocus = oldFocus;
                mNewFocus = newFocus;
                mView = view;

                getLocation();

                if (newFocus != null) {

                    oldWidth += mMargin * 2;
                    oldHeight += mMargin * 2;
                    newWidth += mMargin * 2;
                    newHeight += mMargin * 2;
                    newX = newX - mMargin;
                    newY = newY - mMargin;
                    oldX = oldX - mMargin;
                    oldY = oldY - mMargin;

                    if (transAnimatorX == null) {
                        transAnimatorX = ObjectAnimator.ofFloat(view,
                                "x", oldX, newX);
                        transAnimatorY = ObjectAnimator.ofFloat(view,
                                "y", oldY, newY);

                        WrapView wrapView = new WrapView(view);
                        scaleX = ObjectAnimator.ofInt(wrapView,
                                "width", oldWidth, newWidth);
                        scaleY = ObjectAnimator.ofInt(wrapView,
                                "height", oldHeight, newHeight);
                        floatEvaluator = new FloatEvaluator();
                        getAnimatorSet().playTogether(transAnimatorX, transAnimatorY, scaleX, scaleY);

                    } else {
                        transAnimatorX.setEvaluator(floatEvaluator);
                        transAnimatorY.setEvaluator(floatEvaluator);

                        transAnimatorX.setFloatValues(oldX, newX);
                        transAnimatorY.setFloatValues(oldY, newY);
                        scaleX.setIntValues(oldWidth, newWidth);
                        scaleY.setIntValues(oldHeight, newHeight);

                    }

                    getAnimatorSet().setInterpolator(decelerateInterpolator);
                    getAnimatorSet().setDuration(this.mDurationTraslate);

                }


                if (this.mScalable) {
                    if (oldFocus == null) {
                        Animation anim = AnimateFactory.zoomAnimation(1.0f, mScale, 0);
                        anim.setInterpolator(finishInterpolator);
                        if (newFocus != null)
                            newFocus.startAnimation(anim);
                    } else {
                        AnimateFactory.zoomInView(newFocus, this.mScale, (int) this.mDurationLarge);
                        AnimateFactory.zoomOutView(oldFocus, this.mScale, (int) this.mDurationSmall);
                    }
                }

            }

            private Interpolator finishInterpolator = new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    return 1;
                }
            };
            private int newX = 0;
            private int newY = 0;
            private int newWidth = 0;
            private int newHeight = 0;
            private int oldX = 0;
            private int oldY = 0;
            private int oldWidth = 0;
            private int oldHeight = 0;
            int[] newLocation = new int[2];
            int[] oldLocation = new int[2];

            private FloatEvaluator floatEvaluator;

            private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(1);

            private void getLocation() {
                if (mNewFocus != null) {

                    mNewFocus.getLocationOnScreen(newLocation);
                    if (mScalable) {
                        newWidth = (int) ((float) mNewFocus.getMeasuredWidth() * mScale);
                        newHeight = (int) ((float) mNewFocus.getMeasuredHeight() * mScale);
                        newX = newLocation[0] + (mNewFocus.getMeasuredWidth() - newWidth) / 2;
                        newY = newLocation[1] + (mNewFocus.getMeasuredHeight() - newHeight) / 2;
                    } else {
                        newWidth = mNewFocus.getMeasuredWidth();
                        newHeight = mNewFocus.getMeasuredHeight();
                        newX = newLocation[0];
                        newY = newLocation[1];
                    }

                }
                if (mOldFocus != null) {

                    mOldFocus.getLocationOnScreen(oldLocation);


                    if (mScalable) {
                        oldWidth = (int) (mOldFocus.getMeasuredWidth() * mScale);
                        oldHeight = (int) (mOldFocus.getMeasuredHeight() * mScale);

                        oldX = oldLocation[0] + (mOldFocus.getMeasuredWidth() - oldWidth) / 2;
                        oldY = oldLocation[1] + (mOldFocus.getMeasuredHeight() - oldHeight) / 2;
                    } else {
                        oldX = oldLocation[0];
                        oldY = oldLocation[1];
                        oldWidth = mOldFocus.getMeasuredWidth();
                        oldHeight = mOldFocus.getMeasuredHeight();
                    }


                } else {
                    oldWidth = newWidth;
                    oldHeight = newHeight;
                    oldX = newX;
                    oldY = newY;

                }
            }

            class MyEvaluator implements TypeEvaluator<Float> {
                private float mEndValue;
                private float mStartValue;

                public MyEvaluator() {

                }

                public MyEvaluator(float mEndValue) {
                    this.mEndValue = mEndValue;
                }

                public Float evaluate(float fraction, Float startValue, Float endValue) {
                    //Log.d("test", "evaluate");
                    endValue = mEndValue;
                    //startValue=mStartValue;
                    float startFloat = startValue.floatValue();
                    return startFloat + fraction * (endValue.floatValue() - startFloat);
                }

                public void setEndValue(float endValue) {
                    this.mEndValue = endValue;
                }

                public void setStartValue(float startValue) {
                    this.mStartValue = startValue;
                }

                public void setValue(float startValue, float endValue) {
                    this.mEndValue = endValue;
                    this.mStartValue = startValue;
                }
            }

            private TimeInterpolator finishTimeInterpolator = new TimeInterpolator() {
                @Override
                public float getInterpolation(float input) {
                    return 1;
                }
            };
            private MyEvaluator myEvaluatorX;
            private MyEvaluator myEvaluatorY;
            private long endDelay = 0;
            private long delta = 10;

            @Override
            public void notifyChangeAnimation() {

                if (myEvaluatorX == null) {
                    myEvaluatorX = new MyEvaluator();
                    myEvaluatorY = new MyEvaluator();
                }

                if (transAnimatorY != null) {
                    getAnimatorSet().end();
                    getLocation();
                    myEvaluatorY.setEndValue(newY - mMargin);
                    transAnimatorY.setEvaluator(myEvaluatorY);
                    myEvaluatorX.setEndValue(newX - mMargin);
                    transAnimatorX.setEvaluator(myEvaluatorX);
                    if (!getAnimatorSet().isRunning() && !getAnimatorSet().isStarted()) {
                        endDelay = endDelay + mDurationTraslate / delta;
                        getAnimatorSet().setInterpolator(finishTimeInterpolator);
                        getAnimatorSet().setDuration(endDelay);
                        getAnimatorSet().start();
                    }
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

    public abstract void notifyChangeAnimation();

    public void start(View view, View oldFocus, View newFocus) {
        setupAnimation(view, oldFocus, newFocus);
        view.setVisibility(View.VISIBLE);
        mAnimatorSet.start();

    }

    public void end() {
        mAnimatorSet.end();
        if (mView != null)
            mView.setVisibility(View.GONE);

    }

    public void cancle() {
        mAnimatorSet.cancel();
        if (mView != null)
            mView.setVisibility(View.GONE);
    }

    public void pasue() {
        mAnimatorSet.pause();
    }

    public void resume() {
        mAnimatorSet.resume();
    }

    public AnimatorSet getAnimatorSet() {
        return mAnimatorSet;
    }

    public void setDuration(long duration) {
        mDurationTraslate = duration;
        mDurationLarge = duration;
        mDurationSmall = duration;
    }

    public void setTraslateDuration(long duration) {
        mDurationTraslate = duration;
    }

    public void setScaleDuration(long duration) {
        mDurationLarge = duration;
        mDurationSmall = duration;
    }


}
