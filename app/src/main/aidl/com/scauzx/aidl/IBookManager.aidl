// IBookManager.aidl
package com.scauzx.aidl;

import com.scauzx.aidl.Book;
import com.scauzx.aidl.IOnNewBookArrivedListener;
// Declare any non-default types here with import statements

interface IBookManager {
     List<Book> getBookList();
     void addBook(in Book book);
     void registerListener(IOnNewBookArrivedListener listener);
     void unregisterListener(IOnNewBookArrivedListener listener);
}
