package com.coswit.viewpager.pager.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.coswit.R;

/**
 * @author Created by zhengjing on 2020/10/14.
 */
public class PagerView extends FrameLayout {

    private Context context;

    public PagerView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        View root = LayoutInflater.from(context).inflate(R.layout.view_pager, this);
        loadData();
    }


    private void loadData() {
    }
}
