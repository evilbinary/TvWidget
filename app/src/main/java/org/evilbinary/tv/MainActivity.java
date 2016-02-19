package org.evilbinary.tv;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.evilbinary.tv.widget.BorderView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BorderView border = new BorderView(this);
        border.setBackgroundResource(R.drawable.white_light_10);
        border.getEffect().setMargin(12);
        RelativeLayout main = (RelativeLayout) findViewById(R.id.main);
        border.attachTo(main);


        //test linearlayout
        //        setContentView(R.layout.layout_test);
        //        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //        // 创建一个线性布局管理器
        //        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //        recyclerView.setLayoutManager(layoutManager);
        //        border.attachTo(recyclerView);


        //test grid
        setContentView(R.layout.layout_test);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridlayoutManager = new GridLayoutManager(this, 3);
        gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int y = 0;
            private int dir = 1;
            private int delta = 100;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.d("Main", "onScrollStateChanged:" + newState);//SCROLL_STATE_IDLE

                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    recyclerView.smoothScrollBy( 0, delta * dir);

                }else{
                    super.onScrollStateChanged(recyclerView,newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.d("Main", "onScrolled");
                if (dy < y) {
                    dir = -1;
                } else {
                    dir = 1;
                }
                y = dy;
            }
        });


        recyclerView.setLayoutManager(gridlayoutManager);
        border.attachTo(recyclerView);

        //创建数据集
        String[] dataset = new String[100];
        for (int i = 0; i < dataset.length; i++) {
            dataset[i] = "item" + i;
        }
        // 创建Adapter，并指定数据集
        MyAdapter adapter = new MyAdapter(this, dataset);
        // 设置Adapter
        recyclerView.setAdapter(adapter);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                recyclerView.scrollToPosition(0);
                recyclerView.getChildAt(0).setBackgroundColor(Color.BLUE);
                recyclerView.getChildAt(recyclerView.getChildCount() - 1).setBackgroundColor(Color.GREEN);
            }
        }, 10000);
        recyclerView.scrollToPosition(0);

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
                mTextView = (TextView) itemView.findViewById(R.id.textView);

            }
        }


    }


}
