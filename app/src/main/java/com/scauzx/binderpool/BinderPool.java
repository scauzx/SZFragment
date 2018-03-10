package com.scauzx.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.concurrent.CountDownLatch;


/**
 * <p>
 *  IBinderPool.Stub.asInterface(IBinder), onServiceConnected返回的Ibinder对象，如果是在同一进程内，会直接返回Ibinder对象本身，不是同一个进程则返回代理对象Proxy
 *  IBinder 的linkToDeath(DeathRecipient recipient, int flags) 方法监听连接是否连接着,DeathRecipient类中的binderDied方法则表现 此IBinder已断开连接，断开的可能性有很多种
 *  也可能是IBinder所在进程已死
 *</p>
 *
 * <p> binder的使用
 *  Activity在bind Services之后，会拿到Servcies
 *  不跨进程 onBind方法:
 *  public class MyBinder extends Binder {
 *      MyService getService() {
 *       return MyService.this;
 *   }
 *   onBind方法返回MyBinder实例， onServiceConnected连接拿到IBinder，通过getService拿到Service对象本身,然后执行Service对象本身共有的方法
 *   因为Activity和Service本身是在一个进程里面，所以拿到Service对象直接调用对象本身的方法
 *
 *  跨进程 onBind方法
 *  因为Activity和Service本身不是在一个进程里面，所以无法拿到Service对象直接调用对象本身的方法,而是通过拿到一个IBinder对象，通过IBinder对象对Service执行
 *  首先,Service方法的onBind方法要返回一个实现aidl的IBinder类对象,比如SecurityCenterImp，里面实现了远程调用的方法，注意。比如ISecurityCenter只是一个接口，如果Service返回的额是ISecurityCenter的实现类的话，
 *  那么其实就是实现了这个接口的方法而已，那单纯的实现接口对跨进程没有任何用处，就算Activity拿到了这个接口实例，实现起来还是在同个进程里面，aidl文件会为生成的ISecurityCenter接口编写一个Stub类，这是一个
 *  抽象类，它里面有对跨进程的处理，所以Service中返回的IBinder对象要是ISecurityCenter.Stub的子类的实例
 *
 *  然后onServiceConnected连接拿到ISecurityCenter.Stub的实例IBinder，然后要将IBinder转化为SecurityCenterImp,怎么转化呢？
 *  就是调用SecurityCenterImp.Stub.asInterface()方法，因为这个IBinder是aidl生成的Stab类对象，它是继承了Binder，实现了ISecurityCenter接口，
 *  asInterface方法里面调用Binder里面的{@link Binder#queryLocalInterface(String descriptor)}方法，查询Service进程和Activity进程是不是在同一个进程里面，如果是同一个进程，那么就返回Stub它自己本身,
 *  那么Activity中调用SecurityCenterImp中的encrypt方法，也仅仅只是调用了encrypt方法，并没有做什么额外的处理，也就是像普通的接口调用，还是在一个进程内，这很合理
 *
 *  那么如果发现不是在同一个进程里面呢，那就要做跨进程处理,不在一个进程里面，那么就就返回了实现了抽象类ISecurityCenter接口的静态类ISecurityCenter.Stub.Proxy,他里面页实现了ISecurityCenter的方法，
 *  那么如果在Activity调用SecurityCenterImp中的encrypt方法,其实是调到了ISecurityCenter.Stub.Proxy中的encrypt方法,但是其实我们是想调到ISecurityCenter.Stub里面的encrypt方法,因为这才是被Service里面实现了的方法，
 *  是我们真正的自己写的处理代码，但是跨进程让我们无法直接调到ISecurityCenter.Stub里面的encrypt方法,所以要通过ISecurityCenter.Stub.Proxy中的encrypt方法去调到ISecurityCenter.Stub里面的encrypt方法,
 *  我们看看Proxy的构造函数
 *  private android.os.IBinder mRemote;
 *  Proxy(android.os.IBinder remote) {
 *  mRemote = remote;
 * }
 * 那么Proxy持有Stub的引用,这也是当然的，不然怎么在encrypt方法里面调到Stub里面的encrypt方法
 * @Override public java.lang.String encrypt(java.lang.String content) throws android.os.RemoteException
 *   {
 *   android.os.Parcel _data = android.os.Parcel.obtain();
 *   android.os.Parcel _reply = android.os.Parcel.obtain();
 *   java.lang.String _result;
 *   try {
 *   _data.writeInterfaceToken(DESCRIPTOR);
 *   _data.writeString(content);
 *   mRemote.transact(Stub.TRANSACTION_encrypt, _data, _reply, 0);
 *   _reply.readException();
 *  _result = _reply.readString();
 *  }
 *   finally {
 *   _reply.recycle();
 *   _data.recycle();
 *   }
 * return _result;
 * }
 * mRemote 本身是一个Binder类，调用它的 transact方法，_data 代表传入数据，_reply代表返回数据
 * transact方法向远方Binder对象发出调用,Binder.onTransact()方法调用完成后才返回
 * 通过transact()发送的数据是Parcel，Parcel是一种一般的缓冲区，除了有数据外还带有一些描述它内容的元数据
 *  read http://blog.csdn.net/sergeycao/article/details/52585411
 * </p>
 *
 *
 * <p>IBinder管理类，返回多个类型的IBinder,DeathRecipient实现进程间的相互监听
 * </p>
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


    /**
     * 绑定Service，在绑定完成之前，阻塞线程执行
     */
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
