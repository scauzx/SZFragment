package com.scauzx.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.scauzx.holders.BannerViewHolder;
import com.scauzx.holders.EntranceViewHolder;
import com.scauzx.holders.NormalViewHolder;
import com.scauzx.models.BannerInfo;
import com.scauzx.widget.SZImageView;

import java.util.ArrayList;
import java.util.List;

import scauzx.com.myapplication.BR;
import scauzx.com.myapplication.R;
import scauzx.com.myapplication.databinding.LayoutBannerHeaderBinding;
import scauzx.com.myapplication.databinding.ViewEntranceBinding;

/**
 *
 * @author Administrator
 * @date 2017/12/9
 */

public class SecondImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_HEADER_BANNER = 0;
    private static final int TYPE_SPECIAL_OTHER = 1;
    private static final int TYPE_LIST = 2;
    private List<BannerInfo> mDatas;
    private List<BannerInfo> mBannerInfos;

    public SecondImageListAdapter(List<BannerInfo> info) {
        this.mDatas = info;
    }

    public void setBannerInfos(List<BannerInfo> bannerInfos) {
        this.mBannerInfos = bannerInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER_BANNER;

            case 1:
                return TYPE_SPECIAL_OTHER;

            default:
                return TYPE_LIST;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_HEADER_BANNER:
                LayoutBannerHeaderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_banner_header, parent, false);
                viewHolder = new BannerViewHolder(binding);
                break;
            case TYPE_SPECIAL_OTHER:
                ViewEntranceBinding viewEntranceBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_entrance, parent, false);
                viewHolder = new EntranceViewHolder(viewEntranceBinding);
                break;
            case TYPE_LIST:
                viewHolder = new NormalViewHolder(new SZImageView(parent.getContext()));
                break;

            default:
                viewHolder = new NormalViewHolder(new SZImageView(parent.getContext()));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerViewHolder) {
            if (!mBannerInfos.isEmpty()) {
                StaggeredGridLayoutManager.LayoutParams clp = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                if (clp.isFullSpan()) {
                    return;
                }
                clp.setFullSpan(true);
                ((BannerViewHolder) holder).mBinding.getRoot().setLayoutParams(clp);
                ((BannerViewHolder) holder).bindData(mBannerInfos);
            }
        } else if (holder instanceof EntranceViewHolder) {
            StaggeredGridLayoutManager.LayoutParams clp = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            if (clp.isFullSpan()) {
                return;
            }
            clp.setFullSpan(true);
            ((EntranceViewHolder) holder).mBinding.getRoot().setLayoutParams(clp);
            List<BannerInfo> list = new ArrayList<BannerInfo>(mBannerInfos);
            list.addAll(mBannerInfos);
            list.addAll(mBannerInfos);
            ((EntranceViewHolder) holder).bindData(list);
        } else if (holder instanceof NormalViewHolder) {
            ((NormalViewHolder) holder).bindData(mDatas.get(position - 2));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDatas.size() + 2;
    }
}
