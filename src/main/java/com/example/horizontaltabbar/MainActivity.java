package com.example.horizontaltabbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.horizontaltabbar.MyView.HorizontalTabBar;

public class MainActivity extends AppCompatActivity {

    private HorizontalTabBar tabBar;
    private View slideLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slideLine = findViewById(R.id.slideLine);
        tabBar = (HorizontalTabBar) findViewById(R.id.horizontalview);
        tabBar.setSlideLine(slideLine);
        tabBar.setSlideLineWidth();
        tabBar.setTabNum(10);
    }
}
