package com.scauzx.binderpool;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import com.scauzx.activities.BaseActivity;
import com.scauzx.task.AppExecutors;
import com.scauzx.task.TaskType;
import scauzx.com.myapplication.R;

/**
 *
 * @author Administrator
 * @date 2018/3/9
 */

public class BinderPoolActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = Contacts.TAG;
    IBinder mBinder = new Binder();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binderpool);
        Log.i(TAG, "BinderPoolActivity onCreate");
        setUpView();
    }

    private void setUpView() {
        findViewById(R.id.work).setOnClickListener(this);
        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.restart).setOnClickListener(this);
        findViewById(R.id.finishMainProcess).setOnClickListener(this);
    }

    public void doWork() {
        BinderPool binderPool = BinderPool.getInstance(getApplicationContext());
        binderPool.setBinder(mBinder);
        IBinder binder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        ICompute computeImp = ComputeImp.asInterface(binder);
        try {
            Log.i(TAG, "ComputeImp compute result = " + computeImp.add(1, 2));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //拿到代理类，执行代理操作
        ISecurityCenter securityCenter = SecurityCenterImp.asInterface(binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER));

        try {
            String encrypt = securityCenter.encrypt("hello");
            Log.i(TAG, "securityCenter encrypt hello = " + encrypt);
            Log.i(TAG, "securityCenter decrypt encrypt = " + securityCenter.decrypt(encrypt));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void work() {
        AppExecutors.get().execute(TaskType.BACKGROUND, new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.work:
                work();
                break;

            case R.id.finish:
                finishServiceProcess();
                break;

            case R.id.restart:
                restartServiceProcess();
                break;

            case R.id.finishMainProcess:
                finishMainProcess();
                break;

        }
    }

    private void finishMainProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void restartServiceProcess() {
        AppExecutors.get().execute(TaskType.BACKGROUND, new Runnable() {
            @Override
            public void run() {
                BinderPool.getInstance(BinderPoolActivity.this).reStartServiceProcess();
                BinderPool.getInstance(BinderPoolActivity.this).setBinder(mBinder);
            }
        });
    }

    private void finishServiceProcess() {
        AppExecutors.get().execute(TaskType.BACKGROUND, new Runnable() {
            @Override
            public void run() {
                BinderPool.getInstance(getApplicationContext()).finishServiceProcess();
            }
        });
    }
}
