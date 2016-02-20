package org.evilbinary.tv.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
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
    protected int mMargin = 0;

    private AnimatorSet mAnimatorSet;

    {
        mAnimatorSet = new AnimatorSet();
    }

    public static BorderBaseEffect getDefault() {
        BorderBaseEffect borderBaseEffect = new BorderBaseEffect() {
            private View mOldFocus;
            private View mNewFocus;
            private View mView;
            private ObjectAnimator transAnimatorX;
            private ObjectAnimator transAnimatorY;

            @Override
            protected void setupAnimation(View view, View oldFocus, final View newFocus) {
                mOldFocus = oldFocus;
                mNewFocus = newFocus;
                mView = view;

                getLocation();

                if (newFocus != null && newFocus.getRootView() instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) newFocus.getRootView();
                    if (view.getParent() != viewGroup) {
                        viewGroup.addView(view);
                    }
                    oldWidth += mMargin * 2;
                    oldHeight += mMargin * 2;
                    newWidth += mMargin * 2;
                    newHeight += mMargin * 2;
                    newX = newX - mMargin;
                    newY = newY - mMargin;
                    oldX = oldX - mMargin;
                    oldY = oldY - mMargin;


                    transAnimatorX = ObjectAnimator.ofFloat(view,
                            "x", oldX, newX);
                    transAnimatorY = ObjectAnimator.ofFloat(view,
                            "y", oldY, newY);


                    ObjectAnimator scaleX = ObjectAnimator.ofInt(new WrapView(view),
                            "width", oldWidth, newWidth);
                    ObjectAnimator scaleY = ObjectAnimator.ofInt(new WrapView(view),
                            "height", oldHeight, newHeight);

                    getAnimatorSet().playTogether(transAnimatorX, transAnimatorY, scaleX, scaleY);
                    getAnimatorSet().setDuration(this.mDurationTraslate);
                    getAnimatorSet().setInterpolator(new DecelerateInterpolator(1));

                }


                if (this.mScalable) {
                    AnimateFactory.zoomInView(newFocus, this.mScale, (int) this.mDurationLarge);
                    AnimateFactory.zoomOutView(oldFocus, this.mScale, (int) this.mDurationSmall);
                }

            }

            private int newX = 0;
            private int newY = 0;
            private int newWidth = 0;
            private int newHeight = 0;
            private int oldX = 0;
            private int oldY = 0;
            private int oldWidth = 0;
            private int oldHeight = 0;

            private void getLocation() {
                if (mNewFocus != null) {
                    int[] newLocation = new int[2];
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
                    int[] oldLocation = new int[2];
                    mOldFocus.getLocationOnScreen(oldLocation);
                    oldX = oldLocation[0];
                    oldY = oldLocation[1];

                    oldWidth = mOldFocus.getMeasuredWidth();
                    oldHeight = mOldFocus.getMeasuredHeight();

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

            private TimeInterpolator finishTimeInterpolator=new TimeInterpolator() {
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
