package com.scauzx.holders;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import com.scauzx.models.BannerInfo;
import com.scauzx.widget.BannerPageView;

import java.util.List;
import scauzx.com.myapplication.databinding.LayoutBannerHeaderBinding;

/**
 *
 * @author Administrator
 * @date 2017/12/8
 */

public class BannerViewHolder extends RecyclerView.ViewHolder{
    public LayoutBannerHeaderBinding mBinding;
    private Context mContext;
    BannerPageView viewPager;
    public BannerViewHolder(LayoutBannerHeaderBinding binding) {
        super(binding.getRoot());
        mContext = binding.getRoot().getContext();
        mBinding = binding;
        viewPager = binding.bannerView;
    }

    public void bindData(List<BannerInfo> list) {
        viewPager.bindData(list);
    }

}
