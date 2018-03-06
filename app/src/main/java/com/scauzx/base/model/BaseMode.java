package com.scauzx.base.model;

import android.support.annotation.Nullable;

import com.scauzx.base.presenter.IBasePresenter;

/**
 *
 * @author scauzx
 * @date 2018/3/6
 */

public abstract class BaseMode<T extends IBasePresenter> implements IMode {

    @Nullable
    protected T mPresenter;

    @Override
    public void destroy() {
        //释放引用
        if (mPresenter != null) {
            mPresenter = null;
        }
    }
}
