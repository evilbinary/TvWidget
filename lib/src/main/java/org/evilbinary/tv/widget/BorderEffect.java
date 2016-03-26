package org.evilbinary.tv.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

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
    private View lastFocus, oldLastFocus;
    private AnimatorSet mAnimatorSet;
    List<Animator> mAnimatorList = new ArrayList<Animator>();
    private View mTarget;

    public BorderEffect() {

        mFocusListener.add(focusMoveListener);
        mFocusListener.add(focusScaleListener);
        mFocusListener.add(focusPlayListener);
    }

    public interface FocusListener {
        public void onFocusChanged(View oldFocus, View newFocus);
    }

    private List<FocusListener> mFocusListener = new ArrayList<FocusListener>(1);
    private List<Animator.AnimatorListener> mAnimatorListener = new ArrayList<Animator.AnimatorListener>(1);


    public FocusListener focusScaleListener = new FocusListener() {
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
    public FocusListener focusPlayListener = new FocusListener() {
        @Override
        public void onFocusChanged(View oldFocus, View newFocus) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setInterpolator(new DecelerateInterpolator(1));
            animatorSet.setDuration(mDurationTraslate);
            animatorSet.playTogether(mAnimatorList);
            for (Animator.AnimatorListener listener : mAnimatorListener) {
                animatorSet.addListener(listener);
            }
            mAnimatorSet = animatorSet;
            if (oldFocus == null) {
                animatorSet.setDuration(0);
                mTarget.setVisibility(View.VISIBLE);
            }
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
                    newWidth = (int) (newFocus.getWidth() * mScale) + mMargin * 2;
                    newHeight = (int) (newFocus.getHeight() * mScale + mMargin * 2);
                    newXY[0] = newXY[0] - (newWidth - newFocus.getWidth()) / 2;
                    newXY[1] = newXY[1] - (newHeight - newFocus.getHeight()) / 2;
                } else {
                    newWidth = newFocus.getWidth();
                    newHeight = newFocus.getHeight();
                }

                PropertyValuesHolder valuesWithdHolder = PropertyValuesHolder.ofInt("width", mTarget.getWidth(), newWidth);
                PropertyValuesHolder valuesHeightHolder = PropertyValuesHolder.ofInt("height", mTarget.getHeight(), newHeight);
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

    public void addOnFocusChanged(FocusListener focusListener) {
        this.mFocusListener.add(focusListener);
    }

    public void removeOnFocusChanged(FocusListener focusListener) {
        this.mFocusListener.remove(focusListener);
    }

    public void addAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.mAnimatorListener.add(animatorListener);
    }

    public void removeAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.mAnimatorListener.remove(animatorListener);
    }

    @Override
    public void onFocusChanged(View target, View oldFocus, View newFocus) {
        try {

            lastFocus = newFocus;
            oldLastFocus=oldFocus;
            mTarget = target;
            Log.d(TAG, "onFocusChanged");
            if (newFocus == null || newFocus.getWidth() <= 0 || newFocus.getHeight() <= 0 || isScrollChanged)
                return;

            mAnimatorList.clear();

            for (FocusListener f : this.mFocusListener) {
                f.onFocusChanged(oldFocus, newFocus);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = null;

    private boolean isScrollChanged = false;

    @Override
    public void onScrollChanged(View target, View attachView) {
        try {
            Log.d(TAG, "onScrollChanged");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onLayout(View target, View attachView) {
        try {
            Log.d(TAG, "onLayout");
            ViewGroup viewGroup = (ViewGroup) attachView.getRootView();
            if (target.getParent() != null && target.getParent() != viewGroup) {
                target.setVisibility(View.GONE);
                if (mFirstFocus)
                    viewGroup.requestFocus();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean mFirstFocus = true;

    public void setFirstFocus(boolean b) {
        this.mFirstFocus = b;
    }

    @Override
    public void onTouchModeChanged(View target, View attachView, boolean isInTouchMode) {
        try {
            if (isInTouchMode) {
                target.setVisibility(View.INVISIBLE);
                if (lastFocus != null) {
                    ObjectAnimator oldScaleX = new ObjectAnimator().ofFloat(lastFocus, "scaleX", mScale, 1.0f);
                    ObjectAnimator oldScaleY = new ObjectAnimator().ofFloat(lastFocus, "scaleY", mScale, 1.0f);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(oldScaleX, oldScaleY);
                    animatorSet.setDuration(0).start();
                }

            } else {
                target.setVisibility(View.VISIBLE);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onAttach(View target, View attachView) {
        target.setVisibility(View.GONE);

        if (attachView instanceof RecyclerView) {
            if (recyclerViewOnScrollListener == null) {
                RecyclerView recyclerView = (RecyclerView) attachView;
                final RecyclerView.LayoutManager layoutManager=recyclerView.getLayoutManager();
                recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            Log.d(TAG, "========>SCROLL_STATE_IDLE");
                            isScrollChanged = false;
                            BorderEffect.this.onFocusChanged(mTarget, oldLastFocus, lastFocus);

                        } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                            Log.d(TAG, "========>SCROLL_STATE_SETTLING");
                            isScrollChanged = true;
                        }

                    }
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        Log.d(TAG, "========>scrollX=" + dx + " scrollY=" + dy);
                        if(mTarget!=null) {
                            mTarget.setX(mTarget.getX() -dx);
                            mTarget.setY(mTarget.getY() - dy);
                        }
                    }
                };
                recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
            }
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

}
