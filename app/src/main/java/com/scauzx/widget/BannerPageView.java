package com.scauzx.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.scauzx.models.BannerInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import scauzx.com.myapplication.R;

/**
 *Banner轮询方式有2种，
 * 方式1：设置Adapter的长度为无限长，然后向右滑动不断重复之前的view,但要注意要左滑动也有数据得在初始化时设置位置在一定远的位置，不然一进来就处于0位置的话，右滑不会有数据
 * 方式2: 4012340方式
 * 这里使用第一种方式实现
 * @author scauzx
 * @date 2017/12/7
 */

public class BannerPageView extends FrameLayout implements View.OnTouchListener{
    private static final String TAG = BannerPageView.class.getSimpleName();
    private CustomViewPager mViewPager;
    private LinearLayout mIndicator;
    private List<BannerInfo> mBannerInfos = new ArrayList<>();
    private BannerViewPageAdapter mAdapter;
    private final int PAGES_RATIO = 1000;
    private final static int INTERVAL = 2000;
    private final int DOT_SELECTED_BG = R.drawable.banner_indicator_selected;
    private final int DOT_NORMAL_BG = R.drawable.banner_indicator_normal;
    private ImageView[] mDotViews;
    private HashMap<String, Boolean> mDownStatus = new HashMap<>();
    private MyPageChangeListener mPageChangeListener;


    public BannerPageView(@NonNull Context context) {
        this(context, null);
    }

    public BannerPageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerPageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }

    private void setupView() {
        Fresco.initialize(getContext());
        View.inflate(getContext(), R.layout.view_banner_page, this);
        mViewPager = findViewById(R.id.view_pager);
        mIndicator = findViewById(R.id.indicator_container);
        mAdapter = new BannerViewPageAdapter();
        mViewPager.setAdapter(mAdapter);
        mPageChangeListener = new MyPageChangeListener();
        mViewPager.setOnPageChangeListener(mPageChangeListener);
        mViewPager.setOnTouchListener(this);
    }


    public void bindData(List<BannerInfo> list) {
        mBannerInfos.clear();
        mBannerInfos.addAll(list);
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(getInitPosition(), false);
        initIndicator(0);
        startLoop();
    }


    /**
     * 初始化指示器位置
     * @param index
     */
    public void initIndicator(int index) {
        if (mBannerInfos.isEmpty() || mBannerInfos.size() == 1) {
            return;
        }
        mIndicator.removeAllViews();
        if (mDotViews == null) {
            mDotViews = new ImageView[mBannerInfos.size()];
        }
        for (int i = 0; i < mBannerInfos.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20,20);
            layoutParams.rightMargin = 10;
            imageView.setLayoutParams(layoutParams);
            imageView.setBackgroundResource(DOT_NORMAL_BG);
            mDotViews[i] = imageView;
            mIndicator.addView(imageView);
        }
        mDotViews[index].setBackgroundResource(DOT_SELECTED_BG);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    /**
     * 开始图片循环
     */
    public void startLoop() {
        stopLoop();
        postDelayed(mScrollRunnable, INTERVAL);
    }


    /**
     * 停止图片循环
     */
    public void stopLoop() {
        removeCallbacks(mScrollRunnable);
    }


    /**
     * 图片循环任务
     */
    Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "run: mScrollRunnable");
            mViewPager.setCurrentItem(mCurrentIndex + 1, false);
        }
    };

    private int mCurrentIndex;
    public int getInitPosition() {

        if (mBannerInfos.size() == 1) {
            mCurrentIndex = 1;
        } else {
            mCurrentIndex = PAGES_RATIO / 2 * mBannerInfos.size();
        }
        return mCurrentIndex;
    }


    /**
     * 有手势行为时停止轮询任务
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i(TAG, "onTouch: event = " + event.toString());
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                startLoop();
                break;
            default:
                stopLoop();
        }
        return false;
    }


    class BannerViewPageAdapter extends PagerAdapter {
        ArrayList<SZImageView> mImageViewCache = new ArrayList<>();

        @Override
        public int getCount() {
            if (mBannerInfos.isEmpty()) {
                return 0;
            }
            if (mBannerInfos.size() == 1) {
                return 1;
            }
          return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final int rightPos = position % mBannerInfos.size();
            Log.i(TAG, "instantiateItem: rightPos = " + rightPos);
            SZImageView imageView = null;
            if (mImageViewCache.isEmpty()) {
                imageView = new SZImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setBackgroundResource(R.mipmap.ic_launcher);
            } else {
                imageView = mImageViewCache.remove(0);
            }
            imageView.setTag(mBannerInfos.get(rightPos));
            imageView.setImageUrl(mBannerInfos.get(rightPos).downloadUrl, mPageChangeListener);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            SZImageView imageView = (SZImageView)object;
            container.removeView(imageView);
            mImageViewCache.add(imageView);
        }

    }

    public void setDownLoadStatus(String url, boolean status) {
        synchronized (mDownStatus) {
            mDownStatus.put(url, status);
        }
    }

    public Boolean getDownLoadStatus(String url) {
        synchronized (mDownStatus) {
            return mDownStatus.get(url);
        }
    }

    private final class MyPageChangeListener implements ViewPager.OnPageChangeListener,SZImageView.OnLoadBitmapSucListener {


        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                startLoop();
            }
        }


        @Override
        public void onPageScrolled(int scrolledPosition, float percent, int pixels) {
            //empty
        }

        @Override
        public void onPageSelected(int position) {
            Log.i(TAG, "onPageSelected: position = " + position);
            mCurrentIndex = position;
            if (mDotViews == null || mDotViews.length < 1) {
                return;
            }
            int rightPos = mCurrentIndex % mBannerInfos.size();
            for (int i = 0; i < mDotViews.length; i++) {
                if (rightPos == i) {
                    mDotViews[i].setBackgroundResource(DOT_SELECTED_BG);
                    BannerInfo info = mBannerInfos.get(rightPos);
                    Boolean status = getDownLoadStatus(info.downloadUrl);

                    //尝试继续下载
                    if (status != null && !status) {
                        SZImageView view = findViewWithTag(info);
                        view.setImageUrl(info.downloadUrl, this);
                        Log.d(TAG, " re setImageUrl");
                    }
                } else {
                    mDotViews[i].setBackgroundResource(DOT_NORMAL_BG);
                }
            }
        }

        @Override
        public void onLoadBitmapSuc(SZImageView view) {
            BannerInfo info = (BannerInfo) view.getTag();
            setDownLoadStatus(info.downloadUrl, true);
        }

        @Override
        public void onLoadBitmapFail(SZImageView view) {
            BannerInfo info = (BannerInfo) view.getTag();
            setDownLoadStatus(info.downloadUrl,false);
        }
    }

}