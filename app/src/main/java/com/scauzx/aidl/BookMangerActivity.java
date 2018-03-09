package com.scauzx.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.scauzx.activities.BaseActivity;
import com.scauzx.task.AppExecutors;
import com.scauzx.task.Consumer;
import com.scauzx.task.TaskType;
import java.util.List;
import java.util.concurrent.Callable;
import scauzx.com.myapplication.R;

/**
 *
 * @author scauzx
 * @date 2018/3/8
 */

public class BookMangerActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = Contacts.TAG;
    private IBookManager mRemoteBookManager;
    private IOnNewBookArrivedListener.Stub mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Log.d(TAG, "receive new book :" + newBook.toString());
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        setupView();
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setupView() {
        findViewById(R.id.btn_get).setOnClickListener(this);
    }


    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "mServiceConnection onServiceConnected");
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            mRemoteBookManager = bookManager;
            List<Book> list = null;
            try {
                list = bookManager.getBookList();
                Log.i(TAG, "query book list, list type:"
                        + list.getClass().getCanonicalName());
                Log.i(TAG, "query book list:" + list.toString());
                Book newBook = new Book(3, "Android进阶");
                bookManager.addBook(newBook);
                Log.i(TAG, "add book:" + newBook);
                List<Book> newList = bookManager.getBookList();
                Log.i(TAG, "query book list:" + newList.toString());
                bookManager.registerListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                Log.i(TAG, "mServiceConnection onServiceConnected RemoteException");
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (mRemoteBookManager != null) {
                try {
                    mRemoteBookManager.unregisterListener(mOnNewBookArrivedListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (mRemoteBookManager != null
                && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                Log.i(TAG, "unregister listener:" + mOnNewBookArrivedListener);
                mRemoteBookManager
                        .unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get:
                doGet();
                break;
        }
    }

    private void doGet() {
        Log.i(TAG, "doGet");
        AppExecutors.get().execute(TaskType.NETWORK, new Callable<List<Book>>() {
            @Override
            public List<Book> call() {
                Log.i(TAG, "AppExecutors  call");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    return mRemoteBookManager.getBookList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, new Consumer<List<Book>>() {
            @Override
            public void accept(List<Book> books) {
                Toast.makeText(BookMangerActivity.this, "get it books.length = " + books.size(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "AppExecutors  mRemoteBookManager books" + books.toString());
            }
        }, null);
    }
}
