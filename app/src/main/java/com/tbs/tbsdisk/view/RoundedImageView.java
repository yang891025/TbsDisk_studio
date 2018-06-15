package com.tbs.tbsdisk.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.tbs.tbsdisk.R;
import com.tbs.tbsdisk.utils.LogUtils;


/**
 * Created by EchoGe on 2016/6/16.
 */

public class RoundedImageView extends android.support.v7.widget.AppCompatImageView {

    public static final String TAG = "RoundedImageView";
    public static final float DEFAULT_RADIUS = 0f;
    public static final float DEFAULT_BORDER_WIDTH = 0f;
    private static final ScaleType[] SCALE_TYPES = {
            ScaleType.MATRIX,
            ScaleType.FIT_XY,
            ScaleType.FIT_START,
            ScaleType.FIT_CENTER,
            ScaleType.FIT_END,
            ScaleType.CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE
    };

    private float cornerRadius = DEFAULT_RADIUS;
    private float borderWidth = DEFAULT_BORDER_WIDTH;
    private ColorStateList borderColor =
            ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
    private boolean isOval = false;
    private boolean mutateBackground = false;

    private int mResource;
    private Drawable mDrawable;
    private Drawable mBackgroundDrawable;

    private ScaleType mScaleType;

    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context
                .obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyle, 0);

        int index = a.getInt(R.styleable.RoundedImageView_android_scaleType, -1);
        if (index >= 0) {
            setScaleType(SCALE_TYPES[index]);
        } else {
            // default scaletype to FIT_CENTER
            setScaleType(ScaleType.FIT_XY);
        }

        cornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_corner_radius, -1);
        borderWidth = a.getDimensionPixelSize(R.styleable.RoundedImageView_border_width, -1);

        // don't allow negative values for radius and border
        if (cornerRadius < 0) {
            cornerRadius = DEFAULT_RADIUS;
        }
        if (borderWidth < 0) {
            borderWidth = DEFAULT_BORDER_WIDTH;
        }

        borderColor = a.getColorStateList(R.styleable.RoundedImageView_border_color);
        if (borderColor == null) {
            borderColor = ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
        }

        mutateBackground = a.getBoolean(R.styleable.RoundedImageView_mutate_background, false);
        isOval = a.getBoolean(R.styleable.RoundedImageView_oval, false);

        updateDrawableAttrs();
        setBackground(super.getBackground());
        updateBackgroundDrawableAttrs(true);

        a.recycle();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    /**
     * Return the current scale type in use by this ImageView.
     *
     * @attr ref android.R.styleable#ImageView_scaleType
     * @see ScaleType
     */
    @Override
    public ScaleType getScaleType() {
        return mScaleType;
    }

    /**
     * Controls how the image should be resized or moved to match the size
     * of this ImageView.
     *
     * @param scaleType The desired scaling mode.
     * @attr ref android.R.styleable#ImageView_scaleType
     */
    @Override
    public void setScaleType(ScaleType scaleType) {
        assert scaleType != null;

        if (mScaleType != scaleType) {
            mScaleType = scaleType;

            switch (scaleType) {
                case CENTER:
                case CENTER_CROP:
                case CENTER_INSIDE:
                case FIT_CENTER:
                case FIT_START:
                case FIT_END:
                case FIT_XY:
                    super.setScaleType(ScaleType.FIT_XY);
                    break;
                default:
                    super.setScaleType(scaleType);
                    break;
            }

            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        mResource = 0;
        mDrawable = RoundedDrawable.fromDrawable(drawable);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        mResource = 0;
        mDrawable = RoundedDrawable.fromBitmap(bm);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageResource(int resId) {
        if (mResource != resId) {
            mResource = resId;
            mDrawable = resolveResource();
            updateDrawableAttrs();
            super.setImageDrawable(mDrawable);
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    private Drawable resolveResource() {
        Resources rsrc = getResources();
        if (rsrc == null) {
            return null;
        }

        Drawable d = null;

        if (mResource != 0) {
            try {
                d = rsrc.getDrawable(mResource);
            } catch (Exception e) {
                LogUtils.d(TAG, "Unable to find resource: " + mResource);
                // Don't try again.
                mResource = 0;
            }
        }
        return RoundedDrawable.fromDrawable(d);
    }

    private void updateDrawableAttrs() {
        updateAttrs(mDrawable);
    }

    private void updateBackgroundDrawableAttrs(boolean convert) {
        if (mutateBackground) {
            if (convert) {
                mBackgroundDrawable = RoundedDrawable.fromDrawable(mBackgroundDrawable);
            }
            updateAttrs(mBackgroundDrawable);
        }
    }

    private void updateAttrs(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        if (drawable instanceof RoundedDrawable) {
            ((RoundedDrawable) drawable)
                    .setScaleType(mScaleType)
                    .setCornerRadius(cornerRadius)
                    .setBorderWidth(borderWidth)
                    .setBorderColor(borderColor)
                    .setOval(isOval);
        } else if (drawable instanceof LayerDrawable) {
            // loop through layers to and set drawable attrs
            LayerDrawable ld = ((LayerDrawable) drawable);
            for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
                updateAttrs(ld.getDrawable(i));
            }
        }
    }

    @Override
    @Deprecated
    public void setBackground(Drawable background) {
        //设置背景颜色值，时默认是圆形的
        if (background instanceof ColorDrawable && mutateBackground && isOval) {
            super.setBackground(getBackgroundLayerColorDrawable(
                    (ColorDrawable) background));
        } else {
            mBackgroundDrawable = background;
            updateBackgroundDrawableAttrs(true);
            super.setBackground(mBackgroundDrawable);
        }
    }

    @NonNull
    private LayerDrawable getBackgroundLayerColorDrawable(ColorDrawable background) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.setIntrinsicHeight(getHeight());
        shapeDrawable.setIntrinsicWidth(getWidth());
        shapeDrawable.getPaint().setColor(background.getColor());

        ShapeDrawable shapeDrawable1 = new ShapeDrawable(new OvalShape());
        shapeDrawable1.setIntrinsicHeight(getHeight());
        shapeDrawable1.setIntrinsicWidth(getWidth());
        shapeDrawable1.getPaint().setColor(Color.TRANSPARENT);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                shapeDrawable1, shapeDrawable,
        });
        layerDrawable.setLayerInset(1, 1, 1, 1, 1);
        return layerDrawable;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int resId) {
        setCornerRadius(getResources().getDimension(resId));
    }

    public void setCornerRadius(float radius) {
        if (cornerRadius == radius) {
            return;
        }

        cornerRadius = radius;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int resId) {
        setBorderWidth(getResources().getDimension(resId));
    }

    public void setBorderWidth(float width) {
        if (borderWidth == width) {
            return;
        }

        borderWidth = width;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public int getBorderColor() {
        return borderColor.getDefaultColor();
    }

    public void setBorderColor(int color) {
        setBorderColor(ColorStateList.valueOf(color));
    }

    public ColorStateList getBorderColors() {
        return borderColor;
    }

    public void setBorderColor(ColorStateList colors) {
        if (borderColor.equals(colors)) {
            return;
        }

        borderColor =
                (colors != null) ?
                        colors :
                        ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        if (borderWidth > 0) {
            invalidate();
        }
    }

    public boolean isOval() {
        return isOval;
    }

    public void setOval(boolean oval) {
        isOval = oval;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public boolean isMutateBackground() {
        return mutateBackground;
    }

    public void setMutateBackground(boolean mutate) {
        if (mutateBackground == mutate) {
            return;
        }

        mutateBackground = mutate;
        updateBackgroundDrawableAttrs(true);
        invalidate();
    }
}
