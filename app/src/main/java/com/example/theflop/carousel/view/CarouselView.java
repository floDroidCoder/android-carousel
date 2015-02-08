package com.example.theflop.carousel.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Carousel with animation for the most centered child view
 */
public class CarouselView extends HorizontalScrollView {
    private LinearLayout internalWrapper;

    // Constants
    private Integer LANDSCAPE_PADDING = 300;
    private Integer PORTRAIT_PADDING = 200;

    public CarouselView(Context context) {
        super(context);
        this.setSmoothScrollingEnabled(true);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setSmoothScrollingEnabled(true);
    }

    public CarouselView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setSmoothScrollingEnabled(true);
    }

    public void setItems(List<View> views) {
        internalWrapper = new LinearLayout(getContext());
        internalWrapper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        internalWrapper.setOrientation(LinearLayout.HORIZONTAL);

        addView(internalWrapper);

        for (View view : views) {
            view.setPadding(0, 50, 0, 50);
            internalWrapper.addView(view);
        }
        this.setFillViewport(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        updateViews();

        super.onDraw(canvas);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLayoutPadding(newConfig);
    }

    private void setLayoutPadding(Configuration config) {
        switch(config.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                //internalWrapper.setPadding(LANDSCAPE_PADDING,0,LANDSCAPE_PADDING,0);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                //internalWrapper.setPadding(PORTRAIT_PADDING,0,PORTRAIT_PADDING,0);
                break;
        }
    }

    private void updateViews() {
        Rect scrollBounds = new Rect();
        getDrawingRect(scrollBounds);
        if (internalWrapper != null) {
            for (int i = 0; i < internalWrapper.getChildCount(); i++) {
                ImageView view = (ImageView) internalWrapper.getChildAt(i);

                int left = view.getLeft();
                int right = view.getRight();
                int center = scrollBounds.left + ((scrollBounds.right - scrollBounds.left) / 2);

                if (left < center && right > center) {
                    scaleView(view, left, right, center);
                    view.setAlpha(1f);
                    view.bringToFront();
                } else {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                   view.setAlpha(0.5f);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
            }

            internalWrapper.invalidate();
        }
    }

    private void scaleView(ImageView view, int left, int right, int center) {
        float minDist = Math.min(Math.abs(center - left), Math.abs(center - right));
        float maxDist = view.getWidth() / 2;
        float threshold = (minDist / maxDist) * 2f;
        if (threshold < 1) threshold = 1;

        view.setScaleX(threshold);
        view.setScaleY(threshold);
    }

    private void log(String s) {
        Log.d(this.getClass().getName(), s);
    }

}
