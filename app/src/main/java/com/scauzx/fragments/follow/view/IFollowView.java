package com.scauzx.fragments.follow.view;

import com.scauzx.base.view.IBaseView;

import java.util.List;

/**
 *
 * @author scauzx
 * @date 2018/3/6
 */

public interface IFollowView extends IBaseView{

    /**
     * 处理个人信息回调
     * @param info 个人信息
     * @param success 是否获取成功
     */
    void handleFetchPersonInfo(List<Object> info, boolean success);
}
