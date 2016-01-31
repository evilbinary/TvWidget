package evilbinary.org.tv.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import evilbinary.org.lib.R;
import evilbinary.org.tv.util.AnimateFactory;

/**
 * Created by evil on 1/31/16.
 */
public class BorderView extends RelativeLayout {

    private ViewGroup mViewGroup;
    private boolean mReversed;
    private boolean mAlreadyAligned;
    private static String TAG = "BorderView";

    private BorderView mBorderView;

//    public static BorderView fromRes(Context context, int layoutRes) {
//        BorderView border = new BorderView(context);
//        View.inflate(context, layoutRes, border);
//        return border;
//    }

    public BorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BorderView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setVisibility(INVISIBLE);
        mBorderView = this;
    }

    public void attachTo(ViewGroup viewGroup) {
        attachTo(viewGroup, false);
    }

    public void attachTo(ViewGroup viewGroup, boolean alreadyAligned) {
        mViewGroup = viewGroup;
        mAlreadyAligned = alreadyAligned;
        mReversed = isLayoutManagerReversed(viewGroup);
        setupAlignment(viewGroup);


        viewGroup.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                Log.d(TAG, "onGlobalFocusChanged");
                View v = newFocus;
                if (v == null) v = oldFocus;
                if (v != null) {

                    AnimateFactory.zoomInView(newFocus);
                    if (newFocus != null) {
                        //newFocus.setBackgroundColor(Color.BLUE);
                    }

                    AnimateFactory.zoomOutView(oldFocus);
//                    if (oldFocus != null)
//                        oldFocus.setBackgroundColor(Color.RED);

                    ViewGroup.LayoutParams params = mBorderView.getLayoutParams();
                    params.height = v.getMeasuredHeight();
                    params.width = v.getMeasuredWidth();

                    Log.d(TAG, "h:" + params.height + " w:" + params.width);


//                    mBorderView.setTop(v.getTop());
//                    mBorderView.setBottom(v.getBottom());
//                    mBorderView.setLeft(v.getLeft());
//                    mBorderView.setRight(v.getRight());

//                    mBorderView.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    mBorderView.setVisibility(VISIBLE);
                    View cursor = mBorderView;
                    View focused = v;
                    ValueAnimator transAnimatorX = ObjectAnimator.ofFloat(cursor,
                            "x", cursor.getLeft(), focused.getLeft());
                    ValueAnimator transAnimatorY = ObjectAnimator.ofFloat(cursor,
                            "y", cursor.getTop(), focused.getTop());


                    cursor.layout(focused.getLeft(), focused.getTop(), focused.getRight(), focused.getBottom());


                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(transAnimatorY).with(transAnimatorX);
                    animatorSet.setDuration(300);
                    animatorSet.setInterpolator(new DecelerateInterpolator(1));
                    animatorSet.start();

                } else {
                    mBorderView.setVisibility(INVISIBLE);
                }


            }
        });
    }


    private void setupAlignment(ViewGroup viewGroup) {
        if (!mAlreadyAligned) {
            //setting alignment of border
            ViewGroup.LayoutParams currentParams = getLayoutParams();
            FrameLayout.LayoutParams newBorderParams;
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            int gravity = (mReversed ? Gravity.BOTTOM : Gravity.TOP) | Gravity.CENTER_HORIZONTAL;
            if (currentParams != null) {
                newBorderParams = new FrameLayout.LayoutParams(getLayoutParams()); //to copy all the margins
                newBorderParams.width = width;
                newBorderParams.height = height;
                newBorderParams.gravity = gravity;
            } else {
                newBorderParams = new FrameLayout.LayoutParams(width, height, gravity);
            }
            BorderView.this.setLayoutParams(newBorderParams);

            //setting alignment of viewGroup
            FrameLayout newRootParent = new FrameLayout(viewGroup.getContext());
            newRootParent.setLayoutParams(viewGroup.getLayoutParams());
            ViewParent currentParent = viewGroup.getParent();
            if (currentParent instanceof ViewGroup) {
                int indexWithinParent = ((ViewGroup) currentParent).indexOfChild(viewGroup);

                ((ViewGroup) currentParent).removeViewAt(indexWithinParent);
                viewGroup.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                newRootParent.addView(viewGroup);
                newRootParent.addView(BorderView.this);
                ((ViewGroup) currentParent).addView(newRootParent, indexWithinParent);
            }
        }
    }


    private boolean isLayoutManagerReversed(ViewGroup viewGroup) {
        boolean reversed = false;
        if (viewGroup instanceof RecyclerView) {
            RecyclerView.LayoutManager manager = ((RecyclerView) viewGroup).getLayoutManager();
            if (manager instanceof LinearLayoutManager) {
                reversed = ((LinearLayoutManager) manager).getReverseLayout();
            } else if (manager instanceof StaggeredGridLayoutManager) {
                reversed = ((StaggeredGridLayoutManager) manager).getReverseLayout();
            }
        }
        return reversed;
    }

}
