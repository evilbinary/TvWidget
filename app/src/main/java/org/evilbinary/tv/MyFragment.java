package org.evilbinary.tv;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.evilbinary.tv.widget.BorderView;

/**
 * 作者:evilbinary on 2/28/16.
 * 邮箱:rootdebug@163.com
 */
public class MyFragment extends Fragment {
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.demo_fragment_grid_view, container, false);

        GridView gridView = (GridView) mView.findViewById(R.id.gridView);

        MyGridViewAdapter myAdapter = new MyGridViewAdapter(R.layout.item2);
        gridView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

         gridView.setSelection(0);
        gridView.setSelected(false);
        gridView.setFocusable(false);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("tt", "setOnItemClickListener");
                MyFragment2 fragment2 = new MyFragment2();
                getFragmentManager().beginTransaction().hide(MyFragment.this).replace(R.id.fragment, fragment2).addToBackStack(null).commit();
            }
        });
        //gridView.setFocusable(false);

        BorderView borderView = new BorderView(getActivity());
        borderView.setBackgroundResource(R.drawable.border_white_light_10);
        //borderView.getEffect(BorderEffect.class).setMargin(12);
        borderView.attachTo((ViewGroup) mView);

        BorderView borderView2 = new BorderView(getActivity());
        borderView2.setBackgroundResource(R.drawable.border_white_light_10);
        //borderView2.getEffect(BorderEffect.class).setMargin(12);
        borderView2.attachTo((ViewGroup) gridView);

        return mView;
    }
};