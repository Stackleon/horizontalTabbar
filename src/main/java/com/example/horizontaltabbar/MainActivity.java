package com.example.horizontaltabbar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.example.horizontaltabbar.MyView.HorizontalTabBar;
import com.example.horizontaltabbar.fragment.FirstFragment;
import com.example.horizontaltabbar.fragment.SecondFragment;
import com.example.horizontaltabbar.fragment.ThirdFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private HorizontalTabBar tabBar;
    private View slideLine;
    private ArrayList<String> tabNames;
    private FrameLayout content;
    private ArrayList<Fragment> fragmentsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slideLine = findViewById(R.id.slideLine);
        tabBar = (HorizontalTabBar) findViewById(R.id.horizontalview);
        content = (FrameLayout) findViewById(R.id.content);

        tabBar.setOnPutSlideLineListener(new HorizontalTabBar.OnPutSlideLineListener() {
            @Override
            public View onPutSlide() {
                return slideLine;
            }
        });
        FragmentManager manager = getSupportFragmentManager();

        tabNames = new ArrayList<String>();
        tabNames.add("红色");
        tabNames.add("绿色");
        tabNames.add("蓝色");
        fragmentsList = new ArrayList<Fragment>();
        fragmentsList.add(new FirstFragment());
        fragmentsList.add(new SecondFragment());
        fragmentsList.add(new ThirdFragment());
        tabBar.setFragment(fragmentsList, R.id.content, getSupportFragmentManager());
        tabBar.setTabName(tabNames);
    }
}
