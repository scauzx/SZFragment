package com.scauzx.task;

/**
 * Created by Administrator on 2018/3/7.
 */

/**
 * A functional interface (callback) that accepts a single value.
 *
 * @param <T> the value type
 */
public interface Consumer<T> {
    /**
     * Consume the given value.
     *
     * @param t the value
     * @throws Exception on error
     */
    void accept(T t);
}
