package com.scauzx.widget;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2017/12/8.
 */

public class SZImageView extends SimpleDraweeView {
    OnLoadBitmapSucListener mListener;
    public SZImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public SZImageView(Context context) {
        super(context);
    }

    public SZImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    ControllerListener mControllerListener = new ControllerListener() {
        @Override
        public void onSubmit(String id, Object callerContext) {

        }

        @Override
        public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {

        }

        @Override
        public void onIntermediateImageSet(String id, Object imageInfo) {
            if (mListener!=null) {
                mListener.onLoadBitmapSuc(SZImageView.this);
            }
        }

        @Override
        public void onIntermediateImageFailed(String id, Throwable throwable) {

        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            if (mListener!=null) {
                mListener.onLoadBitmapFail(SZImageView.this);
            }
        }

        @Override
        public void onRelease(String id) {

        }
    };



    public void setImageUrl(Uri uri, @Nullable Object callerContext) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setCallerContext(callerContext)
                .setUri(uri)
                .setOldController(getController())
                .setControllerListener(mControllerListener)
                .build();
        setController(controller);
    }


    public void setImageUrl(String url, OnLoadBitmapSucListener listener) {
        mListener = listener;
        if (TextUtils.isEmpty(url)) {
            setImageURI((String)null);
        } else {
            Uri uri = Uri.parse(url);
            setImageUrl(uri, null);
        }
    }


    public interface OnLoadBitmapSucListener {
        void onLoadBitmapSuc(SZImageView view);
        void onLoadBitmapFail(SZImageView view);
    }
}
