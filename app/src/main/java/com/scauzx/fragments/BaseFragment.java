package com.scauzx.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.scauzx.base.presenter.IBasePresenter;
import com.scauzx.base.view.IBaseView;

/**
 * 针对有ViewPager的Fragment 才会有setUserVisibleHint ，防止ViewPager一进入就初始化多个Fragment
 * @author scauzx
 * @date 2017/11/15
 */

public class BaseFragment <T extends IBasePresenter> extends Fragment implements IBaseView {

    protected T mPresenter;
    protected View mRootView;
    private String TAG = BaseFragment.class.getSimpleName();

    protected boolean mMenuVisibleToUser = true;

    /**
     *     是否已经初始化
     */
    protected boolean mInitialized = false;

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        mMenuVisibleToUser = menuVisible;
        Log.d(TAG, "setMenuVisibility=" + menuVisible);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, this.toString() + "onCreateView");
        if (mRootView == null) {
            setupView(inflater);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();

        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, this.toString() + "onViewCreated " + "mInitialized = " + mInitialized );

        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            //setUserVisibleHint(true);
        }

        if (getUserVisibleHint() && !mInitialized && mMenuVisibleToUser) {
            setupTab();
        }
    }



    /**
     * Fragment第一次可见时
     */
    protected void setupTab(){
        View view = getView();
        if (view == null) {
            return;
        }
        mInitialized = true;
        view.setVisibility(View.VISIBLE);
        onTabBuild(view);
    }


    /**
     * 初始化，构建tab的内容，并请求数据
     *
     * @param view
     */
    protected void onTabBuild(View view) {
        Log.d(TAG, this.toString() + " onTabBuild");
    }

    /**
     * Fragment重新可见时要做的操作
     */
    protected void refreshTab(){
        Log.d(TAG, this.toString() + "refreshTab");
    }

    protected void setupView(LayoutInflater inflater) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d(TAG, "Fragment = " + toString() + "isVisibleToUser = " + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mMenuVisibleToUser) {
            if (mInitialized) {
                refreshTab();
            } else {
                setupTab();
            }
        }
    }
}
