package org.evilbinary.tv;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabWidget;
import android.widget.TextView;

import org.evilbinary.tv.widget.BorderView;

/**
 * 作者:evilbinary on 2/28/16.
 * 邮箱:rootdebug@163.com
 */
public class DemoTabActivity extends Activity {

    private static final String TAG = DemoTabActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private TabWidget mTabWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo_tab_view);

        mTabWidget = ( TabWidget) findViewById(R.id.tabWidget1);

        mTabWidget.setStripEnabled(false);
        mTabWidget.setFocusable(false);

        for (int i = 0; i < mTabWidget.getChildCount(); i++) {
            mTabWidget.getChildAt(i).setOnClickListener(mTabClickListener);
            mTabWidget.getChildAt(i).setOnFocusChangeListener(mTabFocusListener);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewPager1);
        mPagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(mPageChangeListener);

        mPagerAdapter.notifyDataSetChanged();


        mTabWidget.focusCurrentTab(0);

        BorderView borderView = new BorderView(this);

        borderView.setBackgroundResource(R.drawable.border_highlight);
        borderView.attachTo(mTabWidget);

        BorderView borderView2 = new BorderView(this);

         borderView2.setBackgroundResource(R.drawable.border_white_light_10);
        borderView2.attachTo(mViewPager);

    }

    private View.OnFocusChangeListener mTabFocusListener=new View.OnFocusChangeListener(){

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                for (int i = 0; i < mTabWidget.getChildCount(); i++) {
                    if (v == mTabWidget.getChildAt(i)) {
                        mViewPager.setCurrentItem(i);
                    }
                }
            }
        }
    };
    private View.OnClickListener mTabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabWidget.getChildCount(); i++) {
                if (v == mTabWidget.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                }
            }


        }
    };

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            mTabWidget.focusCurrentTab(arg0);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(DemoTabActivity.this).inflate(R.layout.viewpage_item, container, false);

            viewGroup.setBackgroundColor(position * 40 % 255);
            TextView textView = (TextView) viewGroup.findViewById(R.id.textView);
            textView.setText("textView " + position);

            container.addView(viewGroup);
            return viewGroup;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }


}
