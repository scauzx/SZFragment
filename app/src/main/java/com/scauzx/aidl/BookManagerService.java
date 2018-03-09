package com.scauzx.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import com.scauzx.task.AppExecutors;
import com.scauzx.task.TaskType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * RemoteCallBackList：是系统专门提供的用于删除跨进程listener的接口。它的工作原理其实很简单：
 * 在它的内部有一个Map结构专门用来保存所有AIDL回调ArrayMap<IBinder, Callback> mCallback = new ArrayMap<IBinder, Callback>();,
 * 当客户端注册listener时，会把listener的信息注册到mCallBack中,其中key和value通过下面方式获得：
 * IBinder key = listener.asBinder();Callback value = new Callback(listener, cookie)。
 * 另外一点我们需要知道：对象是不能跨进程传输的，对象的跨进程传输过程实际是反序列化的过程，
 * 这是我们Book类为什么要实现Parcelable接口的原因。在跨进程传输中，Binder会把客户端传递的对象重新转化并生成另一对象，
 * 当我们注册和解注册的过程中使用的是同一个客户端对象，但是通过Binder传递到服务端却生成了两个不同的对象。
 * 而RemoteCallBackList就是用来解决这个问题的，虽然所多次跨进程传输客户端的同一个对象会在服务端生成不同的对象，
 * 但在这些新生成的对象都有一个共同点，那就是他们底层的Binder对象是同一个，利用这个，就可以实现上面无法实现的功能。
 * 当客户端解注册时，我们只要遍历所有的listener，找出那个和解注册listener具有相同Binder对象服务器listener并把他删除掉即可，
 * 这就是RemoteCallbackList为我们做的事情。
 * copy from https://github.com/singwhatiwanna/android-art-res/blob/master/Chapter_2/src/com/ryg/chapter_2/aidl/BookManagerService.java
 * @author scauzx
 */

public class BookManagerService extends Service {

    private static final String TAG = Contacts.TAG;
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    private List<Book> mBookList = new ArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList =  new RemoteCallbackList<>();

    private IBinder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            if (book != null) {
                mBookList.add(book);
            }
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
                mListenerList.register(listener);

                final int N = mListenerList.beginBroadcast();
                mListenerList.finishBroadcast();
                Log.d(TAG, "registerListener, current size:" + N);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            boolean success = mListenerList.unregister(listener);
            if (success) {
                Log.d(TAG, "unregister success.");
            } else {
                Log.d(TAG, "not found, can not unregister.");
            }
            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.d(TAG, "unregisterListener, current size:" + N);
            if (listener != null) {
                mListenerList.unregister(listener);
            }
        }
    };

    public BookManagerService() {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        mIsServiceDestoryed.set(false);
        AppExecutors.get().execute(TaskType.WORK, mRunnable);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestoryed.set(true);
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                onNewBookArrived(newBook);
            }
        }
    };

    /**
     * 回调新书增加
     * @param book
     */
    public void onNewBookArrived(Book book) {
        if (book == null) {
            return;
        }
        mBookList.add(book);
        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener l = mListenerList.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
