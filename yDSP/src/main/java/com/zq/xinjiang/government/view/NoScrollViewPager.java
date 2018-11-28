package com.zq.xinjiang.government.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/9/13.
 */

public class NoScrollViewPager extends ViewPager {

    private boolean b = true;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIsCanScroll(boolean b){
        this.b = b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return b && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return b && super.onInterceptTouchEvent(ev);
    }
}
