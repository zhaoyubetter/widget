package com.cz.library.widget.indicator;

import android.graphics.Canvas;
import android.support.v4.view.ViewPager.OnPageChangeListener;

/**
 * 绘制偏移
 * <p/>
 * Created by cz on 15/2/10.
 */
public interface Indicatorable extends OnPageChangeListener {

    void draw(Canvas canvas);
}
