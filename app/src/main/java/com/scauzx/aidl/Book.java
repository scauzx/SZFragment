package com.scauzx.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * @author scauzx
 * @date 2018/3/8
 */

public class Book implements Parcelable{

    public int bookId;
    public String bookName;

    protected Book(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
    }

    public void readFromParcel(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public Book() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(bookName);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Book{")
                .append("bookId = " + bookId + " ")
                .append("bookName = " + bookName)
                .append("}");
        return builder.toString();
    }
}
