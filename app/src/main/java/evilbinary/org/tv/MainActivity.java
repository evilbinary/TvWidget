package evilbinary.org.tv;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import evilbinary.org.tv.widget.BorderView;
import evilbinary.org.tvwidget.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_test);
        setContentView(R.layout.activity_main);

        BorderView border=new BorderView(this);
        border.setBackgroundResource(R.drawable.item_highlight);

        RelativeLayout main= (RelativeLayout) findViewById(R.id.main);
        border.attachTo(main);

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//
//        // 创建一个线性布局管理器
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//
//
//        GridLayoutManager gridlayoutManager = new GridLayoutManager(this,4);
//        gridlayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
//
//
//        recyclerView.setLayoutManager(gridlayoutManager);
//

//        border.attachTo(recyclerView);
//
//        // 创建数据集
//        String[] dataset = new String[100];
//        for (int i = 0; i < dataset.length; i++) {
//            dataset[i] = "item" + i;
//        }
//        // 创建Adapter，并指定数据集
//        MyAdapter adapter = new MyAdapter(this, dataset);
//        // 设置Adapter
//        recyclerView.setAdapter(adapter);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        // 数据集
        private String[] mDataset;
        private Context mContex;

        public MyAdapter(Context context, String[] dataset) {
            super();
            mContex = context;
            mDataset = dataset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
            View view = LayoutInflater.from(mContex).inflate(R.layout.layout_text, viewGroup, false);
            // 创建一个ViewHolder
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            // 绑定数据到ViewHolder上
            viewHolder.mTextView.setText(mDataset[i]);
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView;

            }
        }


    }


}
