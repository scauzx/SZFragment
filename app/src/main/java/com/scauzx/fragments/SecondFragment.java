package com.scauzx.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;

import scauzx.com.myapplication.R;

/**
 *
 * @author scauzx
 * @date 2017/11/15
 */

public class SecondFragment extends BaseFragment {
    private String TAG = "SecondFragment";

    public static SecondFragment getInstance() {
        return new SecondFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_second,null);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG,"SecondFragment setUserVisibleHint = " + isVisibleToUser);
    }

}
