package com.coswit.nestedscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * @author Created by zhengjing on 2020/9/12.
 */
public class CustomNestedScrollView extends NestedScrollView {

    final int MAX_SCROLL_LENGTH = 400;
    public static final String TAG = "CustomNestedScrollView";
    /**
     * 该控件滑动的高度，高于这个高度后交给子滑动控件
     */
    int mParentScrollHeight =400;
    int mScrollY ;
    public CustomNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }





    /**
     * 子控件告诉父控件 开始滑动了
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     * 如果有就返回true
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);

//        if (mScrollY < mParentScrollHeight) {
//            consumed[0] = dx;
//            consumed[1] = dy;
//            scrollBy(0, dy);
//        }

        Log.d(TAG,"dx " + dx + " dy "+ dy +  " " + consumed[0]  + " " + consumed[1] + " scrollY " + mScrollY);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mScrollY = t;
    }
}
