package com.scauzx.fragments.follow.model;


import com.scauzx.base.model.BaseMode;
import com.scauzx.fragments.follow.presenter.IFollowPresenter;

/**
 *
 * @author scauzx
 * @date 2018/3/6
 */

public class FollowModeImp extends BaseMode<IFollowPresenter> implements IFollowMode{

    public FollowModeImp(IFollowPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void fetchPersonInfo() {
        //TODO 获取数据
        if (mPresenter != null) {
            mPresenter.handleFetchPersonInfo(null, false);
        }
    }
}
