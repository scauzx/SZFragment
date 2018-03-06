package com.scauzx.fragments.follow.model;


import com.scauzx.base.model.BaseMode;
import com.scauzx.fragments.follow.presenter.IFollowPresenter;

/**
 *
 * @author scauzx
 * @date 2018/3/6
 */

public class IFollowModeImp extends BaseMode<IFollowPresenter> implements IFollowMode{

    public IFollowModeImp(IFollowPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void fetchPersonInfo() {
        //TODO 获取数据

        if (mPresenter != null) {
            mPresenter.fetchPersonInfo();
        }


    }
}
