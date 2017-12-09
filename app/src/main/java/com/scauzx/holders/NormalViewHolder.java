package com.scauzx.holders;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.scauzx.models.BannerInfo;
import com.scauzx.widget.SZImageView;


/**
 *
 * @author Administrator
 * @date 2017/12/9
 */

public class NormalViewHolder extends RecyclerView.ViewHolder {
    SZImageView imageView;
    public NormalViewHolder(SZImageView itemView) {
        super(itemView);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1000);
        itemView.setLayoutParams(layoutParams);
        imageView = itemView;
    }

    public void bindData(BannerInfo bannerInfo) {
        imageView.setImageURI(bannerInfo.downloadUrl);
    }

}
