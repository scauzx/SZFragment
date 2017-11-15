package com.scauzx.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.subscriptions.CompositeSubscription;

/**
 *
 * @author scauzx
 * @date 2017/11/15
 */

public abstract class BasePresenterImp <T extends  IBaseView, M extends IMode> implements IBasePresenter{

    //视图
    @Nullable
    protected T mView;

    @Nullable
    protected M mProxy;

    //Rxjava 订阅者 ps:如果执行unsubscribe()后,此实例不能再使用
    @NonNull
    protected CompositeSubscription mSubscription;

    public BasePresenterImp(@Nullable T view){
        mView = view;
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        if (mSubscription.hasSubscriptions()) {
            mSubscription.unsubscribe();
        }
        if (mProxy != null) {
            mProxy.destroy();
        }
        if (mView != null) {
            mView = null;
        }
    }

}
