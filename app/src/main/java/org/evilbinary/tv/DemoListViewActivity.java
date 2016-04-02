package org.evilbinary.tv;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.evilbinary.tv.widget.BorderView;

import java.util.List;
import java.util.Map;

/**
 * 作者:evilbinary on 2/22/16.
 * 邮箱:rootdebug@163.com
 */
public class DemoListViewActivity extends Activity {
    private List<Map<String, Object>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_list_view);

        ListView listView = (ListView) findViewById(R.id.listView);

        MyAdapter myAdapter=new MyAdapter();
        listView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        listView.setSelected(true);
        listView.setSelection(0);

        BorderView borderView = new BorderView(this);

        borderView.setBackgroundResource(R.drawable.border_red);
        //borderView.getEffect(BorderEffect.class).setMargin(12);
        borderView.attachTo(listView);

    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 200;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(DemoListViewActivity.this).inflate(R.layout.item3, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //if (position == 0)
            //  convertView.requestFocus();

            viewHolder.text.setText("我是item" + position);
            return convertView;
        }

        private class ViewHolder {
            public TextView text;
        }
    }
}
