package com.scauzx.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CountDownLatch;


/**
 * <p>
 *  IBinderPool.Stub.asInterface(IBinder), onServiceConnected返回的Ibinder对象，如果是在同一进程内，会直接返回Ibinder对象本身，不是同一个进程则返回代理对象Proxy
 *  IBinder 的linkToDeath(DeathRecipient recipient, int flags) 方法监听连接是否连接着,DeathRecipient类中的binderDied方法则表现 此IBinder已断开连接，断开的可能性有很多种
 *  也可能是IBinder所在进程已死
 * </p>
 *
 * IBinder管理类，返回多个类型的IBinder,DeathRecipient实现进程间的相互监听
 * @author Administrator
 * @date 2018/3/9
 */

public class BinderPool {

    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;
    private static final String TAG = Contacts.TAG;
    private volatile static BinderPool sInstance;
    private IBinderPool mBinderPool;
    private Context mContext;

    /**
     * 使用说明 http://www.importnew.com/21889.html
     */
    private CountDownLatch mConnectBinderPoolCountDownLatch;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //拿到BinderPoolService的 IBinder对象,代理类，监听所在进程生命
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mServiceDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (mConnectBinderPoolCountDownLatch != null) {
                mConnectBinderPoolCountDownLatch.countDown();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient mServiceDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.i(TAG, "service process die");
            mBinderPool.asBinder().unlinkToDeath(mServiceDeathRecipient, 0);
            mBinderPool = null;
            if (mContext != null && mServiceConnection != null) {
                mContext.unbindService(mServiceConnection);
            }
        }
    };


    private void bindBinderPoolService() {
        if (mContext != null) {
            mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
            Intent intent = new Intent(mContext, BinderPoolService.class);
            mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            try {
                mConnectBinderPoolCountDownLatch.await(); //上面代码执行完才可以执行下面代码，要保证连接完成拿到binder之后才可以拿binder去执行binder的方法
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void reStartServiceProcess() {
        if (mBinderPool == null) {
            bindBinderPoolService();
        }
    }

    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        try {
            if (mBinderPool != null) {
                binder = mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }

    public void setBinder(IBinder binder) {
        if (mBinderPool != null) {
            try {
                mBinderPool.setBinder(binder);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 结束后台进程
     */
    public void finishServiceProcess() {
        if (mBinderPool != null) {
            try {
                mBinderPool.finishServicesProcess();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 初始化,绑定Services
     * @param context
     */
    private BinderPool(Context context) {
        mContext = context.getApplicationContext();
        bindBinderPoolService();
    }


    /**
     * double check , sInstance使用volatile 关键字，double check 使用volatile说明 https://www.jianshu.com/p/3cc31c265a1b
     * @param context
     * @return
     */
    public static BinderPool getInstance(Context context) {
        if (sInstance == null) {
            synchronized (BinderPool.class) {
                if (sInstance == null) {
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }


}
