package com.scauzx.presenter;

/**
 *
 * @author scauzx
 * @date 2017/11/15
 */

public interface IBasePresenter {

    /**
     * resume时处理的方法
     */
    void onResume();

    /**
     * 销毁时处理的方法
     */
    void onDestroy();

}
