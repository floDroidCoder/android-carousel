package com.example.theflop.carousel.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by TheFlop on 08/02/2015.
 */
public class PdfView extends ImageView {
    public PdfView(Context context) {
        super(context);
    }

    public PdfView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void log(String s) {
        Log.d(this.getClass().getName(), s);
    }

}
