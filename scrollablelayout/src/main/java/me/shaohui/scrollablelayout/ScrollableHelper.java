package me.shaohui.scrollablelayout;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * Created by shaohui on 16/8/18.
 */
public class ScrollableHelper {

    private View mCurrentScrollableView;
    private ScrollableContainer mScrollableContainer;
    private int sysVersion = Build.VERSION.SDK_INT;

    private View getScrollableView() {
        if (mCurrentScrollableView != null) {
            return mCurrentScrollableView;
        } else if (mScrollableContainer != null) {
            return mScrollableContainer.getScrollableView();
        }
        return null;
    }

    public void setCurrentScrollableView(View view) {
        this.mCurrentScrollableView = view;
    }

    public void setScrollableContainer(ScrollableContainer container) {
        this.mScrollableContainer = container;
    }

    public boolean isTop() {
        View scrollableView = getScrollableView();
        if (scrollableView == null) {
            return false;
        }
        if (scrollableView instanceof RecyclerView) {
            return isRecyclerViewTop((RecyclerView) scrollableView);
        }
        return false;
    }

    private static boolean isRecyclerViewTop(RecyclerView recyclerView) {
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                if (childAt == null) {
                    return true;
                }
                if (firstVisibleItemPosition == 0) {
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                    int topMargin = lp.topMargin;
                    int top = childAt.getTop();
                    if (top >= topMargin) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SuppressLint("NewApi")
    public void smoothScrollBy(int velocityY, int distance, int duration) {
        View scrollableView = getScrollableView();
        if (scrollableView instanceof AbsListView) {
            AbsListView absListView = (AbsListView) scrollableView;
            if (sysVersion >= Build.VERSION_CODES.LOLLIPOP) {
                absListView.fling(velocityY);
            } else {
                absListView.smoothScrollBy(distance, duration);
            }
        } else if (scrollableView instanceof ScrollView) {
            ((ScrollView) scrollableView).fling(velocityY);
        } else if (scrollableView instanceof RecyclerView) {
            ((RecyclerView) scrollableView).fling(0, velocityY);
        } else if (scrollableView instanceof WebView) {
            ((WebView)scrollableView).flingScroll(0,velocityY);
        }
    }

    public interface ScrollableContainer {
        View getScrollableView();
    }

}
