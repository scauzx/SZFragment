package com.scauzx.task;

import android.os.Handler;
import android.os.Looper;

/**
 * 主线程任务处理
 * @author scauzx
 * @date 2018/3/7
 */

public class UIThreadUtils {

    /**
     * 主线程中执行任务
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        if (isMainThread()) {
            runnable.run();
        } else {
            LazyHolder.sMainThreadHandler.post(runnable);
        }
    }


    /**
     * 主线程中执行runnable,将Runnable移至最前任务
     * @param runnable
     */
    public static void runOnUiThreadFront(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        LazyHolder.sMainThreadHandler.postAtFrontOfQueue(runnable);
    }

    /**
     * 主线程中延时执行任务
     * @param runnable
     * @param delay
     */
    public static void runOnUiThread(Runnable runnable, long delay) {
        if (runnable == null) {
            return;
        }
        LazyHolder.sMainThreadHandler.postDelayed(runnable, delay);
    }


    /**
     * 移除主线程中任务
     * @param runnable
     */
    public static void removeCallBacks(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        LazyHolder.sMainThreadHandler.removeCallbacks(runnable);
    }

    /**
     * 当前是否在主线程中
     * @return
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    public static class LazyHolder{
        public static Handler sMainThreadHandler = new Handler(Looper.getMainLooper());
    }

}
