package com.scauzx.holders;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scauzx.adapters.EntranceAdapter;
import com.scauzx.models.BannerInfo;

import java.util.ArrayList;
import java.util.List;

import scauzx.com.myapplication.databinding.ViewEntranceBinding;

/**
 * Created by Administrator on 2017/12/9.
 */

public class EntranceViewHolder  extends RecyclerView.ViewHolder {
    public ViewEntranceBinding mBinding;
    RecyclerView mRecyclerView;
    EntranceAdapter mAdapter;
    List<BannerInfo> mInfos = new ArrayList<>();
    public EntranceViewHolder(ViewEntranceBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
        mRecyclerView = binding.entranceRecycler;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new EntranceAdapter(mInfos);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void bindData(List<BannerInfo> bannerInfos) {
        mInfos.clear();
        mInfos.addAll(bannerInfos);
        mAdapter.notifyDataSetChanged();

    }

}
