package com.scauzx.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import scauzx.com.myapplication.R;

/**
 *
 * @author scauzx
 * @date 2017/11/15
 */

public class FourthFragment extends BaseFragment {
    private String TAG = FourthFragment.class.getSimpleName();

    public static FourthFragment getInstance() {
        return new FourthFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_fourth,null);
    }

}
