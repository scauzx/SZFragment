package scauzx.com.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 *
 * @author scauzx
 * @date 2017/11/15
 */

public abstract class BaseCacheStatePagerAdapter extends FragmentStatePagerAdapter {

    private SparseArrayCompat<WeakReference<Fragment>> mFragmentList;

    public BaseCacheStatePagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList = new SparseArrayCompat<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mFragmentList.put(position,new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mFragmentList.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getFragmentAt(int position) {
        if (mFragmentList.get(position) != null) {
            return mFragmentList.get(position).get();
        }
        return null;
    }
}
