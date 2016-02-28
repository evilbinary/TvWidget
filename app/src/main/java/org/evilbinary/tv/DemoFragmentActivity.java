package org.evilbinary.tv;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * 作者:evilbinary on 2/28/16.
 * 邮箱:rootdebug@163.com
 */
public class DemoFragmentActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_fragment);

        Fragment fragment=new MyFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment,fragment).commit();



    }


}
