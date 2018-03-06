package com.scauzx.fragments.follow.presenter;

import com.scauzx.base.presenter.IBasePresenter;

import java.util.List;

/**
 *
 * @author scauzx
 * @date 2018/3/6
 */

public interface IFollowPresenter extends IBasePresenter{

    /**
     * 获取个人信息
     */
    void fetchPersonInfo();


    /**
     * 处理个人信息回调
     * @param info 个人信息
     * @param success 是否获取成功
     */
    void handleFetchPersonInfo(List<Object> info, boolean success);
}
