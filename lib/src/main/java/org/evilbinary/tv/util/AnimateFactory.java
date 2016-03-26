package org.evilbinary.tv.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 作者：evilbinary on 2015/12/10 17:09
 * 邮箱：rootdebug@163.com
 */
public class AnimateFactory {
    /**
     * 缩放动画,用于缩放控件
     *
     * @param startScale 控件的起始尺寸倍率
     * @param endScale   控件的终点尺寸倍率
     * @return
     */
    public static Animation zoomAnimation(float startScale, float endScale, long duration) {
        ScaleAnimation anim = new ScaleAnimation(startScale, endScale, startScale, endScale,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setDuration(duration);
        return anim;
    }

    public static Animation shakeAnimate() {
        TranslateAnimation mAnimate = new TranslateAnimation(0, 5, 0, 0);
        mAnimate.setInterpolator(new CycleInterpolator(50));
        mAnimate.setDuration(600);
        return mAnimate;
    }

    public static void zoomInView(View v) {
        zoomInView(v, 1.1f);
    }

    public static void zoomOutView(View v) {
        zoomOutView(v, 1.1f);
    }

    public static void zoomInView(View v, float zoomSize) {
        zoomInView(v,zoomSize,200);
    }

    public static void zoomOutView(View v, float zoomSize) {
        zoomOutView(v,zoomSize,200);
    }
    public static void zoomInView(View v, float zoomSize,long duration) {
        if (v != null) {
            v.startAnimation(AnimateFactory.zoomAnimation(1.0f, zoomSize,duration));
        }
    }

    public static void zoomOutView(View v, float zoomSize,long duration) {
        if (v != null) {
            v.startAnimation(AnimateFactory.zoomAnimation(zoomSize, 1.0f, duration));
        }
    }


    public static final int ANIMATION_DEFAULT = 0;
    public static final int ANIMATION_TRANSLATE = 1;

}
