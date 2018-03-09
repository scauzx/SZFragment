// IBookManager.aidl
package com.scauzx.aidl;

import com.scauzx.aidl.Book;
import com.scauzx.aidl.IOnNewBookArrivedListener;
import com.scauzx.aidl.IResultListener;
// Declare any non-default types here with import statements

interface IBookManager {
     List<Book> getBookList();
     void addBook(in Book book);
     void registerListener(IOnNewBookArrivedListener listener);
     void unregisterListener(IOnNewBookArrivedListener listener);
     void addBookIn(in Book book);
     void addBookOut(out Book book);
     void addBookInOut(inout Book book);
     oneway void testWithOneWay(int value, String message, in IResultListener result);
     void testWithoutOneWay(int value, String message, in IResultListener result);
     }

