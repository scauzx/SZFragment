package com.scauzx.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.widget.FrameLayout;

import com.scauzx.fragments.FourthFragment;
import com.scauzx.fragments.SecondFragment;
import com.scauzx.fragments.ThirdFragment;
import com.scauzx.fragments.follow.view.FollowFragment;
import com.scauzx.utils.FragmentUtils;
import com.scauzx.utils.OsUtil;

import java.lang.ref.WeakReference;

import scauzx.com.myapplication.R;

/**
 * @author scauzx
 * @date 2017/11/16
 */

public class BottomTabLayoutActivity extends BaseActivity {
    private TabLayout mTabLayout;
    private FrameLayout mRootView;
    public static final int[] mTabRes = new int[]{R.mipmap.auth_icon_twitter, R.mipmap.auth_icon_twitter,  R.mipmap.auth_icon_twitter, R.mipmap.auth_icon_twitter, R.mipmap.auth_icon_twitter};
    public static final int[] mTabResPressed = new int[]{R.mipmap.auth_icon_twitter, R.mipmap.auth_icon_twitter, R.mipmap.auth_icon_twitter,  R.mipmap.auth_icon_twitter, R.mipmap.auth_icon_twitter};
    public static final String[] mTabTitle = new String[]{"首页", "发现", "关注", "我的"};
    private int[] mStatusBarColors = {Color.TRANSPARENT, Color.CYAN, Color.TRANSPARENT, Color.BLACK, Color.BLUE};
    private SparseArrayCompat<WeakReference<Fragment>> mFragments = new SparseArrayCompat<>();
    private String[] mFragmentTags ;
    private final String STATE_SAVE_FRAGMENT_TAGS = "STATE_SAVE_FRAGMENT_TAGS";
    private final String STATE_SAVE_TAB_CHOOSE = "STATE_SAVE_TAB_CHOOSE";
    private int mIndexChoose = 0;

    /**
     * 空白Tab下标
     */
    private final int INDEX_EMPTY_TAB = 2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mFragmentTags = savedInstanceState.getStringArray(STATE_SAVE_FRAGMENT_TAGS);
            mIndexChoose = savedInstanceState.getInt(STATE_SAVE_TAB_CHOOSE);
        }
        if (mFragmentTags == null) {
            mFragmentTags = new String[5];
        }
        setContentView(R.layout.activity_tabs);
        setupView();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mFragmentTags != null) {
            outState.putStringArray(STATE_SAVE_FRAGMENT_TAGS, mFragmentTags);
            outState.putInt(STATE_SAVE_TAB_CHOOSE, mTabLayout.getSelectedTabPosition());
        }

    }

    private void setupView() {

        mRootView = (FrameLayout) findViewById(R.id.activity_tabs_rootview);
        mTabLayout = (TabLayout) findViewById(R.id.bottom_tab_layout);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == INDEX_EMPTY_TAB) {
                    return;
                }
                onTabItemSelected(tab.getPosition());

                //改变Tab 状态
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    if (i == tab.getPosition()) {
                        mTabLayout.getTabAt(i).setIcon(getResources().getDrawable(mTabResPressed[i]));
                    } else {
                        if (i != INDEX_EMPTY_TAB) {
                            mTabLayout.getTabAt(i).setIcon(getResources().getDrawable(mTabRes[i]));
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.auth_icon_twitter)).setText(mTabTitle[0]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.auth_icon_twitter)).setText(mTabTitle[1]));
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.auth_icon_twitter)).setText(mTabTitle[2]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.auth_icon_twitter)).setText(mTabTitle[3]));
        mTabLayout.getTabAt(mIndexChoose).select();
        findViewById(R.id.activity_main_cv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BottomTabLayoutActivity.this,MainActivity.class));
            }
        });
    }

    private void onTabItemSelected(int position) {
        Fragment fragment = null;

        if (mFragments.get(position) != null) {
            fragment = mFragments.get(position).get();
        }
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = FollowFragment.getInstance();
                    break;
                case 1:
                    fragment = SecondFragment.getInstance();
                    break;
                case 3:
                    fragment = ThirdFragment.getInstance();
                    break;
                case 4:
                    fragment = FourthFragment.getInstance();
                    break;
                default:
                    fragment = FourthFragment.getInstance();
                    break;
            }
        }

        if (fragment instanceof FollowFragment) {
            mRootView.setPadding(0, 0, 0, 0);
        } else {
            mRootView.setPadding(0, OsUtil.getStatusBarHeight(this), 0, 0);
        }

        if (mStatusBarColors != null && mStatusBarColors.length >= position + 1) {
            changeStatusBarColor(mStatusBarColors[position]);
        }
        FragmentManager manager = getSupportFragmentManager();
        //解决重叠问题，选中Fragment与已经添加到Frgment中的不一样，那么将不一样的隐藏，如果已经出现过,那么则show，如果没有出现过，就add
        boolean isAdded = false;
        if (fragment != null) {
            if (mFragmentTags != null) {
                for (String tag : mFragmentTags) {
                    Fragment object = getSupportFragmentManager().findFragmentByTag(tag);
                    if (object != null) {
                        if (FragmentUtils.isEquals(object,fragment)) {
                            manager.beginTransaction().show(object).commit();
                            isAdded = true;
                            if (object != fragment) {
                                mFragments.put(position, new WeakReference<>(object));
                            }
                        } else {
                            manager.beginTransaction().hide(object).commit();
                        }
                    }
                }
            }

            if (!isAdded) {
                manager.beginTransaction().add(R.id.home_container, fragment,fragment.getClass().getName()).commit();
                mFragmentTags[position] = fragment.getClass().getName();
                mFragments.put(position, new WeakReference<>(fragment));
            }
        }


    }

}