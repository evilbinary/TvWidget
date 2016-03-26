package org.evilbinary.tv;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import org.evilbinary.tv.widget.BorderView;

/**
 * 作者:evilbinary on 3/19/16.
 * 邮箱:rootdebug@163.com
 */
public class DemoHomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BorderView border = new BorderView(this);

        border.setBackgroundResource(R.drawable.border_red);
        //border.setBackgroundColor(Color.GREEN);

        //border.getEffect().setScale(1.1f);

        ViewGroup list = (ViewGroup) findViewById(R.id.list);
        border.attachTo(list);

    }


}
