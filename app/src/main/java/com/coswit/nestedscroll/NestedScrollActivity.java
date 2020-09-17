package com.coswit.nestedscroll;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.coswit.R;
import com.coswit.nestedscroll.Fragment.BasePagerAdapter;
import com.coswit.nestedscroll.Fragment.PagerFragment;
import com.coswit.nestedscroll.Fragment.PagerRecylerFragment;

import java.util.ArrayList;

public class NestedScrollActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    SlidingTabView slidingtablayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] mTitles = {"介绍", "课程", "评价", "动态"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scroll);


        viewPager = findViewById(R.id.viewpager);
        slidingtablayout = findViewById(R.id.slidingtablayout);
        init();

        String s =
                "" +
                        "TextView主要用于给用户展示文字，并且让用户随意的可以对文字进行编辑。但是普通的TextView是不允许用来编辑的，只有EditText才可以。" +
                        "主要用于给用户展示文字，并且让用户随意的可以对文字进行编辑。但是普通的TextView是不允许用来编辑的，只有才可以。" +
                        "如果在XML中设置了android:textIsSelectable 或者在Java代码中调用了setTextIsSelectable(true)方法，\n" +
                        "如果在XML中设置了android:textIsSelectable 或者在Java代码中调用了setTextIsSelectable(true)方法，\n" +
                        "如果在XML中设置了android:textIsSelectable 或者在Java代码中调用了setTextIsSelectable(true)方法，\n" +
                        "如果在XML中设置了android:textIsSelectable 或者在Java代码中调用了setTextIsSelectable(true)方法，" +
                        "如果在XML中设置了android:textIsSelectable 或者在Java代码中调用了setTextIsSelectable(true)方法,Ja" +
                        "就可以允许对TextView的部分或者全部文字进行复制，然后粘贴到其他地方。textIsSelectable 标签是允许用户在TextVie。标签";
        measuredText(s);

    }

    private void init() {
        fragments.add(new PagerFragment());
        fragments.add(new PagerRecylerFragment());
        fragments.add(new PagerRecylerFragment());
        fragments.add(new PagerRecylerFragment());

        viewPager.setAdapter(new BasePagerAdapter(getSupportFragmentManager(), fragments));
        slidingtablayout.setViewPager(viewPager, mTitles);
        TextView tv = new TextView(this);
        tv.setText("");

    }


    boolean isMeasured = false;
    boolean initial;
    int lineEnd = 0;
    int lineCount;
    float percent = 0;

    public static final int MAX = 2;

    private void measuredText(final String content) {
        final TextView tv = findViewById(R.id.tv_desc);
        final TextView hint = findViewById(R.id.tv_hint);
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hint.setSelected(!hint.isSelected());
                tv.requestLayout();
                isMeasured = false;
            }
        });
        tv.setText(content);

        tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isMeasured) {
                    return;
                }
                if (!initial) {
                    lineCount = tv.getLineCount();
                    if (lineCount > MAX) {
                        Layout layout = tv.getLayout();
                        lineEnd = layout.getLineEnd(1);
                        float lastLineWidth = layout.getLineWidth(Math.max(lineCount - 1, 0));
                        int width = tv.getMeasuredWidth();
                        percent = lastLineWidth / width;
                    }
                    initial = true;
                }
                if (lineCount > MAX) {
                    hint.setVisibility(View.VISIBLE);
                    if (!hint.isSelected()) {
                        tv.setLines(2);
                        String part = content.substring(0, Math.max(lineEnd - 2, 0)) + "...";
                        tv.setText(part);
                        hint.setText("全部");
                        hint.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                ContextCompat.getDrawable(NestedScrollActivity.this, R.mipmap.arrow_down_teacher), null);
                    } else {
                        //最后一行文字剩余空白
                        boolean isOut = percent > 0.86f;
                        tv.setLines(isOut ? lineCount + 1 : lineCount);
                        tv.setText(content);
                        hint.setText("收起");
                        hint.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                ContextCompat.getDrawable(NestedScrollActivity.this, R.mipmap.arrow_up__teacher), null);
                    }
                } else {
                    tv.setText(content);
                    hint.setVisibility(View.GONE);
                }

                isMeasured = true;
            }
        });

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}