package org.evilbinary.tv;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.evilbinary.tv.widget.BorderView;

/**
 * 作者:evilbinary on 4/10/16.
 * 邮箱:rootdebug@163.com
 */
public class DemoMenuActivity extends Activity {
    private BorderView border;

    private Context mContext;
    private String mCategory[] = new String[]{"全部频道", "美食", "休闲娱乐", "购物"};
    //二级列表数据
    private String mDatas[][] = new String[][]{
            new String[]{"全部美食", "江浙菜", "川菜", "粤菜", "湘菜"},
            new String[]{"全部休闲娱乐", "咖啡厅", "酒吧", "茶馆", "KTV"},
            new String[]{"全部购物", "综合商场", "服饰鞋包", "运动户外"},
            new String[]{"全部休闲娱乐", "咖啡厅", "酒吧", "茶馆"},

    };

    private MyAdapter secondAdapter;
    private View lastFocus = null;
    private View lastSubFocus = null;

    private RecyclerView secondRecyclerView;
    private GridLayoutManager layoutManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_menu);
        mContext = this;

        border = new BorderView(this);
        border.setBackgroundResource(R.drawable.border_red);

        RecyclerView firstRecyclerView = (RecyclerView) findViewById(R.id.firstRecyclerView);
        secondRecyclerView = (RecyclerView) findViewById(R.id.secondRecyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        firstRecyclerView.setLayoutManager(layoutManager);
        firstRecyclerView.setFocusable(false);


        layoutManager2 = new GridLayoutManager(this, 1);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        secondRecyclerView.setLayoutManager(layoutManager2);
        secondRecyclerView.setFocusable(false);


        secondAdapter = new MyAdapter(this, new String[]{}, R.layout.item_menu_sub, new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    if (lastSubFocus != null)
                        lastSubFocus.setBackgroundResource(R.drawable.list_item_shape);
                    lastSubFocus = v;
                    v.setBackgroundResource(R.drawable.shape);

                }

            }
        });
        secondAdapter.setOnBindListener(new MyAdapter.OnBindListener() {
            @Override
            public void onBind(View view, int i) {
                Log.d("tt", "onBind===>" + i);

                if (lastSubFocus == null||((int)lastSubFocus.getTag())==i) {
                    view.setBackgroundResource(R.drawable.shape);
                    lastSubFocus = view;
                }else{
                    view.setBackgroundResource(R.drawable.list_item_shape);

                }
                view.setOnClickListener(onClickListener);

            }
        });
        secondRecyclerView.setAdapter(secondAdapter);


        // 创建Adapter，并指定数据集
        MyAdapter adapter = new MyAdapter(this, mCategory, R.layout.item_menu, new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (v != lastFocus)
                        lastSubFocus = null;

                    int pos = (int) v.getTag();
                    secondAdapter.setData(mDatas[pos]);
                    secondAdapter.notifyDataSetChanged();

                    Log.d("tt", "onFocusChange===>" + pos);
                    if (lastFocus != null)
                        lastFocus.setBackgroundResource(R.drawable.list_item_shape);
                    lastFocus = v;
                    v.setBackgroundResource(R.drawable.shape);
                }


            }
        });
        adapter.setOnBindListener(new MyAdapter.OnBindListener() {
            @Override
            public void onBind(View view, int i) {
                view.setOnClickListener(onClickListener);
            }
        });
        // 设置Adapter
        firstRecyclerView.setAdapter(adapter);
        firstRecyclerView.scrollToPosition(0);


        border.getEffect().setScalable(false);
        border.getEffect().setEnableTouch(false);
        border.attachTo(firstRecyclerView);
        border.attachTo(secondRecyclerView);


    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            String tip = "click===" + pos;
            Toast.makeText(mContext, tip, Toast.LENGTH_SHORT).show();
            v.setFocusableInTouchMode(true);
            v.requestFocus();
        }
    };

}
