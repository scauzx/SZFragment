// IOnNewBookArrivedListener.aidl
package com.scauzx.aidl;

import com.scauzx.aidl.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}