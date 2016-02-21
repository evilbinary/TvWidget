package org.evilbinary.tv;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import org.evilbinary.tv.widget.BorderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者:evilbinary on 2/22/16.
 * 邮箱:rootdebug@163.com
 */
public class DemoGridViewActivity extends Activity {
    private List<Map<String, Object>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_grid_view);

        final MyGridView gridView= (MyGridView) findViewById(R.id.gridView);

        getData();
        String [] from ={"text"};
        int [] to = {R.id.textView};

        SimpleAdapter simpleAdapter=new SimpleAdapter(this, data, R.layout.item, from, to);
        gridView.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
        gridView.requestFocus();
        gridView.setSelection(0);

        //gridView.setFocusable(false);
//       gridView.getAdapter().registerDataSetObserver(new DataSetObserver() {
//           @Override
//           public void onChanged() {
//               super.onChanged();
//               scrollView.smoothScrollTo(0,0);
//           }
//       });

        BorderView borderView=new BorderView(this);

        borderView.setBackgroundResource(R.drawable.white_light_10);
        borderView.getEffect().setMargin(12);
        borderView.attachTo(gridView);

    }



    public List<Map<String, Object>> getData(){
        data=new ArrayList<Map<String, Object>>();
         for(int i=0;i<100;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", "item"+i);
            data.add(map);
        }

        return data;
    }
}
