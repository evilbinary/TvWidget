package org.evilbinary.tv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 作者:evilbinary on 2/20/16.
 * 邮箱:rootdebug@163.com
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    // 数据集
    private String[] mDataset;
    private Context mContex;
    private int id;
    private View.OnFocusChangeListener mOnFocusChangeListener;
    private OnBindListener onBindListener;
    public interface  OnBindListener{
        public void onBind(View view,int i);
    };

    public MyAdapter(Context context, String[] dataset) {
        super();
        mContex = context;
        mDataset = dataset;
    }
    public MyAdapter(Context context, String[] dataset,int id) {
        super();
        mContex = context;
        mDataset = dataset;
        this.id=id;
    }

    public MyAdapter(Context context, String[] dataset,int id,View.OnFocusChangeListener onFocusChangeListener) {
        super();
        mContex = context;
        mDataset = dataset;
        this.id=id;
        this.mOnFocusChangeListener=onFocusChangeListener;
    }

    public void setOnBindListener(OnBindListener onBindListener) {
        this.onBindListener = onBindListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int resId=R.layout.item;
        if(this.id>0){
            resId=this.id;
        }
        View view = LayoutInflater.from(mContex).inflate(resId, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.mTextView.setText(mDataset[i]);
        viewHolder.itemView.setTag(i);
        viewHolder.itemView.setOnFocusChangeListener(mOnFocusChangeListener);
        if(onBindListener!=null){
            onBindListener.onBind(viewHolder.itemView,i);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textView);

        }
    }

    public void setData(String[] data){
        this.mDataset=data;
    }

}
