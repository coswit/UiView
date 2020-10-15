package com.coswit.viewpager.pager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coswit.R;

/**
 * @author Created by zhengjing on 2020/9/12.
 */
public class PagerFragment extends Fragment {


    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pager, null);
        webView = root.findViewById(R.id.innerscrollview);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.loadUrl("http://ts.dev.yunduoketang.cn/interfaces/getVideoDetail?id=376484&type=class");
    }
}
