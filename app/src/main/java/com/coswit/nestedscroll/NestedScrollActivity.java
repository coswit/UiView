package com.coswit.nestedscroll;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
//    private DragLayout dragLayout;
    View mTopView;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] mTitles = {"介绍", "课程", "评价", "动态"};
    private ShrinkableText shrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scroll);

        viewPager = findViewById(R.id.viewpager);
//        dragLayout = findViewById(R.id.layout);
        slidingtablayout = findViewById(R.id.slidingtablayout);
        mTopView = findViewById(R.id.topview);
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
        shrink = findViewById(R.id.shrink);

        fragments.add(new PagerRecylerFragment());
        fragments.add(new PagerFragment());
        fragments.add(new PagerRecylerFragment());
        fragments.add(new PagerRecylerFragment());

        viewPager.setAdapter(new BasePagerAdapter(getSupportFragmentManager(), fragments));
        slidingtablayout.setViewPager(viewPager, mTitles);
        TextView tv = new TextView(this);
        tv.setText("");
//        DragLayout layout = findViewById(R.id.layout);
//        shrink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                dragLayout.scrolledUp();
//            }
//        });
//        layout.setDragListener(new DragLayout.DragChangeListener() {
//            @Override
//            public void onDragChanged(float alpha) {
//                shrink.setAlpha(alpha);
//            }
//        });

    }


    boolean isMeasured = false;
    boolean initial;
    int lineEnd = 0;
    int lineCount;
    float percent = 0;

    public static final int MAX = 2;

    private void measuredText(final String content) {

        shrink.setText(content);
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