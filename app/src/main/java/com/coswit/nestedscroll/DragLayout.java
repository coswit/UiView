package com.coswit.nestedscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.coswit.R;

/**
 * @author Created by zhengjing on 2020/9/12.
 */
public class DragLayout extends LinearLayout {

    private static final String TAG = "DragLayout";
    private int mLastMotionY;
    private int mTouchSlop;
    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity, mMinimumVelocity;
    private Context mContext;

    private ViewPager mViewPager;
    private View mTopView;
    private View mTabView;
    private int mTopHeight;

    private boolean isTopHidden;
    private boolean mDragging = false;
    private DragChangeListener listener;


    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initScroller();
    }

    private void initScroller() {
        mScroller = new OverScroller(mContext);
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(mContext).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(mContext).getScaledMinimumFlingVelocity();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mViewPager = findViewById(R.id.viewpager);
        mTopView = findViewById(R.id.topview);
        mTabView = findViewById(R.id.slidingtablayout);
    }

    private View getCurrentScrollView() {
        int currentItem = mViewPager.getCurrentItem();
        PagerAdapter adapter = mViewPager.getAdapter();
        Fragment fragment = (Fragment) adapter.instantiateItem(mViewPager, currentItem);
        View view = fragment.getView();
        if (view != null) {
            return view.findViewById(R.id.innerscrollview);
        }
        return null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mTopHeight = mTopView.getMeasuredHeight();
        Log.i(TAG, "onSizeChanged: " + mTopHeight);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int actionMasked = ev.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final int y = (int) ev.getY();
                int deltaY = mLastMotionY - y;
                if (Math.abs(deltaY) > mTouchSlop) {
                    mDragging = true;
                    boolean interceptEvent = isInterceptEvent(deltaY);
//                    Log.i(TAG, "onInterceptTouchEvent: " + interceptEvent + "::"
//                            + deltaY + "..." + isTopHidden + "::" + getCurrentScrollView().canScrollVertically(-1));
                    if (interceptEvent) {
                        initVelocityTrackerIfNotExists();
                        mVelocityTracker.addMovement(ev);
                        mLastMotionY = y;
                        return true;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                mDragging = false;
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 是否拦截处理
     */
    private boolean isInterceptEvent(int deltaY) {
        View currentScrollView = getCurrentScrollView();
        if (currentScrollView != null) {
            return !isTopHidden || deltaY < 0 && (!currentScrollView.canScrollVertically(-1)) && isTopHidden;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.i(TAG, "onTouchEvent: ");
        initVelocityTrackerIfNotExists();
        final int actionMasked = ev.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                final int y = (int) ev.getY();
                int deltaY = mLastMotionY - y;
                Log.i(TAG, "onTouchEvent:  " + deltaY);
                if (!mDragging && deltaY > mTouchSlop) {
                    mDragging = true;
                }
                if (mDragging) {
                    scrollBy(0, deltaY);
                    // 如果topView隐藏，且上滑动时，则改变当前事件为ACTION_DOWN
                    if (getScrollY() == mTopHeight && deltaY > 0) {
                        ev.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(ev);
                    }
                }

                mLastMotionY = y;
                break;

            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                recycleVelocityTracker();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                recycleVelocityTracker();
                break;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }


    @Override
    public void scrollTo(int x, int y) {
        if (y > mTopHeight) {
            y = mTopHeight;
        }
        if (y < 0) {
            y = 0;
        }
        super.scrollTo(x, y);
        isTopHidden = getScrollY() == mTopHeight;
        if (listener != null) {
            float alpha = 1.0f;
            if (mTopHeight > 0) {
                alpha = y * 1.0f / mTopHeight;
            }
            listener.onDragChanged(Math.min(alpha, 1.0f));
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = getMeasuredHeight() - mTabView.getMeasuredHeight();
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

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopHeight);
        invalidate();
    }

    public void scrolledUp() {
        mScroller.fling(0, getScrollY(), 0, 4600, 0, 0, 0, mTopHeight);
        invalidate();
    }

    public void setDragListener(DragChangeListener listener) {
        this.listener = listener;
    }


    interface DragChangeListener {
        void onDragChanged(float alpha);
    }
}
