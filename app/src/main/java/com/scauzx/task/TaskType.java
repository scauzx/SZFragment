package com.scauzx.task;

/**
 *
 * @author scauzx
 * @date 2018/3/7
 */

public enum TaskType {

    /**
     * 后台任务
     * 优先级较低(NORMAL-2)
     * 执行后台常规任务
     */
    BACKGROUND,


    /**
     * IO任务
     * 优先级正常(NORMAL)
     * 执行IO任务
     */
    IO,

    /**
     * 网络任务
     * 优先级较高(NORMAL + 2)
     * 执行网络任务
     */
    NETWORK,

    /***
     * 独立任务
     * 优先级正常
     * 每次开启一个单独线程
     * 主要用于独立并且不希望受其他任务影响的任务,一般的任务尽量使用后台先吃吃
     */
    WORK;

}
