package com.cz.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.cz.app.R;
import com.cz.library.util.Utils;
import com.cz.library.widget.RadioGridLayout;

import java.util.ArrayList;

/**
 * Created by cz on 16/3/8.
 */
public class ColorLayout extends ScrollView {
    private final int[] colorItems;
    private RadioGridLayout.OnCheckListener listener;

    public ColorLayout(Context context) {
        this(context, null);
    }

    public ColorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        colorItems = Utils.getIntArray(R.array.color_items);
        inflate(context, R.layout.color_layout, this);
        RadioGridLayout layout = (RadioGridLayout) findViewById(R.id.rl_layout);
        layout.setOnCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void onSingleChoice(View v, int newPosition, int oldPosition) {
                if (null != listener) {
                    listener.onSingleChoice(v, newPosition, oldPosition);
                }
            }

            @Override
            public void onMultiChoice(View v, ArrayList<Integer> mChoicePositions) {
            }

            @Override
            public void onRectangleChoice(int startPosition, int endPosition) {
            }
        });
        for (int i = 0; i < colorItems.length; i++) {
            int color = colorItems[i];
            View colorView = new View(context);
            colorView.setBackgroundColor(color);
            layout.addView(colorView);
        }
    }

    public int getColor(int position) {
        return colorItems[position];
    }

    public void setOnCheckedListener(RadioGridLayout.OnCheckListener listener) {
        this.listener = listener;
    }

}
