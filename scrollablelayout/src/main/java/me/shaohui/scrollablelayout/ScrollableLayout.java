package me.shaohui.scrollablelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
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
public class ScrollableLayout extends LinearLayout{

    private View mHeaderView;
    private ViewGroup mContentView;
    private ScrollableHelper mHelper;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private OnScrollListener mOnScrollListener;
    private int mMinHeight = 0;

    private int maxY;
    private int minY = 0;
    private int mCurY;
    private int mHeaderHeight;
    private int mScrollMinY = 10;
    private DIRECTION mDirection;
    private int mLastScrollerY;
    private boolean isClickHeader;
    private int sysVersion;

    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private boolean mIsHorizontalScrolling;
    private boolean mDisallowIntercept;
    boolean flag1, flag2;
    private float mDownX, mDownY;
    private float mLastX, mLastY;
    private float x_down, y_down;
    private float x_move, y_move;
    private int moveDistanceX;
    private int moveDistanceY;

    public ScrollableLayout(Context context) {
        this(context, null);
    }

    public ScrollableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ScrollableLayout);
        try {
          mMinHeight = (int) array.getDimension(R.styleable.ScrollableLayout_min_top_height, 0);
        } finally {
            array.recycle();
        }

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mHelper = new ScrollableHelper();
        mScroller = new Scroller(context);
        sysVersion = Build.VERSION.SDK_INT;
        setOrientation(VERTICAL);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeaderView != null) {
            measureChildWithMargins(mHeaderView, widthMeasureSpec, 0, MeasureSpec.UNSPECIFIED, 0);
            maxY = mHeaderView.getMeasuredHeight() - mMinHeight;
            mHeaderHeight = mHeaderView.getMeasuredHeight();
        }
        super.onMeasure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) + mHeaderHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onFinishInflate() {
        mHeaderView = getChildAt(0);
        mContentView = (ViewGroup) getChildAt(1);
        super.onFinishInflate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float deltaY ;
        float currentX = ev.getX();
        float currentY = ev.getY();
        int shiftX = (int) Math.abs(currentX - mDownX);
        int shiftY = (int) Math.abs(currentY - mDownY);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDisallowIntercept = false;
                mIsHorizontalScrolling = false;
                flag1 = true;
                flag2 = true;
                mDownX = currentX;
                mDownY = currentY;
                x_down = ev.getRawX();
                y_down = ev.getRawY();
                initOrResetVelocityTracker();
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

                if (flag2 && shiftY > mTouchSlop && shiftY > shiftX
                        && (!isStickied() || mHelper.isTop())) {
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
                        && (Math.abs(moveDistanceY) * 0.1 > moveDistanceX)) {
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
                        mDirection = yVelocity > 0 ? DIRECTION.UP:DIRECTION.DOWN;
                        if (mDirection == DIRECTION.UP && isStickied()) {
                        } else {
                            mScroller.fling(0, getScrollY(), 0, (int) yVelocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                            mScroller.computeScrollOffset();
                            mLastScrollerY = getScrollY();
                            invalidate();
                        }
                    }
                    if (!isStickied()) {
                        int action = ev.getAction();
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        boolean bb = super.dispatchTouchEvent(ev);
                        ev.setAction(action);
                        return bb;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (flag2 && isClickHeader && (shiftX > mTouchSlop || shiftY > mTouchSlop)) {
                    return super.dispatchTouchEvent(ev);
                }
                break;
            default:
                break;
        }
        super.dispatchTouchEvent(ev);
        return true;
    }

    private int getScrollerVelocity(int distance, int duration) {
        if (mScroller == null) {
            return 0;
        } else if (sysVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return (int) mScroller.getCurrVelocity();
        } else {
            return distance/duration;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int currY = mScroller.getCurrY();
            if (mDirection == DIRECTION.UP) {
                if (isStickied()) {
                    int distance = mScroller.getFinalY() - currY;
                    int duration = (mScroller.getDuration() - mScroller.timePassed());
                    mHelper.smoothScrollBy(getScrollerVelocity(distance, duration), distance, duration);
                    mScroller.forceFinished(true);
                    return;
                } else {
                    scrollTo(0, currY);
                }
            } else {
                if (mHelper.isTop()) {
                    int deltaY = (currY - mLastScrollerY);
                    int toY = getScrollY() + deltaY;
                    scrollTo(0, toY);
                    if (mCurY <= minY) {
                        mScroller.forceFinished(true);
                        return;
                    }
                }
                invalidate();
            }
            mLastScrollerY = currY;
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        int scrollY = getScrollY();
        int toY = scrollY + y;
        if (toY >= maxY) {
            toY = maxY;
        } else if (toY <= minY) {
            toY = minY;
        }
        y = toY - scrollY;
        super.scrollBy(x, y);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y >= maxY) {
            y = maxY;
        } else if (y <= minY) {
            y = minY;
        }
        mCurY = y;
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(y, maxY);
        }
        super.scrollTo(x, y);
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        recycleVelocityTracker();
        super.onDetachedFromWindow();
    }

    public ScrollableHelper getHelper() {
        return mHelper;
    }

    private boolean isStickied() {
        return mCurY >= maxY;
    }

    private boolean isClickHead(MotionEvent ev) {
        return false;
    }

    enum DIRECTION {
        UP,
        DOWN
    }

    public interface OnScrollListener {
        void onScroll(int currentY, int maxY);
    }
}
