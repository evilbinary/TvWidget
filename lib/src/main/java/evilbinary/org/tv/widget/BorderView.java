package evilbinary.org.tv.widget;

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
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 作者:evilbinary on 1/31/16.
 * 邮箱:rootdebug@163.com
 */

public class BorderView extends RelativeLayout implements ViewTreeObserver.OnGlobalFocusChangeListener {

    private ViewGroup mViewGroup;
    private boolean mReversed;
    private boolean mAlreadyAligned;
    private static String TAG = "BorderView";


    private BorderBaseEffect mEffect;


    private BorderView mBorderView;

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
        if(mEffect==null)
            mEffect=BorderBaseEffect.getDefault();
        setVisibility(INVISIBLE);
    }

    public void setScalable(boolean scalable) {
        mEffect.setScalable(scalable);
    }

    public boolean isScalable() {
        return mEffect.isScalable();
    }

    public BorderBaseEffect getEffect(){return mEffect;}
    public void setEffect(BorderBaseEffect effect){
        mEffect=effect;
    }


    public void attachTo(ViewGroup viewGroup) {
        attachTo(viewGroup, false);
    }

    public void attachTo(ViewGroup viewGroup, boolean alreadyAligned) {
        mViewGroup = viewGroup;
        mAlreadyAligned = alreadyAligned;
        mReversed = isLayoutManagerReversed(viewGroup);
        setupAlignment(viewGroup);
        viewGroup.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
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

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        Log.d(TAG, "onGlobalFocusChanged");
        View v = newFocus;
        if (v == null) v = oldFocus;
        if (v != null) {

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = v.getMeasuredHeight();
            params.width = v.getMeasuredWidth();

            setVisibility(View.VISIBLE);
            mEffect.start(this,oldFocus,newFocus);
            layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

        } else {
            setVisibility(INVISIBLE);
        }

    }
}
