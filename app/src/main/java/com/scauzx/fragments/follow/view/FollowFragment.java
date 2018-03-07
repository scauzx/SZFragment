package com.scauzx.fragments.follow.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.scauzx.fragments.BaseCacheStatePagerAdapter;
import com.scauzx.fragments.BaseFragment;
import com.scauzx.fragments.FirstFragment;
import com.scauzx.fragments.FourthFragment;
import com.scauzx.fragments.SecondFragment;
import com.scauzx.fragments.ThirdFragment;
import com.scauzx.fragments.follow.presenter.FollowPresenter;
import com.scauzx.fragments.follow.presenter.IFollowPresenter;
import com.scauzx.widget.PagerSlidingTabStrip;
import java.util.List;
import scauzx.com.myapplication.R;

/**
 *
 * @author scauzx
 * @date 2017/11/16
 */

public class FollowFragment extends BaseFragment<IFollowPresenter> implements IFollowView{

    private Toolbar mToolBar;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mTabLayout;
    private String[] Title = {"First", "Second", "Third", "Fourth"};
    private MyAdapter mMyAdapter;


    public static FollowFragment getInstance() {
        return new FollowFragment();
    }


    @Override
    protected void setupView(LayoutInflater inflater) {
        super.setupView(inflater);
        mRootView = inflater.inflate(R.layout.fragment_main, null);
    }


    @Override
    protected void onTabBuild(View view) {
        super.onTabBuild(view);
        mPresenter = new FollowPresenter(this);
        mTabLayout =  mRootView.findViewById(R.id.activity_main_tablayout);
        mMyAdapter = new MyAdapter(getFragmentManager());
        mViewPager = mRootView.findViewById(R.id.activity_main_viewpager);
        mViewPager.setAdapter(mMyAdapter);
        mTabLayout.setOnTabStateChangeListener(mMyAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        initToolBar();
        mPresenter.fetchPersonInfo();
    }

    @Override
    protected void refreshTab() {
        super.refreshTab();
        mRootView.setPadding(0, 0, 0, 0);
    }

    private void initToolBar() {
        mToolBar = mRootView.findViewById(R.id.toolbar);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getActivity().getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
    }

    @Override
    public void handleFetchPersonInfo(List<Object> info, boolean success) {
        //这里只是提供mvp使用例子，这个暂不实现
    }


    class MyAdapter extends BaseCacheStatePagerAdapter implements PagerSlidingTabStrip.OnTabStateChangeListener,PagerSlidingTabStrip.ViewTabProvider{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (Title == null || Title.length < position + 1) {
                return "";
            }
            return Title[position];
        }

        @Override
        public Fragment getItem(int position) {
            if (getFragmentAt(position) != null) {
                return getFragmentAt(position);
            }
            switch (position) {
                case 0:
                    return FirstFragment.getInstance();
                case 1:
                    return SecondFragment.getInstance();
                case 2:
                    return ThirdFragment.getInstance();
                default:
                    return FourthFragment.getInstance();
            }
        }

        @Override
        public int getCount() {
            return Title.length;
        }


        @Override
        public void onTabStateChange(View view, int position, boolean isSelected) {
            if (!isAdded() || view == null) {
                return;
            }
            if (view instanceof TextView) {
                ((TextView)view).setTextColor(Color.parseColor(isSelected ? "#FFCE46EC" : "#999999"));
                ((TextView)view).setTypeface(null, isSelected ? Typeface.BOLD : Typeface.NORMAL);
            } else {
                TabViewHolder holder = (TabViewHolder) view.getTag();
                holder.text.setTextColor(Color.parseColor(isSelected ? "#FFCE46EC" : "#999999"));
                holder.text.setTypeface(null, isSelected ? Typeface.BOLD : Typeface.NORMAL);
            }
        }

        @Override
        public View getPageView(int position) {
            TabViewHolder tabViewHolder = TabViewHolder.newTab(getActivity());
            tabViewHolder.text.setText(getPageTitle(position));
            tabViewHolder.icon.setImageResource(R.mipmap.auth_icon_twitter);
            return tabViewHolder.tabView;
        }


    }
    static class TabViewHolder {
        View tabView;
        TextView text;
        ImageView icon;

        static TabViewHolder newTab(Context context) {
            TabViewHolder holder = new TabViewHolder();
            View view = View.inflate(context, R.layout.fragment_tab_item, null);
            holder.tabView = view;
            holder.text =  view.findViewById(R.id.tab_item_tv);
            holder.icon =  view.findViewById(R.id.tab_item_iv);
            view.setTag(holder);
            return holder;
        }
    }
}
