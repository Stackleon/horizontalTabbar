package com.example.horizontaltabbar.MyView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
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
    private int tabNum = 0;

    private int tabId = 0x0;

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


    private ArrayList<String> tabNames = null;

    /*set tab name ,this is must be setting if you not to set ,the system should be init a four size list*/
    public void setTabName(ArrayList<String> nameList) {
        tabNames = nameList;
    }

    public void setTabName(String[] names) {
        if (tabNames == null) {
            tabNames = new ArrayList<String>();
            for (String name : names) {
                tabNames.add(name);
            }
        }
    }

    private void initTabList() {
        tabNames = new ArrayList<String>();
        for (int i = 1; i <= 5; i++) {
            tabNames.add("TAB-" + i);
        }
    }

    private void putTabInGroup() {
        if (tabNames == null) {
            initTabList();
        }
        tabNum = tabNames.size();
        if (tabNum < (screenWidth / lineWidth)) {
            lineWidth = screenWidth / tabNum;

            for (int i = 0; i < tabNum; i++) {
                RadioButton rbt = getFixRadioButton(lineWidth);
                rbt.setText(tabNames.get(i));
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
                rbt.setText(tabNames.get(i));
                rbt.setId(tabId);
                tabId++;
                tabGroup.addView(rbt);
            }
        }
        tabGroup.check(0x0);
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
                int num = Integer.valueOf(checkedId + "", 10);

                Log.i("TAG", "num-->" + num);
                int XPoint = num * (lineWidth + padding) + padding;
                smoothScrollTo(XPoint, 0);
                slideLineAnimation(XPoint);

                if (listener != null) {
                    listener.OnTabClick(group, checkedId, tabNum);
                } else {
                    // Log.i("TAG","fragments--->"+fragments+"    containerViewId--->"+containerViewId+"     manager--->"+manager);
                    if (fragments != null && containerViewId != 0 && manager != null) {
                        for (int i = 0; i < tabNum; i++) {
                            if (num == i) {
                                manager.beginTransaction().replace(containerViewId, fragments.get(i)).commit();
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    private boolean isFirst = true; //为了  自定义View  会不断的重走  onMeasure 和 onLayout

    private void run() {
        tabGroup = (RadioGroup) getChildAt(0);
        itmeSelect = (RadioButton) LayoutInflater.from(mContext).inflate(R.layout.base_rbt, null);
        itmeSelect.measure(0, 0);
        padding = itmeSelect.getPaddingRight();
        lineWidth = itmeSelect.getMeasuredWidth();

        Log.i("TAG","run lineWidth-->"+lineWidth);
        slideLine = slidelineListener.onPutSlide();
        setSlideLineWidth();

        lineYPoint = itmeSelect.getMeasuredHeight();
        putTabInGroup();
        setGroupClickListener();
        isFirst = false;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isFirst) {
            run();
        }
    }


    private TranslateAnimation translateAnimation;
    int lastXPoint;
    private View slideLine;

    public void setSlideLineWidth() {
        if (slideLine != null && lineWidth != 0) {
            Log.i("TAG"," setSlideLineWidth  lineWidth--->"+lineWidth);
            slideLine.setMinimumWidth(lineWidth);
        }
    }



    public interface  OnPutSlideLineListener{
        public View onPutSlide();
    }
    private OnPutSlideLineListener slidelineListener;

    public void setOnPutSlideLineListener(OnPutSlideLineListener slidelineListener){
        this.slidelineListener = slidelineListener;
    }



    public void slideLineAnimation(int XPoint) {
        if (slideLine != null) {
            translateAnimation = new TranslateAnimation(lastXPoint, XPoint, 0, 0);
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(300);
            lastXPoint = XPoint;
            slideLine.startAnimation(translateAnimation);
        }
    }

    private ArrayList<Fragment> fragments;
    private FragmentManager manager;
    private int containerViewId;

    public void setFragment(List<Fragment> fragmentList, int containerViewId, FragmentManager manager) {
        this.fragments = (ArrayList<Fragment>) fragmentList;
        this.containerViewId = containerViewId;
        this.manager = manager;
        manager.beginTransaction().add(containerViewId, fragmentList.get(0)).commit();
    }

    public interface OnTabClickListener {
        public void OnTabClick(RadioGroup group, int checkedId, int totalNums);
    }

    private OnTabClickListener listener;

    public void setOnTabClickListener(OnTabClickListener listener) {
        this.listener = listener;
    }

}
