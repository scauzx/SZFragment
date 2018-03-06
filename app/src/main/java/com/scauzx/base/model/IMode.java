package com.scauzx.base.model;

/**
 *
 * @author scauzx
 * @date 2017/11/15
 */

public interface IMode {

    /**
     * Model结束时释放资源等,DataBinding会使用到Model
     */
    void destroy();
}
