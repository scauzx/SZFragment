package com.scauzx.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import scauzx.com.myapplication.R;

/**
 *
 * @author scauzx
 * @date 2017/9/22
 * 总结:使用纹理来画圆加效果比裁剪图片简单多了`
 */

public class CircleImageView extends android.support.v7.widget.AppCompatImageView {
    /**
     * 圆的边缘线的颜色
     */
    private int mLineColor;

    /**
     * 圆的边缘线的大小
     */
    private float mlineSize;

    /**
     * 是否要画阴影
     */
    private boolean mNeedShader;

    /**
     * 是否要自适应
     */
    private boolean mAdjust;

    private  String TAG = CircleImageView.class.getSimpleName();
    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        mLineColor = a.getColor(R.styleable.CircleImageView_lineColor,0xFFFFFF);
        mlineSize = a.getDimension(R.styleable.CircleImageView_lineSize,3);
        mNeedShader = a.getBoolean(R.styleable.CircleImageView_needShader,false);
        mAdjust = a.getBoolean(R.styleable.CircleImageView_adjustSize,true);
        a.recycle();
        Log.d(TAG,"lineSize = " + mlineSize);

    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "CircleImageView onMeasure");
        if (mAdjust) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG,"widthMode = " + widthMode + " heightMode = " + heightMode + " height = " +height + " width = " + width);
        if ((widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST ) && (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST )) {
            Log.d(TAG," onMeasure EXACTLY AT_MOST");
            if (width > height) {
                setMeasuredDimension(height,height);
            } else {
                setMeasuredDimension(width,width);
            }
            this.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    /**
     * 剪切圆有2种方法,一种是裁剪Bitmap，将bitmap做为一个圆形的bitmap，另外一个是画圆，将bitmap做为纹理给填充到圆里面
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (mAdjust) {
            super.onDraw(canvas);
            return;
        }

        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getHeight() == 0 || getWidth() == 0) {
            return;
        }
        if (!(drawable instanceof BitmapDrawable)) {
            return;
        }
        Bitmap b = ((BitmapDrawable)drawable).getBitmap();
        if (b == null) {
            return;
        }

        //用纹理画图,直接画圆，纹理填充内容
      //  drawBitmapWithSharder(canvas,b);

        //裁剪图片再画上渐变圆
        drawBitmapWithCutBitmap(canvas,b);

      /*  Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        final Paint paint = new Paint();
        paint.setColor(lineColor);
       // canvas.drawCircle(getWidth()/2,getHeight()/2,getWidth()/2,paint);
        int w = getWidth();
        Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
      //  canvas.drawBitmap(roundBitmap, lineSize, lineSize, roundBitmap);
        //canvas.drawCircle(getWidth()/2,getHeight()/2,getWidth()/2,mixedRGB(roundBitmap));

       // Bitmap bitmap = Bitmap.createScaledBitmap(b, getWidth()/2, getWidth()/2, false);

        canvas.drawBitmap(roundBitmap,0 ,0 ,null);
        canvas.drawCircle(getWidth()/2,getWidth()/2,getWidth()/2,mixedRGB(null));*/
//        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//// 将位图渲染设置给paint
//        paint.setShader(bitmapShader);
  //      canvas.drawCircle(getWidth()/2, getWidth()/2, getWidth()/2, mixedRGB(bitmap));
    }

    private static final int[] mColors = new int[] {
            Color.TRANSPARENT, Color.TRANSPARENT, Color.WHITE
    };
    private static final float[] mPositions = new float[] {
            0, 0.6f, 1f
    };


    /**
     *  纹理填充背景画图
     * @param bitmap
     * @return
     */
    private void  drawBitmapWithSharder(Canvas canvas, Bitmap bitmap){
        Paint paint = new Paint();
        //图片太大或者太小都要进行缩放
        Bitmap b = Bitmap.createScaledBitmap(bitmap, getWidth(), getWidth(), false);
        BitmapShader bitmapShader = new BitmapShader(b, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        RadialGradient radialGradient = new RadialGradient(getWidth()/2, getWidth()/2, getWidth()/2,
                mColors, mPositions, Shader.TileMode.MIRROR);
        //添加渐变，实现模糊效果,创建组合渐变，由于直接按原样绘制就好，所以选择Mode.SRC_OVER
        ComposeShader composeShader = new ComposeShader(bitmapShader,radialGradient, PorterDuff.Mode.SRC_OVER);
        paint.setShader(composeShader);
        canvas.drawCircle(getWidth()/2,getWidth()/2,getWidth()/2,paint);
    }

    /**
     *   通过裁剪Bitmap得到一个圆形Bitmap放在画布上，然后在画布上再放一个渐变圆
     * @param  canvas
     * @param bitmap
     */
    private void drawBitmapWithCutBitmap(Canvas canvas, Bitmap bitmap){
        //画出头像
        Bitmap b = getCroppedBitmap(bitmap,getWidth());
        canvas.drawBitmap(b,0,0,null);

        //画出渐变圆
        if (mNeedShader) {
            Paint paint = new Paint();
            RadialGradient radialGradient = new RadialGradient(getWidth()/2, getWidth()/2, getWidth()/2,
                    mColors, mPositions, Shader.TileMode.MIRROR);
            paint.setShader(radialGradient);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            canvas.drawCircle(getWidth()/2,getWidth()/2,getWidth()/2,paint);
        }
    }


    /**
     * 初始Bitmap对象的缩放裁剪过程
     * @param bmp        初始Bitmap对象
     * @param radius    圆形图片直径大小
     * @return 返回一个圆形的缩放裁剪过后的Bitmap对象
     */
    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        //比较初始Bitmap宽高和给定的圆形直径，判断是否需要缩放裁剪Bitmap对象
        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        }
        else {
            sbmp = bmp;
        }
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        paint.setColor(Color.parseColor("#FF6600"));
        paint.setAntiAlias(true);//抗锯齿
        paint.setDither(true);//防抖动
  /*      paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0); //填满画布背景
        paint.setColor(Color.parseColor("#BAB399"));*/
        canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2 , sbmp.getWidth() / 2 , paint);
        //核心部分，设置两张图片的相交模式，在这里就是上面绘制的Circle和下面绘制的Bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, 0, 0, paint);
        //canvas.drawBitmap(sbmp,0,0,null);

        return output;
    }

}
