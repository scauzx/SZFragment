package scauzx.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 *
 * @author scauzx
 * @date 2017/11/15
 */

public class FirstFragment extends BaseFragment {

    private static final String TAG = "FirstFragment";
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private static String[] Title = {"First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth", "Ninth"};


    public static FirstFragment getInstance() {
        return new FirstFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG,"FirstFragment setUserVisibleHint = " + isVisibleToUser);
    }

    @Override
    protected void setupView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_first, null);
        mViewPager = mRootView.findViewById(R.id.fragment_first_vp);
        mTabLayout = mRootView.findViewById(R.id.fragment_first_tl);
        mViewPager.setAdapter(new MyAdapter());
        for (int i = 0; i < Title.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < Title.length; i++) {
            if (mTabLayout.getTabAt(i) != null) {
                mTabLayout.getTabAt(i).setText(Title[i]);
            }
        }
    }



    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Title.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setBackgroundResource(R.mipmap.ic_launcher);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
