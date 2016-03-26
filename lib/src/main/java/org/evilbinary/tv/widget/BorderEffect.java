package org.evilbinary.tv.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;

import org.evilbinary.tv.util.AnimateFactory;
import org.evilbinary.tv.widget.BorderView.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:evilbinary on 3/26/16.
 * 邮箱:rootdebug@163.com
 */
public class BorderEffect implements Effect {
    private String TAG = BorderEffect.class.getSimpleName();



    protected boolean mScalable = true;
    protected float mScale = 1.1f;

    protected long mDurationTraslate = 200;
    protected int mMargin = 0;
    private View lastFocus;
    private AnimatorSet mAnimatorSet;
    List<Animator> mAnimatorList = new ArrayList<Animator>();
    private View mTarget;

    public BorderEffect() {
        mFocusListener.add(focusScaleListener);
        mFocusListener.add(focusMoveListener);
        mFocusListener.add(focusPlayListener);
    }

    public interface FocusListener {
        public void onFocusChanged(View oldFocus, View newFocus);
    }
    private List<FocusListener> mFocusListener = new ArrayList<FocusListener>(1);
    private  List<Animator.AnimatorListener> mAnimatorListener=new  ArrayList<Animator.AnimatorListener>(1);

    public FocusListener focusScaleListener = new FocusListener() {
        @Override
        public void onFocusChanged(View oldFocus, View newFocus) {
            if(mScalable) {
                AnimateFactory.zoomInView(newFocus, mScale);
                AnimateFactory.zoomOutView(oldFocus, mScale);
            }
        }
    };
    public FocusListener focusScale2Listener = new FocusListener() {
        @Override
        public void onFocusChanged(View oldFocus, View newFocus) {
            ObjectAnimator scaleX = new ObjectAnimator().ofFloat(newFocus, "scaleX", 1.0f, mScale);
            ObjectAnimator scaleY = new ObjectAnimator().ofFloat(newFocus, "scaleY", 1.0f, mScale);
            mAnimatorList.add(scaleX);
            mAnimatorList.add(scaleY);

            if (oldFocus != null) {
                ObjectAnimator oldScaleX = new ObjectAnimator().ofFloat(oldFocus, "scaleX", mScale, 1.0f);
                ObjectAnimator oldScaleY = new ObjectAnimator().ofFloat(oldFocus, "scaleY", mScale, 1.0f);
                mAnimatorList.add(oldScaleX);
                mAnimatorList.add(oldScaleY);
            }
        }
    };
    public FocusListener focusPlayListener=new FocusListener() {
        @Override
        public void onFocusChanged(View oldFocus, View newFocus) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setInterpolator(new DecelerateInterpolator(1));
            animatorSet.setDuration(mDurationTraslate);
            animatorSet.playTogether(mAnimatorList);
            for(Animator.AnimatorListener listener: mAnimatorListener){
                animatorSet.addListener(listener);
            }
            mAnimatorSet = animatorSet;
            animatorSet.start();
        }
    };
    public FocusListener focusMoveListener = new FocusListener() {
        @Override
        public void onFocusChanged(View oldFocus, View newFocus) {
            if (newFocus == null) return;
            try {

                int newXY[];
                int oldXY[];
                newXY = getGlobalLocation(newFocus);
                oldXY = getGlobalLocation(mTarget);

                int newWidth;
                int newHeight;

                if (mScalable) {
                    newWidth = (int) (newFocus.getMeasuredWidth() * mScale) + mMargin * 2;
                    newHeight = (int) (newFocus.getMeasuredHeight() * mScale + mMargin * 2);
                    newXY[0] = newXY[0] - (newWidth - newFocus.getMeasuredWidth()) / 2;
                    newXY[1] = newXY[1] - (newHeight - newFocus.getMeasuredHeight()) / 2;
                } else {
                    newWidth = newFocus.getMeasuredWidth();
                    newHeight = newFocus.getMeasuredHeight();
                }

                PropertyValuesHolder valuesWithdHolder = PropertyValuesHolder.ofInt("width", mTarget.getMeasuredWidth(), newWidth);
                PropertyValuesHolder valuesHeightHolder = PropertyValuesHolder.ofInt("height", mTarget.getMeasuredHeight(), newHeight);
                PropertyValuesHolder valuesXHolder = PropertyValuesHolder.ofInt("x", oldXY[0], newXY[0]);
                PropertyValuesHolder valuesYHolder = PropertyValuesHolder.ofInt("y", oldXY[1], newXY[1]);
                final ObjectAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(mTarget, valuesWithdHolder, valuesHeightHolder, valuesYHolder, valuesXHolder);

                scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int width = (int) animation.getAnimatedValue("width");
                        int height = (int) animation.getAnimatedValue("height");
                        int x = (int) animation.getAnimatedValue("x");
                        int y = (int) animation.getAnimatedValue("y");

                        View view = (View) scaleAnimator.getTarget();
                        view.setX(x);
                        view.setY(y);
                        int w = view.getLayoutParams().width;
                        view.getLayoutParams().width = width;
                        view.getLayoutParams().height = height;
                        if (w > 0) {
                            view.requestLayout();
                            view.invalidate();
                        }
                    }
                });
                mAnimatorList.add(scaleAnimator);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    private int[] getGlobalLocation(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    public void addOnFocusListener(FocusListener focusListener) {
        this.mFocusListener.add(focusListener);
    }

    public void addAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.mAnimatorListener.add(animatorListener);
    }


    @Override
    public void onFocusChanged(View target, View oldFocus, View newFocus) {
        try {

            Log.d(TAG, "onFocusChanged oldFocus:"+oldFocus+" newFocus:"+newFocus );
            if (newFocus.getMeasuredWidth() <= 0 || newFocus.getMeasuredHeight() <=0)
                return;

            mTarget = target;
            lastFocus = newFocus;
            mAnimatorList.clear();

            for (FocusListener f : this.mFocusListener) {
                f.onFocusChanged(oldFocus, newFocus);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onScrollChanged(View target, View attachView) {
        if (attachView instanceof AbsListView) {

        } else {
            if (mAnimatorSet != null) ;
                mAnimatorSet.end();
        }
    }

    @Override
    public void onLayout(View target, View attachView) {
        target.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTouchModeChanged(View target, View attachView, boolean isInTouchMode) {
        try {
            if (isInTouchMode) {
                target.setVisibility(View.INVISIBLE);
            } else {
                target.setVisibility(View.VISIBLE);
            }
            if (mAnimatorSet != null) ;
            mAnimatorSet.end();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onAttach(View target, View attachView) {
        target.setVisibility(View.INVISIBLE);
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

}
