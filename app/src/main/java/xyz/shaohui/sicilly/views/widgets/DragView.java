package xyz.shaohui.sicilly.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by shaohui on 16/8/4.
 */
public class DragView extends FrameLayout {

    private ViewDragHelper mDraggier;

    private int range = 600;

    private int width;

    private int height;

    private int mainLeft;
    private View layoutLeft;
    private View layoutMain;

    public DragView(Context context) {
        super(context);
        init();
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = getWidth();
        height = getHeight();
        range = (int) (width * 0.6);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        layoutLeft = getChildAt(0);
        layoutMain = getChildAt(1);
        layoutMain.setClickable(true);
        layoutLeft.setClickable(true);
    }

    private void init() {
        mDraggier = ViewDragHelper.create(this, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (mainLeft + dx < 0) {
                    return 0;
                } else if (mainLeft + dx > range) {
                    return range;
                } else {
                    return left;
                }
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return width;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (xvel > 0) {
                    open();
                } else if (xvel < 0) {
                    close();
                } else if (releasedChild == layoutLeft && mainLeft > range * 0.7) {
                    open();
                } else if (releasedChild == layoutMain && mainLeft > range * 0.3 ) {
                    open();
                } else {
                    close();
                }
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (changedView == layoutMain) {
                    mainLeft = left;
                } else {
                    mainLeft = mainLeft + left;
                }

                if (mainLeft < 0) {
                    mainLeft = 0;
                } else if (mainLeft > range) {
                    mainLeft = range;
                }

                if (changedView == layoutLeft) {
                    layoutLeft.layout(0, 0, width, height);
                    layoutMain.layout(mainLeft, 0, width + mainLeft, height);
                }
                dispatchDragEvent(mainLeft);
            }
        });

//        mDraggier.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    private void open() {
        if (mDraggier.smoothSlideViewTo(layoutMain, range, 0)) {
            invalidate();
        }
    }

    private void close() {
        if (mDraggier.smoothSlideViewTo(layoutMain, 0, 0)) {
            invalidate();
        }
    }

    private void dispatchDragEvent(int mainLeft) {

        float percent = mainLeft / (float)range;

        //TODO

        animateView(percent);
    }

    private void animateView(float percent) {

        float scale = 1 - 0.3f * percent;

        ViewHelper.setScaleX(layoutMain, scale);
        ViewHelper.setScaleY(layoutMain, scale);

        ViewHelper.setTranslationX(layoutLeft,
                -layoutLeft.getWidth() / 2.3f + layoutLeft.getWidth() / 2.3f * percent);
        ViewHelper.setScaleX(layoutLeft, 0.5f + 0.5f * percent);
        ViewHelper.setScaleY(layoutLeft, 0.5f + 0.5f * percent);
        ViewHelper.setAlpha(layoutLeft, percent);

//        getBackground().setColorFilter(evaluate(percent, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }

    private Integer evaluate(float fraction,Object startValue, Integer endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24)& 0xff;
        int startR = (startInt >> 16)& 0xff;
        int startG = (startInt >> 8)& 0xff;
        int startB = startInt & 0xff;
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) &0xff;
        int endR = (endInt >> 16) &0xff;
        int endG = (endInt >> 8) &0xff;
        int endB = endInt & 0xff;
        return (int) ((startA + (int) (fraction* (endA - startA))) << 24)
                | (int) ((startR + (int)(fraction * (endR - startR))) << 16)
                | (int) ((startG + (int)(fraction * (endG - startG))) << 8)
                | (int) ((startB + (int)(fraction * (endB - startB))));
    }

    @Override
    public void computeScroll() {
        if (mDraggier.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDraggier.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDraggier.processTouchEvent(event);
        return true;
    }
}
