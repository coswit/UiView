package com.coswit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.coswit.View.GraphicView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GraphicView view = findViewById(R.id.gv);
        ArrayList<GraphicView.PointInfo> infos = new ArrayList<>();
        for(int i = 0;i<12;i++) {
            infos.add(new GraphicView.PointInfo("1月",45));
            infos.add(new GraphicView.PointInfo("2月",20));
            infos.add(new GraphicView.PointInfo("3月",12));
            infos.add(new GraphicView.PointInfo("4月",88));
            infos.add(new GraphicView.PointInfo("5月",32));
            infos.add(new GraphicView.PointInfo("6月",67));
            infos.add(new GraphicView.PointInfo("7月",19));
            infos.add(new GraphicView.PointInfo("8月",34));
            infos.add(new GraphicView.PointInfo("9月",66));
            infos.add(new GraphicView.PointInfo("10月",69));
            infos.add(new GraphicView.PointInfo("11月",19));
            infos.add(new GraphicView.PointInfo("12月",59));
        }
        view.setData(infos,88);
    }
}
