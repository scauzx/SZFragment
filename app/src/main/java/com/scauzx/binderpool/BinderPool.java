package com.scauzx.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;



/**
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

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service); //拿到BinderPoolService的 IBinder对象，监听所在进程生命
            try {
                mBinderPool.asBinder().linkToDeath(mServiceDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
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
            Intent intent = new Intent(mContext, BinderPoolService.class);
            mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
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
