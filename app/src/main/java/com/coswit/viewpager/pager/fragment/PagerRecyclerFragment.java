package com.coswit.viewpager.pager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.coswit.R;
import com.coswit.viewpager.pager.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by zhengjing on 2020/9/12.
 */
public class PagerRecyclerFragment extends Fragment {


    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pager_recyler, null);
        recyclerView = root.findViewById(R.id.innerscrollview);
        recyclerView.setNestedScrollingEnabled(false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter adapter = new Adapter(R.layout.item_text);
        recyclerView.setAdapter(adapter);
        List<Bean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new Bean("item---"+i));
        }
        adapter.setNewData(list);

    }



    class Adapter extends BaseQuickAdapter<Bean, BaseViewHolder>{

        public Adapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Bean item) {
          helper.setText(R.id.item_text,item.getName());
        }
    }
}
