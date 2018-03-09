package com.scauzx.task;

import android.support.annotation.NonNull;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author scauzx
 * @date 2018/3/7
 */

class NamedThreadFactory implements ThreadFactory {
    private final ThreadFactory mDefaultThreadFactory;
    private final int mPriority;
    private final String mBaseName;
    private AtomicInteger mCount = new AtomicInteger();

    public NamedThreadFactory(String baseName, int priority) {
        mDefaultThreadFactory = Executors.defaultThreadFactory();
        mPriority = priority;
        mBaseName = baseName;
    }

    @Override
    public Thread newThread(@NonNull Runnable r) {
        Thread thread = mDefaultThreadFactory.newThread(r);
        thread.setName(mBaseName + "-" + mCount.getAndIncrement()); //线程安全自增
        thread.setPriority(mPriority);
        return thread;
    }
}
