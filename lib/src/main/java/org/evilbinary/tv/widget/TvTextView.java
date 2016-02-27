package org.evilbinary.tv.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import org.evilbinary.tv.util.AnimateFactory;

import evilbinary.org.lib.R;

/**
 * 作者：evilbinary on 2015/12/10 17:09
 * 邮箱：rootdebug@163.com
 */
public class TvTextView extends TextView implements View.OnFocusChangeListener {


    private Rect mBound;
    private Drawable mBorderDrawable;
    private Rect mRect;

    private Animation scaleSmallAnimation;
    private Animation scaleBigAnimation;
    private int borderSize = 20;

    private boolean mScaleable = true;
    private int mKeyNumber;

    private Paint mPaint;

    public TvTextView(Context context) {
        super(context);
        init();
    }

    public TvTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TvTextView);

        mScaleable = a.getBoolean(R.styleable.TvTextView_scaleable, true);
        mKeyNumber = a.getInteger(R.styleable.TvTextView_number, -1);
        int berderResId = a.getResourceId(R.styleable.TvTextView_borderDrawable, R.drawable.border_highlight);
        mBorderDrawable = getResources().getDrawable(berderResId);

        int numberColor = a.getColor(R.styleable.TvTextView_numberColor, Color.WHITE);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(25);
        mPaint.setColor(numberColor);

        a.recycle();


    }

    protected void init() {

        setFocusable(true);
        setClickable(true);
        setWillNotDraw(false);
        mRect = new Rect();
        mBound = new Rect();
        this.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (!mScaleable)
            return;
        if (b) {
            AnimateFactory.zoomInView(view);
        } else {
            AnimateFactory.zoomOutView(view);
        }
    }

    @Override
    public void draw(Canvas canvas) {

        if (hasFocus()) {
            super.getDrawingRect(mRect);
            mBound.set(-borderSize + mRect.left, -borderSize + mRect.top, borderSize + mRect.right, borderSize + mRect.bottom);
            mBorderDrawable.setBounds(mBound);
            canvas.save();
            if (mBorderDrawable != null)
                mBorderDrawable.draw(canvas);
            canvas.restore();
        }
        super.draw(canvas);
    }


    protected void drawCenterNumberText(Canvas canvas, String text, Paint textPaint) {
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.
        canvas.drawText(text, xPos, yPos, textPaint);
    }

    protected void drawBottomNumberText(Canvas canvas, String text, Paint textPaint) {
        float strWidth = textPaint.measureText(text);
        int xPos = (canvas.getWidth() / 2) - (int) strWidth / 2;
        int yPos = (int) ((canvas.getHeight()) + ((textPaint.descent() + textPaint.ascent()) / 2) / 2);
        canvas.drawText(text, xPos, yPos, textPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mKeyNumber >= 0) {
            super.getDrawingRect(mRect);
            drawBottomNumberText(canvas, "" + mKeyNumber, mPaint);
        }

        super.onDraw(canvas);
    }


}
