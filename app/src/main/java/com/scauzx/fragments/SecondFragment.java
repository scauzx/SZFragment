package com.scauzx.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.scauzx.adapters.SecondImageListAdapter;
import com.scauzx.models.BannerInfo;
import com.scauzx.widget.BannerPageView;

import java.util.ArrayList;
import java.util.List;

import scauzx.com.myapplication.R;

/**
 *
 * @author scauzx
 * @date 2017/11/15
 */

public class SecondFragment extends BaseFragment {
    private String TAG = "SecondFragment";

    private BannerPageView mBannerView;
    private List<BannerInfo> mBannerInfo = new ArrayList<>();
    private List<BannerInfo> mUserImages = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SecondImageListAdapter mAdapter;
    private SwipeRefreshLayout mFreshLayout;
    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    public static SecondFragment getInstance() {
        return new SecondFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_second,null);

    }

    @Override
    protected void onTabBuild(View view) {
        super.onTabBuild(view);
     //   mBannerView = mRootView.findViewById(R.id.banner_view);
        mRecyclerView = mRootView.findViewById(R.id.second_image_recycler);
        mFreshLayout = mRootView.findViewById(R.id.refresh_layout);
        initIamgeInfos(3);
        mAdapter = new SecondImageListAdapter(mUserImages);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new WrappedStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mBannerInfo.add(new BannerInfo("https://raw.githubusercontent.com/scauzx/SZFragment/master/app/src/main/res/drawable/test01.jpg", "https://raw.githubusercontent.com/scauzx/SZFragment/master/app/src/main/res/drawable/test01.jpg"));
        mBannerInfo.add(new BannerInfo("https://raw.githubusercontent.com/scauzx/SZFragment/master/app/src/main/res/drawable/test02.jpg", "https://raw.githubusercontent.com/scauzx/SZFragment/master/app/src/main/res/drawable/test02.jpg"));
        mBannerInfo.add(new BannerInfo("https://raw.githubusercontent.com/scauzx/SZFragment/master/app/src/main/res/drawable/test03.png", "https://raw.githubusercontent.com/scauzx/SZFragment/master/app/src/main/res/drawable/test03.png"));
        mAdapter.setBannerInfos(mBannerInfo);
        mFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initIamgeInfos(2);
                        mFreshLayout.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();                    }
                }, 500);


                Log.i(TAG, "onRefresh: ......");
            }
        });
    }



    public void initIamgeInfos(int count) {
        for (int i = 0; i < count; i++) {
            mUserImages.add(new BannerInfo("https://raw.githubusercontent.com/scauzx/SZFragment/master/app/src/main/res/drawable/test01.jpg", "https://raw.githubusercontent.com/scauzx/SZFragment/master/app/src/main/res/drawable/test01.jpg"));
            mUserImages.add(new BannerInfo("https://raw.githubusercontent.com/scauzx/SZFragment/master/app/src/main/res/drawable/test02.jpg", "https://raw.githubusercontent.com/scauzx/SZFragment/master/app/src/main/res/drawable/test02.jpg"));
            mUserImages.add(new BannerInfo("https://raw.githubusercontent.com/scauzx/SZFragment/master/app/src/main/res/drawable/test03.png", "https://raw.githubusercontent.com/scauzx/SZFragment/master/app/src/main/res/drawable/test03.png"));

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG,"SecondFragment setUserVisibleHint = " + isVisibleToUser);
    }

    private static class WrappedStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

        public WrappedStaggeredGridLayoutManager(int spanCount, int orientation) {
            super(spanCount, orientation);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

}
