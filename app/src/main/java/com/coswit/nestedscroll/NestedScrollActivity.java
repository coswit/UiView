package com.coswit.nestedscroll;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.coswit.R;
import com.coswit.viewpager.pager.fragment.FragmentPageAdapter;
import com.coswit.viewpager.pager.fragment.PagerFragment;
import com.coswit.viewpager.pager.fragment.PagerRecyclerFragment;
import com.coswit.viewpager.pager.view.PagerRecyclerView;
import com.coswit.viewpager.pager.view.PagerView;
import com.coswit.viewpager.pager.view.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NestedScrollActivity extends AppCompatActivity {

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
        shrink = findViewById(R.id.shrink);
        shrink.setText("测试多行文字收缩。。。。。。。。。测试多行文字收缩。。。。。。。。测试多行文字收缩。。。。。。。。");


//        initFragment();

        initViews();

        slidingtablayout.setViewPager(viewPager, mTitles);

    }

    private void initFragment() {
        fragments.add(new PagerRecyclerFragment());
        fragments.add(new PagerFragment());
        fragments.add(new PagerRecyclerFragment());
        fragments.add(new PagerRecyclerFragment());

        viewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(), fragments));
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

    private void initViews() {
        List<View> views = new ArrayList<>();
        views.add(new PagerRecyclerView(this));
        views.add(new PagerRecyclerView(this));
        views.add(new PagerView(this));
        views.add(new PagerRecyclerView(this));
        viewPager.setAdapter(new ViewPagerAdapter(views));

//        viewPager.setOffscreenPageLimit(views.size());
    }


}