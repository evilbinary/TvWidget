package org.evilbinary.tv;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import org.evilbinary.tv.widget.BorderEffect;
import org.evilbinary.tv.widget.BorderView;
import org.evilbinary.tv.widget.TvGridView;

/**
 * 作者:evilbinary on 2/22/16.
 * 邮箱:rootdebug@163.com
 */
public class DemoGridViewActivity extends Activity {


    private TvGridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_grid_view);

        mGridView = (TvGridView) findViewById(R.id.gridView);

        MyGridViewAdapter myAdapter = new MyGridViewAdapter(R.layout.item_grid);
        mGridView.setAdapter(myAdapter);

        BorderView borderView = new BorderView<RelativeLayout>(this, R.layout.custom_item);


        mGridView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("tt", "onFocusChange");
            }
        });

        mGridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("tt", "onItemSelected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("tt", "onNothingSelected");

            }
        });

        borderView.attachTo(mGridView);
        borderView.getEffect().setMargin(10);
        borderView.getEffect().addOnFocusChanged(new BorderEffect.FocusListener() {
            @Override
            public void onFocusChanged(View oldFocus, View newFocus) {
                Log.d("tt", "onFocusChanged=====>" + oldFocus + " " + newFocus);
            }
        });

//        mGridView.setSelection(0);
//        mGridView.requestFocus();
    }


}
