package org.evilbinary.tv;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.evilbinary.tv.widget.BorderEffect;
import org.evilbinary.tv.widget.BorderView;

/**
 * 作者:evilbinary on 4/1/16.
 * 邮箱:rootdebug@163.com
 */
public class DemoTopBorderActivity extends Activity {

    private String TAG = DemoTopBorderActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        FrameLayout roundedFrameLayout = new FrameLayout(this);
        roundedFrameLayout.setClipChildren(false);

        final BorderView borderView = new BorderView(roundedFrameLayout);
        borderView.setBackgroundResource(R.drawable.border_red);

        ViewGroup list = (ViewGroup) findViewById(R.id.list);
        borderView.attachTo(list);


        borderView.getEffect().addOnFocusChanged(new BorderEffect.FocusListener() {
            @Override
            public void onFocusChanged(View oldFocus, final View newFocus) {
                borderView.getView().setTag(newFocus);

            }
        });
        borderView.getEffect().addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                View t = borderView.getView().findViewWithTag("top");
                if (t != null) {
                    ((ViewGroup) t.getParent()).removeView(t);
                    View of = (View) borderView.getView().getTag(borderView.getView().getId());
                    ((ViewGroup) of).addView(t);

                }

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                View nf = (View) borderView.getView().getTag();
                if (nf != null) {
                    View top = nf.findViewWithTag("top");
                    if (top != null) {
                        ((ViewGroup) top.getParent()).removeView(top);
                        ((ViewGroup) borderView.getView()).addView(top);
                        borderView.getView().setTag(borderView.getView().getId(), nf);

                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }


}
