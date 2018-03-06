package com.scauzx.fragments.follow.model;

import com.scauzx.base.model.IMode;

/**
 *
 * @author scauzx
 * @date 2018/3/6
 */

public interface IFollowMode extends IMode{

    /**
     * 获取个人信息
     */
    void fetchPersonInfo();
}
