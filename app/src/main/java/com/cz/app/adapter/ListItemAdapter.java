package com.cz.app.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cz.app.R;
import com.cz.library.util.AnimUtils;
import com.cz.library.widget.RecyclerTabLayout;
import com.ldzs.recyclerlibrary.adapter.BaseViewHolder;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cz on 16/3/7.
 */
public class ListItemAdapter extends RecyclerTabLayout.Adapter<BaseViewHolder> {
    private final LayoutInflater layoutInflater;
    private final ArrayList<String> items;

    public ListItemAdapter(Context context, ViewPager viewPager, List<String> items) {
        super(viewPager);
        this.layoutInflater = LayoutInflater.from(context);
        this.items = new ArrayList<>();
        if (null != items) {
            this.items.addAll(items);
        }

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, int selectColor, int defaultColor) {
        TextView textView = (TextView) holder.itemView;
        String item = this.items.get(position);
        if (null != item) {
            textView.setText(item.toString());
        }
    }

    @Override
    public void onTabScroll(View lastView, View selectView, float offset, float scale, int selectColor, int defaultColor) {
        if (null != lastView) {
            TextView tabView = (TextView) lastView;
            int color = AnimUtils.evaluate(offset, selectColor, defaultColor);
            tabView.setTextColor(color);
            ViewHelper.setScaleX(tabView, 1f + scale * (1f - offset));
            ViewHelper.setScaleY(tabView, 1f + scale * (1f - offset));
        }
        if (null != selectView) {
            TextView tabView = (TextView) selectView;
            int color = AnimUtils.evaluate(offset, defaultColor, selectColor);
            tabView.setTextColor(color);
            ViewHelper.setScaleX(tabView, 1f + scale * offset);
            ViewHelper.setScaleY(tabView, 1f + scale * offset);
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(layoutInflater.inflate(R.layout.simple_text_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
