package scauzx.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.scauzx.presenter.IBasePresenter;

/**
 *
 * @author scauzx
 * @date 2017/11/15
 */

public class BaseFragment <T extends IBasePresenter> extends Fragment {

    protected T mPresenter;
    protected View mRootView;
    private String TAG = "BaseFragment";

    /**
     *     是否已经初始化
     */
    protected boolean mInitialized = false;


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
        Log.d(TAG, this.toString() + "onViewCreated");

        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            setUserVisibleHint(true);
        }

        if (getUserVisibleHint() && !mInitialized) {
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
    private void onTabBuild(View view) {
        Log.d(TAG,"onTabBuild");
    }

    /**
     * Fragment重新可见时要做的操作
     */
    protected void refreshTab(){
        Log.d(TAG,"refreshTab");
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
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mInitialized) {
                refreshTab();
            } else {
                setupTab();
            }
        }
    }
}
