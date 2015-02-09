package com.example.theflop.carousel;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.theflop.carousel.view.CarouselView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        TextView label = getTextView(30);
        TextView subLabel = getTextView(18);

        CarouselView carouselView = new CarouselView(this);
        carouselView.setLabel(label);
        carouselView.setSublabel(subLabel);

        List<View> views = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            ImageView child = new ImageView(this);
            child.setImageDrawable(getResources().getDrawable(R.drawable.team_member_3));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5,0,5,0);

            child.setTag("View " + i);

            child.setLayoutParams(layoutParams);
            views.add(child);
        }

        carouselView.setItems(views);
        layout.addView(carouselView);
        layout.addView(label);
        layout.addView(subLabel);
    }

    private TextView getTextView(int fontSize) {
        TextView label = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        label.setLayoutParams(params);
        label.setGravity(Gravity.CENTER_HORIZONTAL);
        label.setText("Label");
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        label.setTextColor(Color.BLACK);
        return label;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
