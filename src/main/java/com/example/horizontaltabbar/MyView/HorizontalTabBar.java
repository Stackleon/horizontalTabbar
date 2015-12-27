package com.example.horizontaltabbar.MyView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.horizontaltabbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 2015/12/23.
 */
public class HorizontalTabBar extends HorizontalScrollView {
    private int screenWidth;
    private int lineWidth;
    private int padding;
    private int lineYPoint;
    private int tabNum;

    private int tabId = 0;

    private RadioGroup tabGroup;
    private RadioButton itmeSelect;

    private Context mContext;
    private Paint mPaint;

    public HorizontalTabBar(Context context) {
        this(context, null);
    }

    public HorizontalTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalTabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        //set line width
        mPaint.setStrokeWidth(5.0F);
    }

    public void setTabNum(int num) {
        this.tabNum = num;
    }

    public void setTabName(ArrayList<String> nameList) {
        tabNames = nameList;
    }

    private ArrayList<String> tabNames = null;

    private void initTabList() {
        tabNames = new ArrayList<String>();
        for (int i = 0; i < tabNum; i++) {
            tabNames.add("TAB-" + i);
        }
    }

    private void putTabInGroup() {
        if (tabNames == null) {
            initTabList();
        }
        if (tabNum > 0) {
            if (tabNum < (screenWidth / lineWidth)) {
                lineWidth = screenWidth / tabNum;

                for (int i = 0; i < tabNum; i++) {
                    RadioButton rbt = getFixRadioButton(lineWidth);
                    if (tabNum <= tabNames.size()) {
                        rbt.setText(tabNames.get(i));
                    }
                    rbt.setId(tabId);
                    tabId++;

                    if (i == 0) {
                        rbt.setChecked(true);
                    }

                    tabGroup.addView(rbt);
                }
            } else {
                for (int i = 0; i < tabNum; i++) {
                    RadioButton rbt = (RadioButton) LayoutInflater.from(mContext).inflate(R.layout.base_rbt, null);
                    if (tabNum <= tabNames.size()) {
                        rbt.setText(tabNames.get(i));
                    }

                    rbt.setId(tabId);
                    tabId++;
                    tabGroup.addView(rbt);
                }
            }
            tabGroup.check(0);
        }
    }

    private RadioButton getFixRadioButton(int width) {
        RadioButton rbt = (RadioButton) LayoutInflater.from(mContext).inflate(R.layout.base_rbt, null);
        rbt.setMinWidth(width);
        return rbt;
    }

    private void setGroupClickListener() {
        tabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                group.check(checkedId);
                int XPoint = checkedId * (lineWidth + padding) + padding;
                smoothScrollTo(XPoint, 0);
                slideLineAnimation(XPoint);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        tabGroup = (RadioGroup) getChildAt(0);
        itmeSelect = (RadioButton) LayoutInflater.from(mContext).inflate(R.layout.base_rbt, null);
        itmeSelect.measure(0, 0);
        padding = itmeSelect.getPaddingRight();
        lineWidth = itmeSelect.getMeasuredWidth();
        lineYPoint = itmeSelect.getMeasuredHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        putTabInGroup();
        setGroupClickListener();
    }


    private TranslateAnimation translateAnimation;
    int lastXPoint;

    private View slideLine;
    public void setSlideLine(View view){
        this.slideLine = view;
    }

    public void setSlideLineWidth(){
        if(slideLine!=null&&lineWidth!=0){
            slideLine.setMinimumWidth(lineWidth);
        }
    }
    public void slideLineAnimation(int XPoint) {
        if(slideLine!=null){
            translateAnimation = new TranslateAnimation(lastXPoint, XPoint,0,0);
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(300);
            lastXPoint = XPoint;
            slideLine.startAnimation(translateAnimation);
        }
    }

    private ArrayList<Fragment> fragments;
    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragments = (ArrayList<Fragment>) fragmentList;
    }


    public void useWithFrameLayout() {


    }

    public void useWithViewPager() {


    }

}
