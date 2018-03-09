package com.scauzx.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import com.scauzx.aidl.BookMangerActivity;
import com.scauzx.task.AppExecutors;
import com.scauzx.task.TaskType;

/**
 *
 * @author scauzx
 * @date 2018/3/9
 */

public class BinderPoolService extends Service {
    private static final String TAG = Contacts.TAG;
    private IBinder mBinderPool = new BinderPoolImp();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPool;
    }

    /**
     * 详细可参考 http://blog.csdn.net/free555/article/details/14754323
     */
    class BinderPoolImp extends IBinderPool.Stub {

        private IBinder mClientBinder;
        private DeathRecipient mClientDeathRecipient = new DeathRecipient() {
            @Override
            public void binderDied() {
                Log.i(TAG, "client process die");
                mClientBinder.unlinkToDeath(mClientDeathRecipient, 0);
                mClientBinder = null;
                AppExecutors.get().executeDelay(TaskType.BACKGROUND, new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "AppExecutors start BookMangerActivity");
                        Intent intent = new Intent(BinderPoolService.this, BookMangerActivity.class);
                        startActivity(intent);
                    }
                }, 5000);
            }
        };


        @Override
        public void setBinder(IBinder binder) throws RemoteException {
            Log.i(TAG, "setBinder");
            if (mClientBinder == null) {
                mClientBinder = binder;
                mClientBinder.linkToDeath(mClientDeathRecipient, 0);
            }
        }

        @Override
        public IBinder queryBinder(int code) throws RemoteException {
            IBinder binder = null;
            switch (code) {
                case BinderPool.BINDER_SECURITY_CENTER: {
                    binder = new SecurityCenterImp();
                    break;
                }

                case BinderPool.BINDER_COMPUTE: {
                    binder = new ComputeImp();
                    break;
                }

                default:
                    break;
            }

            return binder;
        }

        @Override
        public void finishServicesProcess() throws RemoteException {
            //结束自身进程
            Log.i(Contacts.TAG, "finishServicesProcess process " + android.os.Process.myPid());
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


}
