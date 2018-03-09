package com.scauzx.binderpool;

import android.os.RemoteException;

/**
 *
 * @author Administrator
 * @date 2018/3/9
 */

public class ComputeImp extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
