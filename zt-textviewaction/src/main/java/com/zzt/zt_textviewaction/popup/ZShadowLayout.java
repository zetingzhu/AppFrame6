package com.zzt.zt_textviewaction.popup;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;

import com.zzt.zt_textviewaction.R;


/**
 * @author: zeting
 * @date: 2021/9/5
 * 阴影控件
 * https://github.com/lihangleo2/ShadowLayout 去掉了不用的功能
 */
public class ZShadowLayout extends FrameLayout {

    private int mShadowColor; // 阴影颜色
    private float mShadowLimit;// 阴影宽度
    private float mCornerRadius;// 阴影圆角
    private float mDx; // x轴偏移
    private float mDy;// y轴便宜
    private boolean leftShow; // 隐藏左边
    private boolean rightShow;// 隐藏右边
    private boolean topShow;// 隐藏上边
    private boolean bottomShow;//隐藏下边
    private Paint shadowPaint;// 阴影画笔
    /*** 间距 */
    private int leftPadding;
    private int topPadding;
    private int rightPadding;
    private int bottomPadding;

    //是否展示阴影
    private boolean isShowShadow = true;
    // 有偏移是否对称
    private boolean isSym;


    public ZShadowLayout(Context context) {
        this(context, null);
    }

    public ZShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ZShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    //增加selector样式
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (getWidth() != 0) {
        } else {
            addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    removeOnLayoutChangeListener(this);
                    setSelected(isSelected());
                }
            });
        }
    }


    //是否隐藏阴影
    public void setShadowHidden(boolean isShowShadow) {
        this.isShowShadow = !isShowShadow;
        if (getWidth() != 0 && getHeight() != 0) {
            setBackgroundCompat(getWidth(), getHeight());
        }
    }


    //设置x轴偏移量
    public void setShadowOffsetX(float mDx) {
        if (isShowShadow) {
            if (Math.abs(mDx) > mShadowLimit) {
                if (mDx > 0) {
                    this.mDx = mShadowLimit;
                } else {
                    this.mDx = -mShadowLimit;
                }
            } else {
                this.mDx = mDx;
            }
            setPadding();
        }
    }

    /**
     * 是否对称
     *
     * @param sym
     */
    public void setShadowSymmetry(boolean sym) {
        if (isShowShadow) {
            isSym = sym;
            setPadding();
        }
    }


    //动态设置y轴偏移量
    public void setShadowOffsetY(float mDy) {
        if (isShowShadow) {
            if (Math.abs(mDy) > mShadowLimit) {
                if (mDy > 0) {
                    this.mDy = mShadowLimit;
                } else {
                    this.mDy = -mShadowLimit;
                }
            } else {
                this.mDy = mDy;
            }
            setPadding();
        }
    }


    public float getCornerRadius() {
        return mCornerRadius;
    }

    //动态设置 圆角属性
    public void setCornerRadius(int mCornerRadius) {
        this.mCornerRadius = mCornerRadius;
        if (getWidth() != 0 && getHeight() != 0) {
            setBackgroundCompat(getWidth(), getHeight());
        }
    }

    public float getShadowLimit() {
        return mShadowLimit;
    }

    //动态设置阴影扩散区域
    public void setShadowLimit(int mShadowLimit) {
        if (isShowShadow) {
            int dip5 = dp2px(getContext(), 5f);
            if (mShadowLimit >= dip5) {
                this.mShadowLimit = mShadowLimit;
            } else {
                this.mShadowLimit = dip5;
            }
            setPadding();
        }
    }

    //动态设置阴影颜色值
    public void setShadowColor(int mShadowColor) {
        this.mShadowColor = mShadowColor;
        if (getWidth() != 0 && getHeight() != 0) {
            setBackgroundCompat(getWidth(), getHeight());
        }
    }


    //是否隐藏阴影的上边部分
    public void setShadowHiddenTop(boolean topShow) {
        this.topShow = !topShow;
        setPadding();
    }

    public void setShadowHiddenBottom(boolean bottomShow) {
        this.bottomShow = !bottomShow;
        setPadding();
    }


    public void setShadowHiddenRight(boolean rightShow) {
        this.rightShow = !rightShow;
        setPadding();
    }


    public void setShadowHiddenLeft(boolean leftShow) {
        this.leftShow = !leftShow;
        setPadding();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildAt(0) == null) {
            //当子View都没有的时候。默认不使用阴影
            isShowShadow = false;
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            setBackgroundCompat(w, h);
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        initAttributes(attrs);
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.FILL);

        setPadding();

    }


    public int dip2px(float dipValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void setPadding() {
        if (isShowShadow && mShadowLimit > 0) {
            //控件区域是否对称，默认是对称。不对称的话，那么控件区域随着阴影区域走
            if (isSym) {
                int xPadding = (int) (mShadowLimit + Math.abs(mDx));
                int yPadding = (int) (mShadowLimit + Math.abs(mDy));

                if (leftShow) {
                    leftPadding = xPadding;
                } else {
                    leftPadding = 0;
                }

                if (topShow) {
                    topPadding = yPadding;
                } else {
                    topPadding = 0;
                }


                if (rightShow) {
                    rightPadding = xPadding;
                } else {
                    rightPadding = 0;
                }

                if (bottomShow) {
                    bottomPadding = yPadding;
                } else {
                    bottomPadding = 0;
                }
            } else {
                if (Math.abs(mDy) > mShadowLimit) {
                    if (mDy > 0) {
                        mDy = mShadowLimit;
                    } else {
                        mDy = 0 - mShadowLimit;
                    }
                }


                if (Math.abs(mDx) > mShadowLimit) {
                    if (mDx > 0) {
                        mDx = mShadowLimit;
                    } else {
                        mDx = 0 - mShadowLimit;
                    }
                }

                if (topShow) {
                    topPadding = (int) (mShadowLimit - mDy);
                } else {
                    topPadding = 0;
                }

                if (bottomShow) {
                    bottomPadding = (int) (mShadowLimit + mDy);
                } else {
                    bottomPadding = 0;
                }


                if (rightShow) {
                    rightPadding = (int) (mShadowLimit - mDx);
                } else {
                    rightPadding = 0;
                }


                if (leftShow) {
                    leftPadding = (int) (mShadowLimit + mDx);
                } else {
                    leftPadding = 0;
                }
            }
            setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        }
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(int w, int h) {
        if (isShowShadow) {
            //判断传入的颜色值是否有透明度
            isAddAlpha(mShadowColor);
            Bitmap bitmap = createShadowBitmap(w, h, mShadowLimit, mDx, mDy, mShadowColor, Color.TRANSPARENT);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(drawable);
            } else {
                setBackground(drawable);
            }
        } else {
            this.setBackgroundColor(Color.parseColor("#00000000"));
        }
    }


    private void initAttributes(AttributeSet attrs) {
        TypedArray attr = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (attr == null) {
            return;
        }

        try {
            //默认是显示
            isShowShadow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHidden, false);
            leftShow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHiddenLeft, false);
            rightShow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHiddenRight, false);
            bottomShow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHiddenBottom, false);
            topShow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHiddenTop, false);
            mCornerRadius = attr.getDimension(R.styleable.ShadowLayout_hl_cornerRadius, 0);

            //默认扩散区域宽度
            mShadowLimit = attr.getDimension(R.styleable.ShadowLayout_hl_shadowLimit, 0);
            if (mShadowLimit == 0) {
                //如果阴影没有设置阴影扩散区域，那么默认隐藏阴影
                isShowShadow = false;
            } else {
                int dip5 = dp2px(getContext(), 5f);
                if (mShadowLimit < dip5) {
                    mShadowLimit = dip5;
                }
            }

            //x轴偏移量
            mDx = attr.getDimension(R.styleable.ShadowLayout_hl_shadowOffsetX, 0);
            //y轴偏移量
            mDy = attr.getDimension(R.styleable.ShadowLayout_hl_shadowOffsetY, 0);
            mShadowColor = attr.getColor(R.styleable.ShadowLayout_hl_shadowColor, Color.parseColor("#2a000000"));
            isSym = attr.getBoolean(R.styleable.ShadowLayout_hl_shadowSymmetry, true);
        } finally {
            attr.recycle();
        }
    }


    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float shadowRadius,
                                      float dx, float dy, int shadowColor, int fillColor) {
        //优化阴影bitmap大小,将尺寸缩小至原来的1/4。
        dx = dx / 4;
        dy = dy / 4;
        shadowWidth = shadowWidth / 4 == 0 ? 1 : shadowWidth / 4;
        shadowHeight = shadowHeight / 4 == 0 ? 1 : shadowHeight / 4;
        shadowRadius = shadowRadius / 4;

        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);

        //这里缩小limit的是因为，setShadowLayer后会将bitmap扩散到shadowWidth，shadowHeight
        //同时也要根据某边的隐藏情况去改变

        float rect_left = 0;
        float rect_right = 0;
        float rect_top = 0;
        float rect_bottom = 0;
        if (leftShow) {
            rect_left = shadowRadius;
        } else {
            rect_left = 0;

        }

        if (topShow) {
            rect_top = shadowRadius;
        } else {
            rect_top = 0;

        }

        if (rightShow) {
            rect_right = shadowWidth - shadowRadius;
        } else {
            rect_right = shadowWidth;

        }

        if (bottomShow) {
            rect_bottom = shadowHeight - shadowRadius;
        } else {
            rect_bottom = shadowHeight;

        }


        RectF shadowRect = new RectF(
                rect_left,
                rect_top,
                rect_right,
                rect_bottom);

        if (isSym) {
            if (dy > 0) {
                shadowRect.top += dy;
                shadowRect.bottom -= dy;
            } else if (dy < 0) {
                shadowRect.top += Math.abs(dy);
                shadowRect.bottom -= Math.abs(dy);
            }

            if (dx > 0) {
                shadowRect.left += dx;
                shadowRect.right -= dx;
            } else if (dx < 0) {

                shadowRect.left += Math.abs(dx);
                shadowRect.right -= Math.abs(dx);
            }
        } else {
            shadowRect.top -= dy;
            shadowRect.bottom -= dy;
            shadowRect.right -= dx;
            shadowRect.left -= dx;
        }

        shadowPaint.setColor(fillColor);
        if (!isInEditMode()) {//dx  dy
            shadowPaint.setShadowLayer(shadowRadius / 2, dx, dy, shadowColor);
        }

        shadowPaint.setAntiAlias(true);
        int leftTop = (int) mCornerRadius / 4;
        int leftBottom = (int) mCornerRadius / 4;
        int rightTop = (int) mCornerRadius / 4;
        int rightBottom = (int) mCornerRadius / 4;

        float[] outerR = new float[]{leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom};//左上，右上，右下，左下
        Path path = new Path();
        path.addRoundRect(shadowRect, outerR, Path.Direction.CW);
        canvas.drawPath(path, shadowPaint);

        return output;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPadding();
    }


    public void isAddAlpha(int color) {
        //获取单签颜色值的透明度，如果没有设置透明度，默认加上#2a
        if (Color.alpha(color) == 255) {
            String red = Integer.toHexString(Color.red(color));
            String green = Integer.toHexString(Color.green(color));
            String blue = Integer.toHexString(Color.blue(color));

            if (red.length() == 1) {
                red = "0" + red;
            }

            if (green.length() == 1) {
                green = "0" + green;
            }

            if (blue.length() == 1) {
                blue = "0" + blue;
            }
            String endColor = "#2a" + red + green + blue;
            mShadowColor = convertToColorInt(endColor);
        }
    }


    public static int convertToColorInt(String argb)
            throws IllegalArgumentException {

        if (!argb.startsWith("#")) {
            argb = "#" + argb;
        }

        return Color.parseColor(argb);
    }

    public int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }
}
