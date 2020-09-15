package com.coswit.nestedscroll;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.coswit.R;
import com.coswit.nestedscroll.Fragment.BasePagerAdapter;
import com.coswit.nestedscroll.Fragment.PagerFragment;
import com.coswit.nestedscroll.Fragment.PagerRecylerFragment;

import java.util.ArrayList;

public class NestedScrollActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

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

    }
    private void init(){
        fragments.add(new PagerFragment());
        fragments.add(new PagerRecylerFragment());
        fragments.add(new PagerRecylerFragment());
        fragments.add(new PagerRecylerFragment());

        viewPager.setAdapter(new BasePagerAdapter(getSupportFragmentManager(),fragments));
        slidingtablayout.setViewPager(viewPager,mTitles);
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