package com.example.theflop.carousel.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Carousel with animation for the most centered child view
 */
public class CarouselView extends HorizontalScrollView {
    public static final int DEFAULT_MARGIN = 25;
    private LinearLayout internalWrapper;
    private TextView label;
    private TextView sublabel;

    private View selectedView;

    public CarouselView(Context context) {
        super(context);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarouselView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setItems(List<View> views) {
        internalWrapper = new LinearLayout(getContext());
        internalWrapper.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        internalWrapper.setOrientation(LinearLayout.HORIZONTAL);
        internalWrapper.setGravity(Gravity.BOTTOM);
        addView(internalWrapper);

        Point size = getScreenSize();
        final int margin = size.x / 2  -  size.x / 6;

        int i = 0;
        for (final View view : views) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size.x / 3, size.y / 2);

            final int marginStart = (i==0)? margin : DEFAULT_MARGIN;
            int marginEnd   = (i==views.size()-1)? margin :DEFAULT_MARGIN;

            layoutParams.setMargins(marginStart,0,marginEnd,0);
            view.setLayoutParams(layoutParams);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int left = v.getLeft();
                    int right = v.getRight();
                    int center = left + ((right - left) / 2);

                    CarouselView.this.smoothScrollTo(center - 370, 0);
                }
            });

            internalWrapper.addView(view, layoutParams);

            i++;
        }

        this.setFillViewport(true);
        this.setSmoothScrollingEnabled(true);
        this.setHorizontalScrollBarEnabled(false);

        // DEBUG
        //this.setBackgroundColor(Color.RED);
    }

    private Point getScreenSize() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        return size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        updateViews();

        super.onDraw(canvas);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resizeViews(newConfig);
    }

    private void resizeViews(Configuration config) {
        if (internalWrapper != null) {
            for (int i = 0; i < internalWrapper.getChildCount(); i++) {
                View view = internalWrapper.getChildAt(i);

                Point size = getScreenSize();
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size.x / 3, size.y / 2);

                int marginStart = (i==0)?(size.x / 2  -  size.x / 6): DEFAULT_MARGIN;
                int marginEnd   = (i==internalWrapper.getChildCount()-1)?(size.x / 2  -  size.x / 6):DEFAULT_MARGIN;
                layoutParams.setMargins(marginStart,0,marginEnd,0);
                view.setLayoutParams(layoutParams);

                view.requestLayout();
                view.invalidate();
            }
        }

        switch(config.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (selectedView != null) {
                int left = selectedView.getLeft();
                int right = selectedView.getRight();

                //this.smoothScrollTo(right-left, (int) selectedView.getY());
            }

            return true;
        }

        return super.onTouchEvent(ev);
    }

    private void updateViews() {
        Rect scrollBounds = new Rect();
        getDrawingRect(scrollBounds);
        if (internalWrapper != null) {
            for (int i = 0; i < internalWrapper.getChildCount(); i++) {
                View view = internalWrapper.getChildAt(i);

                int left = view.getLeft();
                int right = view.getRight();
                int center = scrollBounds.left + ((scrollBounds.right - scrollBounds.left) / 2);

                if (left < center && right > center) {
                    scaleView(view, left, right, center);
                    view.setAlpha(1f);

                    label.setText(view.getTag().toString());
                    sublabel.setText(i + " PAGES");

                    selectedView = view;
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

    private void scaleView(View view, int left, int right, int center) {
        float minDist = Math.min(Math.abs(center - left), Math.abs(center - right));
        float maxDist = view.getWidth() / 2;
        float threshold = Math.max(1, 0.25f + (minDist / maxDist) * 1.5f);

        view.setScaleX(threshold);
        view.setScaleY(threshold);
    }

    private void log(String s) {
        Log.d(this.getClass().getName(), s);
    }

    public void setLabel(TextView label) {
        this.label = label;
    }

    public void setSublabel(TextView sublabel) {
        this.sublabel = sublabel;
    }
}
