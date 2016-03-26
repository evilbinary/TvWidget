package org.evilbinary.tv;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.evilbinary.tv.widget.BorderView;

/**
 * 作者:evilbinary on 2/22/16.
 * 邮箱:rootdebug@163.com
 */
public class DemoGridViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_grid_view);

        GridView gridView = (GridView) findViewById(R.id.gridView);


        MyGridViewAdapter myAdapter = new MyGridViewAdapter(R.layout.item);
        gridView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        BorderView borderView = new BorderView(this);

        borderView.setBackgroundResource(R.drawable.border_white_light_10);

        borderView.attachTo(gridView);

        gridView.setSelected(true);
        gridView.setSelection(0);

        gridView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("tt", "onFocusChange");
            }
        });
//        gridView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//
//                return false;
//            }
//        });

        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("tt", "onItemSelected");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("tt", "onNothingSelected");

            }
        });

    }




}
