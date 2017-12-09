package com.scauzx.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import scauzx.com.myapplication.R;

/**
 *
 * @author scauzx
 * @date 2017/11/17
 */

public class CustomViewPager extends ViewPager {

    /**
     * 是否可滑动
     */
    private boolean mPagingEnable = true;

    /**
     * 滑动时是否开启动画
     */
    private boolean mAnimateSwitchPage = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomViewPager);
        mPagingEnable = a.getBoolean(R.styleable.CustomViewPager_pagingEnable,true);
        mAnimateSwitchPage = a.getBoolean(R.styleable.CustomViewPager_animateSwitchEnable,true);
        a.recycle();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return mPagingEnable && super.canScrollHorizontally(direction);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, mAnimateSwitchPage);
    }
}
