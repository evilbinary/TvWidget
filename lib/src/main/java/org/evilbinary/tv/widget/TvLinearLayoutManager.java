package org.evilbinary.tv.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * 作者:evilbinary on 1/31/16.
 * 邮箱:rootdebug@163.com
 */

public class TvLinearLayoutManager extends LinearLayoutManager {

    private static String TAG="TvLinearLayoutManager";

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mCursor;

        public ViewHolder(View itemView) {
            super(itemView);
            mCursor = (View) itemView;

        }
        public View getCursor(){
            return mCursor;
        }

    }
    ViewHolder mCursorViewHolder;

    public TvLinearLayoutManager(Context context) {
        super(context);

        //ImageView border= (ImageView) View.inflate(context, R.layout.layout_border, null);
        //mCursorViewHolder=new ViewHolder(border);
        //addDisappearingView(mCursorViewHolder.mCursor);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
    }

    @Override
    public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) {
        Log.d(TAG, "onRequestChildFocus:"+parent.getChildCount());
        //focused.setBackgroundColor(Color.BLUE);

//        View cursor=mCursorViewHolder.mCursor;
//
//

        return super.onRequestChildFocus(parent, state, child, focused);
    }


}
