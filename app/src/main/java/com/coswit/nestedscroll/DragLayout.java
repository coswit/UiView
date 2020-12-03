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

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.coswit.R;
import com.coswit.viewpager.pager.fragment.FragmentPageAdapter;
import com.coswit.viewpager.pager.view.ViewPagerAdapter;

/**
 * @author Created by zhengjing on 2020/9/12.
 */
public class DragLayout extends LinearLayout {

    private static final String TAG = "DragLayout";
    private int mLastMotionY;
    private int mLastMotionX;
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
    private boolean mIsBeingDragged = false;
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;

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
        View curView = null;
        int currentItem = mViewPager.getCurrentItem();
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter instanceof FragmentPageAdapter) {
            curView = ((FragmentPageAdapter) adapter).getItem(currentItem).getView();
        }
        if (adapter instanceof ViewPagerAdapter) {
            curView = ((ViewPagerAdapter) adapter).getItem(currentItem);

        }
        if (curView != null) {
            return curView.findViewById(R.id.innerscrollview);
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
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE && (mIsBeingDragged))) {
            return true;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        final int actionMasked = ev.getActionMasked();
        switch (actionMasked & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    break;
                }
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    break;
                }
                final int y = (int) ev.getY(pointerIndex);
                final int x = (int) ev.getX(pointerIndex);
                int deltaY = mLastMotionY - y;
                final int yDiff = Math.abs(deltaY);
                final int xDiff = Math.abs(mLastMotionX - x);
                boolean interceptEvent = isInterceptEvent(deltaY);
                if (yDiff > mTouchSlop || yDiff > xDiff) {
                    if (interceptEvent) {
                        mIsBeingDragged = true;
                        mLastMotionY = y;
                    }
                }
                break;

            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                mLastMotionX = (int) ev.getX();
                mActivePointerId = ev.getPointerId(0);
                mScroller.computeScrollOffset();
                mIsBeingDragged = !mScroller.isFinished();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                mVelocityTracker.clear();
                break;
            default:
                break;
        }
        return mIsBeingDragged;
    }

    /**
     * 是否拦截处理
     */
    private boolean isInterceptEvent(int deltaY) {
        View currentScrollView = getCurrentScrollView();
//        if (currentScrollView != null) {
//            return !isTopHidden || deltaY < 0 && (!currentScrollView.canScrollVertically(-1)) && isTopHidden;
//        }
        if (currentScrollView != null) {
            boolean canScrollDown = currentScrollView.canScrollVertically(-1);
            if (isTopHidden) {
                return deltaY < 0 && !canScrollDown;
            } else {
                if (deltaY < 0 && canScrollDown) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        final int actionMasked = ev.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionY = (int) ev.getY();
                mActivePointerId = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    break;
                }
                final int y = (int) ev.getY(activePointerIndex);
                int deltaY = mLastMotionY - y;
                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    mIsBeingDragged = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
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
                if (mIsBeingDragged) {
                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;
                }
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity(mActivePointerId);

                if (mIsBeingDragged) {
                    if (Math.abs(initialVelocity) > mMinimumVelocity) {
                        fling(-initialVelocity);
                    }
                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;
                }
                velocityTracker.clear();
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = ev.getActionIndex();
                mLastMotionY = (int) ev.getY(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                mLastMotionY = (int) ev.getY(ev.findPointerIndex(mActivePointerId));
                break;
        }


//        return super.onTouchEvent(ev);
        return true;
    }


    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
                MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionY = (int) ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        Log.i(TAG, "scrollTo: " + y);
        if (y > mTopHeight) {
            y = mTopHeight;
        }
        if (y < -200) {
            y = -200;
        }
        super.scrollTo(x, y);
        if (y < 0 && mScroller.springBack(0, y, 0, 0, 0, 0)) {
            postInvalidate();
        }
//        if (y > mTopHeight) {
//            mScroller.springBack(0,y,0,0,0,mTopHeight);
//        }
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


    public void fling(int velocityY) {
        Log.i(TAG, "fling: " + velocityY + ":::" + mMinimumVelocity);
        mScroller.fling(0, getScrollY(), 0, velocityY, 0,
                0, 0, mTopHeight);
        invalidate();
//        postInvalidateOnAnimation();
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
