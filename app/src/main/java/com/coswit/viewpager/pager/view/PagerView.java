package com.coswit.viewpager.pager.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.coswit.R;

/**
 * @author Created by zhengjing on 2020/10/14.
 */
public class PagerView extends FrameLayout {

    private Context context;
    private WebView webView;

    public PagerView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        View root = LayoutInflater.from(context).inflate(R.layout.fragment_pager, this);
        webView = root.findViewById(R.id.innerscrollview);
        loadData();
    }


    private void loadData(){
        webView.loadUrl("http://ts.dev.yunduoketang.cn/interfaces/getVideoDetail?id=376484&type=class");
    }
}
