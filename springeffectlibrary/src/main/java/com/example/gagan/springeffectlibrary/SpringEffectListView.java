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
    private int mTotalItemCount;

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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
     //Todo
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
     //Todo
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

    private void updateHeaderHeight(float delta) {
        setHeaderHeight((int) (getHeaderHeight()+delta));
    }

    private void updateFooterHeight(float delta) {
        setFooterViewHeight((int) (getFooterHeight()+delta));
    }

    private void resetHeaderHeight() {
        //Todo
    }

    private void resetFooterHeight() {
       //Todo
    }
}
