package com.scauzx.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import scauzx.com.myapplication.FirstFragment;
import scauzx.com.myapplication.FollowFragment;
import scauzx.com.myapplication.FourthFragment;
import scauzx.com.myapplication.R;
import scauzx.com.myapplication.SecondFragment;
import scauzx.com.myapplication.ThirdFragment;

/**
 * Created by Administrator on 2017/11/16.
 */

public class DataGenerator {

    public static final int []mTabRes = new int[]{R.mipmap.auth_icon_twitter,R.mipmap.auth_icon_twitter,R.mipmap.auth_icon_twitter,R.mipmap.auth_icon_twitter};
    public static final int []mTabResPressed = new int[]{R.mipmap.auth_icon_twitter,R.mipmap.auth_icon_twitter,R.mipmap.auth_icon_twitter, R.mipmap.auth_icon_twitter};
    public static final String []mTabTitle = new String[]{"首页","发现","关注","我的"};




    public static SparseArrayCompat<WeakReference<Fragment>> getFragments(){
        SparseArrayCompat<WeakReference<Fragment>> fragments = new SparseArrayCompat<>();
        for (int i = 0; i < mTabTitle.length; i++) {
            switch (i) {
                case 0:
                    fragments.put(i,new WeakReference<Fragment>(FollowFragment.getInstance()));
                    break;
                case 1:
                    fragments.put(i,new WeakReference<Fragment>(SecondFragment.getInstance()));
                    break;
                case 2:
                    fragments.put(i,new WeakReference<Fragment>(ThirdFragment.getInstance()));
                    break;
                case 3:
                    fragments.put(i,new WeakReference<Fragment>(ThirdFragment.getInstance()));
                    break;
                default:
                    fragments.put(i,new WeakReference<Fragment>(ThirdFragment.getInstance()));
                    break;
            }

        }
        return fragments;
    }

    /**
     * 获取Tab 显示的内容
     * @param context
     * @param position
     * @return
     */
    public static View getTabView(Context context, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.home_tab_content,null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }
}