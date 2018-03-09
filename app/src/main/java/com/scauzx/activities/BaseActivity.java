package com.scauzx.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.scauzx.base.presenter.IBasePresenter;
import com.scauzx.base.view.IBaseView;

import java.lang.ref.WeakReference;

/**
 *
 * @author scauzx
 * @date 2017/11/15
 */

public class BaseActivity <T extends IBasePresenter>extends AppCompatActivity implements IBaseView{

    protected T mPresenter;

    public WeakReference<BaseActivity> mCurrentActivity = new WeakReference<>(null);
    public WeakReference<BaseActivity> mLastActivity = new WeakReference<>(null);

    /**
     * 存活着的activity数量
     */
    public static int sAliveActivityCount;

    /**
     * 可见的Activity数量
     */
    public static int sVisibleActivityCount;
    protected Handler mUIHandler = new Handler(Looper.getMainLooper());


    /**
     * 判断客户端是否处于前台
     * @return
     */
    public boolean isApplicationVisable() {
        return sVisibleActivityCount > 0 ? true:false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        sAliveActivityCount++;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLastActivity = new WeakReference<>(mCurrentActivity.get());
        mCurrentActivity = new WeakReference<BaseActivity>(this);
        sVisibleActivityCount++;
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sVisibleActivityCount--;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sAliveActivityCount--;
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    /**
     * 修改StatusBar颜色
     */
    protected void changeStatusBarColor(@ColorInt int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
