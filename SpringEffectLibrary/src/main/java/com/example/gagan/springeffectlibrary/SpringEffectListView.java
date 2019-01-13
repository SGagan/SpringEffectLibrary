package com.example.gagan.springeffectlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Scroller;

public class SpringEffectListView extends ListView implements AbsListView.OnScrollListener{
    private Scroller mScroller; // used for scroll back
    private float mLastY = -1;
    private View mHeaderView; //Top Image
    private View mFooterView; //Bottom
    private int finalTopHeight;
    private int finalBottomHeight;
    private final static float OFFSET_RADIO = 1.8f;
    // total list items
    private int mTotalItemCount;
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;
    private final static int SCROLL_DURATION = 400; // scroll back duration
    private OnScrollListener mScrollListener; // user's scroll listener

    public SpringEffectListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public SpringEffectListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public SpringEffectListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);

        this.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if(mHeaderView==null){
                            View view=new View(getContext());
                            addHeaderView(view);
                        }
                        if(mFooterView==null){
                            View view=new View(getContext());
                            addFooterView(view);
                        }
                        getViewTreeObserver()
                                .removeOnGlobalLayoutListener(this);
                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && (mHeaderView.getHeight() > finalTopHeight || deltaY > 0)
                        && mHeaderView.getTop() >= 0) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                } else if (getLastVisiblePosition() == mTotalItemCount - 1
                        && (getFooterHeight() >finalBottomHeight || deltaY < 0)) {
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0 && getHeaderHeight() > finalTopHeight) {
                    resetHeaderHeight();
                }
                if (getLastVisiblePosition() == mTotalItemCount - 1 ){
                    if(getFooterHeight() > finalBottomHeight) {
                        resetFooterHeight();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * Setters
     */
    // Set the top height
    private void setHeaderHeight(int height) {
        LayoutParams layoutParams = (LayoutParams) mHeaderView.getLayoutParams();
        layoutParams.height = height;
        mHeaderView.setLayoutParams(layoutParams);
    }

    // Set the bottom height
    private void setFooterViewHeight(int height) {
        LayoutParams layoutParams =
                (LayoutParams) mFooterView.getLayoutParams();
        layoutParams.height =height;
        mFooterView.setLayoutParams(layoutParams);
    }

    /**
     * Getters
     */

    // Get the top height
    public int getHeaderHeight() {
        AbsListView.LayoutParams layoutParams =
                (AbsListView.LayoutParams) mHeaderView.getLayoutParams();
        return layoutParams.height;
    }

    // Get the bottom height
    public int getFooterHeight() {
        AbsListView.LayoutParams layoutParams =
                (AbsListView.LayoutParams) mFooterView.getLayoutParams();
        return layoutParams.height;
    }

    /**
     * Update
     */
    private void updateHeaderHeight(float delta) {
        setHeaderHeight((int) (getHeaderHeight()+delta));
    }

    private void updateFooterHeight(float delta) {
        setFooterViewHeight((int) (getFooterHeight()+delta));
    }

    /**
     * Reset
     */
    private void resetHeaderHeight() {
        int height = getHeaderHeight();
        if (height == 0) // not visible.
            return;
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalTopHeight - height,
                SCROLL_DURATION);
        invalidate();
    }

    private void resetFooterHeight() {
        int bottomHeight = getFooterHeight();
        if (bottomHeight > finalBottomHeight) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomHeight, 0, -bottomHeight+finalBottomHeight,
                    SCROLL_DURATION);
            invalidate();
        }
    }

    // Calculate the slide when the invalidate() is called, the system will automatically call
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                setHeaderHeight(mScroller.getCurrY());
            } else {
                setFooterViewHeight(mScroller.getCurrY());
            }
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    public void addHeaderView(View v) {
        mHeaderView = v;
        super.addHeaderView(mHeaderView);
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if(finalTopHeight==0) {
                            finalTopHeight = mHeaderView.getMeasuredHeight();
                        }
                        setHeaderHeight(finalTopHeight);
                        getViewTreeObserver()
                                .removeOnGlobalLayoutListener(this);
                    }
                });
    }

    @Override
    public void addFooterView(View v) {
        mFooterView = v;
        super.addFooterView(mFooterView);

        mFooterView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if(finalBottomHeight==0) {
                            finalBottomHeight = mFooterView.getMeasuredHeight();
                        }
                        setFooterViewHeight(finalBottomHeight);
                        getViewTreeObserver()
                                .removeOnGlobalLayoutListener(this);
                    }
                });
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }

}
