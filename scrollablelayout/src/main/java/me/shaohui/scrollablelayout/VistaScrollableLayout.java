package me.shaohui.scrollablelayout;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by shaohui on 16/8/18.
 */
public class VistaScrollableLayout extends LinearLayout {

    private float mDownX;
    private float mDownY;
    private float mLastX;
    private float mLastY;
    private boolean mDisallowIntercept;
    private boolean mIsHorizontalScrolling;
    private boolean flag1;
    private boolean flag2;
    private int mScrollY;
    private int moveDistanceX;
    private int moveDistanceY;
    private int mScrollMinY;

    private float x_down;
    private float y_down;
    private float x_move;
    private float y_move;

    private DIRECTION mDirection;
    private boolean isSticked;
    private int maxY;
    private int mHeaderHeight;
    private int sysVersion;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private VelocityTracker mVelocityTracker;

    private Scroller mScroller;
    private View mHeaderView;
    private ViewGroup mContentView;

    private OnScrollListener onScrollListener;
    private ScrollableHelper mHelper;

    public void setOnScrollListener(OnScrollListener listener) {
        this.onScrollListener = listener;
    }

    public VistaScrollableLayout(Context context) {
        this(context, null);
    }

    public VistaScrollableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VistaScrollableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHelper = new ScrollableHelper();
        mScroller = new Scroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        sysVersion = Build.VERSION.SDK_INT;

        setOrientation(VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mHeaderView != null) {
            measureChildWithMargins(mHeaderView, widthMeasureSpec, 0, MeasureSpec.UNSPECIFIED, 0);
            maxY = mHeaderView.getMeasuredHeight();
            mHeaderHeight = mHeaderView.getMeasuredHeight();
        }
        super.onMeasure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) + maxY, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onFinishInflate() {
        mHeaderView = getChildAt(0);
        if (mHeaderView != null && !mHeaderView.isClickable()) {
            mHeaderView.setClickable(true);
        }

        mContentView = (ViewGroup) getChildAt(1);

        super.onFinishInflate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentX = ev.getX();
        float currentY = ev.getY();
        float deltaY;
        int shiftX;
        int shiftY;
        shiftX = (int) Math.abs(currentX - mDownX);
        shiftY = (int) Math.abs(currentY - mDownY);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDisallowIntercept = false;
                mIsHorizontalScrolling = false;
                x_down = ev.getRawX();
                y_down = ev.getRawY();
                flag1 = true;
                flag2 = true;
                mDownX = currentX;
                mDownY = currentY;
                mScrollY = getScrollY();
                checkIsClickHead((int) currentY, mHeaderHeight, getScrollY());
                initOrResetVelocity();
                mVelocityTracker.addMovement(ev);
                mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDisallowIntercept) {
                    break;
                }
                initVelocityTrackerIfNotExists();
                mVelocityTracker.addMovement(ev);
                deltaY = mLastY - currentY;
                if (flag1) {
                    if (shiftX > mTouchSlop && shiftX > shiftY) {
                        flag1 = false;
                        flag2 = false;
                    } else if (shiftY > mTouchSlop && shiftY > shiftX) {
                        flag1 = false;
                        flag2 = true;
                    }
                }
                if (flag2 && shiftY > mTouchSlop && shiftY > shiftX && (!isSticked || mHelper.isTop())) {
                    if (mContentView != null) {
                        mContentView.requestDisallowInterceptTouchEvent(true);
                    }
                    scrollBy(0, (int) (deltaY + 0.5));
                }
                mLastX = currentX;
                mLastY = currentY;
                x_move = ev.getRawX();
                y_move = ev.getRawY();
                moveDistanceX = (int) (x_move - x_down);
                moveDistanceY = (int) (y_move - y_down);
                if (Math.abs(moveDistanceY) > mScrollMinY
                        && (Math.abs(moveDistanceY) * 0.1 > Math.abs(moveDistanceX))) {
                    mIsHorizontalScrolling = false;
                } else {
                    mIsHorizontalScrolling = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (flag2 && shiftY > shiftX && shiftY > mTouchSlop) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    float yVelocity = -mVelocityTracker.getYVelocity();
                    if (Math.abs(yVelocity) > mMinimumVelocity) {
                        mDirection = yVelocity > 0 ? DIRECTION.UP : DIRECTION.DOWN;
                        if (mDirection == DIRECTION.UP && isSticked()) {
                        } else {
                            mScroller.fling(0, getScrollY(), 0, (int) yVelocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                            mScroller.computeScrollOffset();
                        }
                    }
                }
                break;
        }

        super.dispatchTouchEvent(ev);
        return true;
    }

    private void checkIsClickHead(int currentY, int headerViewHeight, int scrollY) {
        mScroller.forceFinished(true);
    }

    private void initOrResetVelocity() {

    }

    private void initVelocityTrackerIfNotExists() {

    }

    private boolean isSticked() {
        return true;
    }

    public interface OnScrollListener {
        void onScroll(int currentY, int maxY);
    }

    public enum DIRECTION {
        UP,
        DOWN
    }
}
