package com.coswit.nestedscroll.Fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;



public class BasePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>(); // Fragment的集合

    private List<String> titles = new ArrayList<>(); // Fragment的标题
    private List<Object> otherTags = new ArrayList<>();//fragment的标识


    public BasePagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public BasePagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, List<Object> tags) {
        super(fm);
        this.fragments = fragments;
        this.otherTags = tags;
    }

    public BasePagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, List<String> names, List<Object> tags) {
        super(fm);
        this.fragments = fragments;
        this.titles = names;
        this.otherTags = tags;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public Object getOtherTagByPosition(int position) {
        try {
            return otherTags.get(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public long getItemId(int position) {
        return fragments.get(position).hashCode();
    }

}
