// IBinderPool.aidl
package com.scauzx.binderpool;

// Declare any non-default types here with import statements

interface IBinderPool {
    void setBinder(IBinder binder);
    IBinder queryBinder(int code);
    void finishServicesProcess(); //结束自身进程
}
