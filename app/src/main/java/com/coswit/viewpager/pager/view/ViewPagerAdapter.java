package com.coswit.viewpager.pager.view;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by zhengjing on 2020/10/14.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<View> views = new ArrayList<>();

    public ViewPagerAdapter(List<View> views) {
        this.views.addAll(views);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.i("ViewPagerAdapter", "instantiateItem: ");
        View child = views.get(position);
        container.addView(child);
        return child;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Log.i("ViewPagerAdapter", "destroyItem: ");
        container.removeView(views.get(position));
    }

    public View getItem(int position) {
        return views.get(position);
    }
}
