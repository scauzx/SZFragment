package com.scauzx.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.StyleableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import com.scauzx.utils.ListUtils;
import com.scauzx.utils.OsUtil;
import java.util.ArrayList;
import java.util.List;
import scauzx.com.myapplication.R;

/**
 * RoundCornerTextView
 * 自定义圆角TextView,可调节圆角大小，TextView各种状态下的按钮颜色背景,支持渐变，暂时支持线性渐变,要支持复杂的各种渐变效果的话，直接调用 {@link #setBackgroundDrawable(Drawable background)}方法，
 * background使用自己需要的复杂的效果的组合效果StateListDrawable对象，或者自己需要的图片背景
 * xml中设置 tvRadius修改圆角半径，默认为20dp
 * 设置tvNormalColor/tvPressColor/tvDisEnableColor修改按钮常态颜色/按钮按住时颜色/按钮不可用时颜色
 * 设置tvNormalGradientColors/tvPressGradientColors/tvDisEnableGradientColors修改按钮常态渐变颜色/按钮按住状态渐变颜色/按钮不可用时渐变颜色，
 * 设置了tvNormalColor/tvPressColor/tvDisEnableColor之后再设置对应的tvNormalGradientColors/tvPressGradientColors/tvDisEnableGradientColors时，只会有设置tvNormalColor/tvPressColor/tvDisEnableColor的效果，不会渐变
 * tvNormalGradientColors/tvPressGradientColors/tvUnEnableGradientColors的值为字符串，值类似于"#111111|#222222|#333333",xml中既设置了TextView自带的background属性后就不要设置tvPressColor等属性，不然导致xml中background属性失效
 * tvNormalGradientAgent/tvPressGradientAgent/tvDisEnableGradientAgent设置常态/按下/不可用时状态渐变方向
 * @author zhangzexian
 * @date 2018/3/1
 */

public class RoundCornerTextView extends android.support.v7.widget.AppCompatTextView {

    private final String TAG = RoundCornerTextView.class.getSimpleName();
    private int INVALID_COLOR = -1;
    private int INVALID_AGENT = -1;
    private int mRadius = OsUtil.dpToPx(20);
    private List<StateStruct> mStateStruct = new ArrayList<>();


    public RoundCornerTextView(Context context) {
        this(context, null);
    }

    public RoundCornerTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public RoundCornerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerTextView);
            mRadius = typedArray.getDimensionPixelSize(R.styleable.RoundCornerTextView_tvRadius, mRadius);
            int[] normalGradientColor = parseColorFromTypeArray(typedArray, R.styleable.RoundCornerTextView_tvNormalColor, R.styleable.RoundCornerTextView_tvNormalGradientColors);
            int[] pressGradientColor = parseColorFromTypeArray(typedArray, R.styleable.RoundCornerTextView_tvPressColor, R.styleable.RoundCornerTextView_tvPressGradientColors);
            int[] disEnableGradientColor = parseColorFromTypeArray(typedArray, R.styleable.RoundCornerTextView_tvDisEnableColor, R.styleable.RoundCornerTextView_tvDisEnableGradientColors);
            GradientDrawable.Orientation normalAgent = getOrientation(typedArray.getInteger(R.styleable.RoundCornerTextView_tvNormalGradientAgent, INVALID_AGENT));
            GradientDrawable.Orientation pressAgent = getOrientation(typedArray.getInteger(R.styleable.RoundCornerTextView_tvPressGradientAgent, INVALID_AGENT));
            GradientDrawable.Orientation  unEnableAgent = getOrientation(typedArray.getInteger(R.styleable.RoundCornerTextView_tvDisEnableGradientAgent, INVALID_AGENT));
            typedArray.recycle();
            addState(normalGradientColor, normalAgent);
            addState(pressGradientColor, pressAgent, new int[]{StateStruct.STATE_PRESSED});
            addState(disEnableGradientColor, unEnableAgent, new int[]{StateStruct.STATE_DISABLED});
            if (!ListUtils.isEmpty(normalGradientColor)) { //要保证常态颜色值不能为空
                setSelector();
            }
        }
    }

    /**
     * 解析xml中的颜色值，如果设置了单个颜色，不管是否设置了渐变颜色，不会去解析渐变颜色，只解析单个颜色值;如果只设置了渐变颜色，那么解析渐变颜色
     * @param typedArray
     * @param colorIndex
     * @param GradientColorIndex
     * @return
     */
    private int[] parseColorFromTypeArray(TypedArray typedArray, @StyleableRes int colorIndex, @StyleableRes int GradientColorIndex) {
        int color = typedArray.getColor(colorIndex, INVALID_COLOR);
        int[] colorList = null;
        if (color != INVALID_COLOR) {
            colorList = new int[] {color};
        } else {
            String gradientColorString = typedArray.getString(GradientColorIndex);
            if (!TextUtils.isEmpty(gradientColorString)) {
                colorList = parseColorsFromString(gradientColorString);
            }
        }
        return colorList;
    }

    /**
     * 解析xml中配置的渐变颜色集合字符串,这边默认颜色规则为"#111111|#222222|#333333"
     * @param colorString
     * @return
     */
    private int[] parseColorsFromString(String colorString) {
        int[] gradientColors = null;
        try {
            String [] colors = colorString.replaceAll(" ", "").split("\\|");
            Log.i(TAG, "parseColorsFromString colors = " + colors == null ? "null" : colors.toString());
            if (colors != null && colors.length > 0) {
                gradientColors = new int[colors.length];
                for (int i = 0; i < colors.length; i++) {
                    gradientColors[i] = Color.parseColor(colors[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "parseColorsFromString error");
        }

        return gradientColors;
    }

    /**
     * 设置背景颜色,支持常态按钮颜色,按住按钮颜色
     * @param normalColors  常态按钮颜色,渐变颜色列表，不渐变数组长度为1
     * @param pressColors 按住颜色,不需要设置按住颜色的话使用 {@link #setBackGroundColor(int... normalColor)} 方法
     */
   public void setBackGroundColor(int [] normalColors, int [] pressColors) {
       setBackGroundColor(normalColors, pressColors, null);
   }

    /**
     * 设置常态颜色，按住颜色，不可点击按钮颜色,渐变方向使用默认的
     * @param normalColors
     * @param pressColors
     * @param disEnableGradientColor
     */
   public void setBackGroundColor(int[] normalColors, int[] pressColors, int[] disEnableGradientColor) {
       Log.i(TAG, "RoundCornerTextView setBackGroundColor");
       if (ListUtils.isEmpty(normalColors)) {
           return;
       }
       ListUtils.clear(mStateStruct); //清除本地保存的旧有状态数据
       addStateStruct(new StateStruct(normalColors));
       if (pressColors != null) {
           addStateStruct(new StateStruct(pressColors, new int[] {StateStruct.STATE_PRESSED}));
       }
       if (disEnableGradientColor != null) {
           addStateStruct(new StateStruct(disEnableGradientColor, new int[] {StateStruct.STATE_DISABLED}));
       }
       setSelector();
   }

   public RoundCornerTextView addState(int [] colors, GradientDrawable.Orientation orientation, int[] states) {
    if (!ListUtils.isEmpty(colors)) {
        StateStruct stateStruct = new StateStruct(colors, orientation, states);
        addStateStruct(stateStruct);
    }
    return this;
   }

    public RoundCornerTextView addState(int [] colors, GradientDrawable.Orientation orientation) {
        if (!ListUtils.isEmpty(colors)) {
            StateStruct stateStruct = new StateStruct(colors, orientation);
            addStateStruct(stateStruct);
        }
        return this;
    }

    public RoundCornerTextView addState(int [] colors, int[] states) {
        if (!ListUtils.isEmpty(colors)) {
            StateStruct stateStruct = new StateStruct(colors, states);
            addStateStruct(stateStruct);
        }
        return this;
    }

    public RoundCornerTextView addState(int [] colors) {
        if (!ListUtils.isEmpty(colors)) {
            StateStruct stateStruct = new StateStruct(colors);
            addStateStruct(stateStruct);
        }
        return this;
    }


    /**
     * 设置图片背景选择器，设置常态，按住等状态, 如果没有常态颜色值的话，那么其他状态就不设置了
     */
   private void setSelector() {
       StateListDrawable listDrawable = getSelector();
       if (listDrawable != null) {
           setBackgroundDrawable(listDrawable);
       }
   }


    /**
     * 设置圆角大小
     * @param radius
     */
   public void setRadius(int radius) {
       mRadius = radius;
       setSelector();
   }

    /**
     * 按钮只有常态颜色，按住状态颜色
     * @param normalColor
     * @param pressColor
     */
    public void setBackGroundColor(int normalColor, int pressColor) {
        setBackGroundColor(new int[] {normalColor}, new int[]{pressColor});
    }

    /**
     * 按钮常态渐变， 按钮颜色不渐变
     * @param normalColors
     * @param pressColor
     */
    public void setBackGroundColor(int[] normalColors, int pressColor) {
        setBackGroundColor(normalColors, new int[] {pressColor});
    }

    /**
     * 常态颜色不渐变， 按住颜色渐变
     * @param normalColor
     * @param pressColors
     */
    public void setBackGroundColor(int normalColor, int[] pressColors) {
        setBackGroundColor(new int[] {normalColor}, pressColors);
    }

    /**
     * 按钮有常态颜色，没有按住状态颜色
     * @param normalColor
     */
    public void setBackGroundColor(int... normalColor) {
        setBackGroundColor(normalColor, null);
    }


    private GradientDrawable.Orientation getOrientation(int agent) {
        GradientDrawable.Orientation orientation;
        switch (agent) {
            case 0:
                orientation = GradientDrawable.Orientation.BL_TR;
                break;
            case 1:
                orientation = GradientDrawable.Orientation.BOTTOM_TOP;
                break;
            case 2:
                orientation = GradientDrawable.Orientation.BR_TL;
                break;
            case 3:
                orientation = GradientDrawable.Orientation.LEFT_RIGHT;
                break;
            case 4:
                orientation = GradientDrawable.Orientation.RIGHT_LEFT;
                break;
            case 5:
                orientation = GradientDrawable.Orientation.TL_BR;
                break;
            case 6:
                orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                break;
            case 7:
                orientation = GradientDrawable.Orientation.TR_BL;
                break;
            default:
                orientation = GradientDrawable.Orientation.TL_BR;
                break;
        }
        return orientation;
    }


    /**
     * GradientDrawable 背景图生成所需属性集合类
     * 包括 orientation : 渐变方向
     * colors ; 渐变颜色集合
     * states : GradientDrawable 在图片集合中对应的状态结合;STATE_PRESSED/STATE_UNPRESSED/STATE_ENABLE/STATE_DISABLED/STATE_FOCUSED/STATE_UNFOCUSED
     */
    public static class StateStruct {
        public static int STATE_PRESSED = android.R.attr.state_pressed; //按住转态
        public static int STATE_UNPRESSED = -android.R.attr.state_pressed; //不按住转态
        public static int STATE_ENABLE = android.R.attr.state_enabled; //可点击状态
        public static int STATE_DISABLED = -android.R.attr.state_enabled; //不可点击状态
        public static int STATE_FOCUSED = android.R.attr.state_focused; //有焦点状态
        public static int STATE_UNFOCUSED = -android.R.attr.state_focused; //无焦点状态

        public int[] colors; //颜色组合，可一个可多个颜色，多个颜色情况时代表渐变
        public GradientDrawable.Orientation orientation = GradientDrawable.Orientation.TL_BR; //代表渐变方向
        public int[] states; //state状态集合,可能是多种状态的情况下显示这张图片，比如state_enabled="true" android:state_focused="true" ;常态则states为空

        public StateStruct(int[] colors, GradientDrawable.Orientation orientation, int... states) {
            this.colors = colors;
            this.orientation = orientation;
            this.states = states;
        }

        public StateStruct(int... colors) {
            this.colors = colors;
        }

        public StateStruct(int[] colors, GradientDrawable.Orientation orientation) {
            this.colors = colors;
            this.orientation = orientation;
        }

        public StateStruct(int[] colors, int[] states) {
            this.colors = colors;
            this.states = states;
        }

        public StateStruct() {
        }
    }

    /**
     * 增加按钮的状态效果
     * @param stateStruct
     * @return
     */
    public RoundCornerTextView setStateStruct(List<StateStruct> stateStruct) {
        mStateStruct.clear();
        if (stateStruct != null) {
            mStateStruct.addAll(stateStruct);
        }
        setSelector();
        return this;
    }

    /**
     * 增加按钮的状态效果
     * @param stateStruct
     * @return
     */
    public RoundCornerTextView addStateStruct(StateStruct stateStruct) {
        if (stateStruct != null) {
            mStateStruct.add(stateStruct);
        }
        return this;
    }

    /**
     * 显示效果，调用addStateStruct()之后要调用showStateEffect()才会起作用
     */
    public void showStateEffect() {
        setSelector();
    }


    /**
     * 获取View的背景样式selector,添加state时，是有顺序的，
     * stateListDrawable会先执行最新添加的state，如果不是该state，在执行下面的state，如果把大范围的state放到前面添加，会导致直接执行大范围的state，而不执行后面的state
     * @return
     */
    private StateListDrawable getSelector() {
        if (ListUtils.isEmpty(mStateStruct)) {
            return null;
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        GradientDrawable normalDrawable = null;
        for (StateStruct stateStruct : mStateStruct) {
            GradientDrawable drawable = getLinearGradientDrawable(stateStruct);
            if (drawable != null) {
                if (ListUtils.isEmpty(stateStruct.states)) {
                    normalDrawable = drawable;
                } else {
                    stateListDrawable.addState(stateStruct.states, drawable);
                }
            }
        }
        if (normalDrawable != null) { //保证常态Drawable要最后设置，不然会覆盖前面的
            stateListDrawable.addState(new int[]{}, normalDrawable);
        }
        return stateListDrawable;
    }

    /**
     * 获取线性渐变Drawable GradientDrawable
     * @param stateStruct
     * @return
     */
    public GradientDrawable getLinearGradientDrawable(StateStruct stateStruct) {
        if (stateStruct == null || ListUtils.isEmpty(stateStruct.colors)) {
            return null;
        }
        return getLinearGradientDrawable(stateStruct.orientation, stateStruct.colors);
    }


    /**
     * 获取线性渐变Drawable GradientDrawable
     * @param orientation 渐变方向
     * @param colors 渐变颜色列表
     * @return
     */
    public GradientDrawable getLinearGradientDrawable(GradientDrawable.Orientation orientation, int... colors) {
        if (ListUtils.isEmpty(colors)) {
            return null;
        }
        GradientDrawable drawable;
        if (colors.length == 1) { //非渐变
            drawable = new GradientDrawable();
            drawable.setColor(colors[0]);
        } else { //渐变
            drawable = new GradientDrawable(orientation, colors);
            drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        }
        if (mRadius > 0) {
            drawable.setCornerRadius(mRadius);
        }
        return drawable;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ListUtils.clear(mStateStruct);
    }
}
