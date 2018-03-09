// IResultListener.aidl
package com.scauzx.aidl;

// Declare any non-default types here with import statements

interface IResultListener {
    void onSunncess(int count);
    void onFailed(int onResCode);
}
