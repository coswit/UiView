package com.coswit.viewpager.pager.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.coswit.R;
import com.coswit.viewpager.pager.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by zhengjing on 2020/10/14.
 */
public class PagerRecyclerView extends FrameLayout {
    private Context context;
    private RecyclerView recyclerView;

    public PagerRecyclerView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }


    private void init() {
        LayoutInflater.from(context).inflate(R.layout.fragment_pager_recyler, this);
        recyclerView = findViewById(R.id.innerscrollview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter adapter = new Adapter(R.layout.item_text);
        recyclerView.setAdapter(adapter);
        List<Bean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new Bean("item---"+i));
        }
        adapter.setNewData(list);
    }


}
class Adapter extends BaseQuickAdapter<Bean, BaseViewHolder> {

    public Adapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bean item) {
        helper.setText(R.id.item_text,item.getName());
    }
}