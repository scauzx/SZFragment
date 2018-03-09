package com.scauzx.task;

import java.util.IllegalFormatCodePointException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import rx.Scheduler;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * App 任务调度类
 * execute和executeDelay拿到的Subscription要记得在适当的时机执行unSubscribe
 * @author scauzx
 * @date 2018/3/7
 */

public class AppExecutors {
    public static AppExecutors sInstance;

    private ExecutorService mBackGroundServices;
    private ExecutorService mIOServices;
    private ExecutorService mNetWorkServices;

    /**
     * 定义不同线程池内线程执行的优先级
     */
    private final int THREAD_BACKGROUND_PRIORITY = Thread.NORM_PRIORITY - 2;
    private final int THREAD_IO_PRIORITY = Thread.NORM_PRIORITY;
    private final int THREAD_NETWORK_PRIORITY = Thread.NORM_PRIORITY + 2;


    public static AppExecutors get() {
        if (sInstance == null) {
            synchronized (AppExecutors.class) {
                if (sInstance == null) {
                    sInstance = new AppExecutors();
                }
            }
        }
        return sInstance;
    }


    /**
     * 执行一个runnable
     * @param taskType 执行runnable的线程类型
     * @param runnable 执行任务
     * @return
     */
    public Subscription execute(TaskType taskType, final Runnable runnable) {
        return execute(taskType, new Callable<Object>() {

            @Override
            public Void call() throws Exception {
                if (runnable != null) {
                    runnable.run();
                }
                return null;
            }
        }, null, null);
    }


    public <T> Subscription execute(TaskType taskType, final  Runnable runnable, final Consumer<T> UICallback) {
        return execute(taskType, new Callable<T>() {
            @Override
            public T call() throws Exception {
                if (runnable != null) {
                    runnable.run();
                }
                return null;
            }
        }, UICallback,  null);
    }


    /**
     * 运行一个任务
     * @param taskType 任务运行线程类型
     * @param task 任务
     * @param UICallback 任务成功回调
     * @param errorHandler 任务失败回调
     * @param <T>
     * @return
     */
    public <T> Subscription execute(TaskType taskType, final Callable<T> task, final Consumer<T> UICallback, final Consumer<Throwable> errorHandler) {
        Scheduler scheduler = null;
        switch (taskType) {
            case BACKGROUND:
                if (mBackGroundServices == null) {
                    ensureBackGroundExecuteCreated();
                }
                scheduler = Schedulers.from(mBackGroundServices);
                break;

            case IO:
                if (mIOServices == null) {
                    ensureIOExecuteCreated();
                }
                scheduler = Schedulers.from(mIOServices);
                break;

            case NETWORK:
                if (mNetWorkServices == null) {
                    ensureNewWorkExecuteCreated();
                }
                scheduler = Schedulers.from(mNetWorkServices);
                break;

            case WORK:
                scheduler = Schedulers.newThread();
                break;

            default:
                throw new IllegalArgumentException("task type is not supported!!!");

        }
        return Single.fromCallable(task).subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<T>() {
            @Override
            public void call(T t) {
                if (UICallback != null) {
                    UICallback.accept(t);
                }
            }
        }, new Action1<Throwable>() {

            @Override
            public void call(Throwable throwable) {
                if (errorHandler != null) {
                    errorHandler.accept(throwable);
                }
            }
        });
    }


    /**
     * 延迟执行一个runnable
     * @param taskType
     * @param delay
     * @param runnable
     * @return
     */
    public Subscription executeDelay(TaskType taskType, final Runnable runnable, long delay) {
        return executeDelay(taskType, delay, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (runnable != null) {
                    runnable.run();
                }
                return null;
            }
        }, null, null);
    }


    /**
     * 延迟执行一个任务
     * @param taskType 任务运行线程类型
     * @param delay 延迟时间
     * @param task 任务
     * @param UICallback 任务执行回调
     * @param errorHandler 任务失败回调
     * @param <T>
     * @return
     */
    public <T> Subscription executeDelay(TaskType taskType, final long delay, final Callable<T> task, final Consumer<T> UICallback, final Consumer<Throwable> errorHandler) {
        Scheduler scheduler = null;
        switch (taskType) {
            case BACKGROUND:
                if (mBackGroundServices == null) {
                    ensureBackGroundExecuteCreated();
                }
                scheduler = Schedulers.from(mBackGroundServices);
                break;

            case IO:
                if (mIOServices == null) {
                    ensureIOExecuteCreated();
                }
                scheduler = Schedulers.from(mIOServices);
                break;

            case NETWORK:
                if (mNetWorkServices == null) {
                    ensureNewWorkExecuteCreated();
                }
                scheduler = Schedulers.from(mNetWorkServices);
                break;

            case WORK:
                scheduler = Schedulers.newThread();
                break;

            default:
                break;

        }
        return  Single.just(0)
                .delay(delay, TimeUnit.MILLISECONDS)
                .map(new Func1<Integer, T>() {
                    @Override
                    public T call(Integer integer) {
                        try {
                            return task.call();
                        } catch (Exception e) {
                            throw Exceptions.propagate(e);
                        }
                    }
                }).subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<T>() {
                    @Override
                    public void call(T t) {
                        if (UICallback != null) {
                            UICallback.accept(t);
                        }
                    }
                }, new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {
                        if (errorHandler != null) {
                            errorHandler.accept(throwable);
                        }
                    }
                });

    }


    private synchronized void ensureBackGroundExecuteCreated() {
        if (mBackGroundServices == null) {
            mBackGroundServices = Executors.newFixedThreadPool(2, new NamedThreadFactory("background_global_thread", THREAD_BACKGROUND_PRIORITY));
        }
    }

    private synchronized void ensureIOExecuteCreated() {
        if (mIOServices == null) {
            mIOServices = Executors.newFixedThreadPool(2, new NamedThreadFactory("io_global_thread", THREAD_IO_PRIORITY));
        }
    }

    private synchronized void ensureNewWorkExecuteCreated() {
        if (mNetWorkServices == null) {
            mNetWorkServices = Executors.newFixedThreadPool(2, new NamedThreadFactory("network_global_thread", THREAD_NETWORK_PRIORITY));
        }
    }

}
