package org.evilbinary.tv;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.evilbinary.tv.widget.BorderView;

/**
 * 作者:evilbinary on 2/28/16.
 * 邮箱:rootdebug@163.com
 */
public class MyFragment2 extends Fragment {
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_main, container, false);


        BorderView borderView = new BorderView(getActivity());
        borderView.setBackgroundResource(R.drawable.border_highlight);
        borderView.attachTo((ViewGroup) mView);

        return mView;
    }
};