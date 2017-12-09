package com.scauzx.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scauzx.models.BannerInfo;
import com.scauzx.widget.SZImageView;

import java.util.List;

import scauzx.com.myapplication.R;
import scauzx.com.myapplication.databinding.ItemEntranceBinding;

/**
 *
 * @author Administrator
 * @date 2017/12/9
 */

public class EntranceAdapter extends RecyclerView.Adapter {

    public List<BannerInfo> mData;

    public EntranceAdapter(List<BannerInfo> infos) {
        mData = infos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemEntranceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_entrance, parent, false);
        return new EntranceItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EntranceItemHolder) {
            ((EntranceItemHolder) holder).bindData(mData.get(position));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class EntranceItemHolder extends RecyclerView.ViewHolder{
        ItemEntranceBinding mBinding;
        SZImageView mView;
        public BannerInfo mInfo;
        public EntranceItemHolder(ItemEntranceBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mView = mBinding.itemIv;
        }

        public void bindData(BannerInfo info) {
            mInfo = info;
            mView.setImageURI(mInfo.downloadUrl);
        }
    }

}
