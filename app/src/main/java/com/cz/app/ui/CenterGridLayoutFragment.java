package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.cz.app.R;
import com.cz.app.widget.RadioLayout;
import com.cz.app.widget.SeekLayout;
import com.cz.library.util.Utils;
import com.cz.library.widget.CenterGridLayout;

import java.util.Random;

/**
 * Created by cz on 16/3/7.
 */
public class CenterGridLayoutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_center_grid_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final CenterGridLayout layout = (CenterGridLayout) view.findViewById(R.id.cl_layout);
        SeekLayout rawSeek = (SeekLayout) view.findViewById(R.id.sl_raw);
        rawSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                layout.setFixRaw(progress);
            }
        });
        SeekLayout widthSeek = (SeekLayout) view.findViewById(R.id.sl_width);
        widthSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                layout.setItemWidth(40 + progress);
            }
        });
        SeekLayout heightSeek = (SeekLayout) view.findViewById(R.id.sl_height);
        heightSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                layout.setItemHeight(40 + progress);
            }
        });
        SeekLayout horizontalPaddingSeek = (SeekLayout) view.findViewById(R.id.sl_horizontal_padding);
        horizontalPaddingSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                layout.setItemHorizontalPadding(progress);
            }
        });
        SeekLayout verticalPaddingSeek = (SeekLayout) view.findViewById(R.id.sl_vertical_padding);
        verticalPaddingSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                layout.setItemVerticalPadding(progress);
            }
        });
        RadioLayout typeLayout = (RadioLayout) view.findViewById(R.id.rl_type);
        typeLayout.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                layout.setItemSizeMode(position);
            }
        });

        final int[] colorItems = Utils.getIntArray(R.array.color_items);
        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View childView = new View(getActivity());
                childView.setBackgroundColor(colorItems[new Random().nextInt(colorItems.length)]);
                layout.addView(childView);
            }
        });
        view.findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = layout.getChildCount();
                if (0 < childCount) {
                    layout.removeViewAt(childCount - 1);
                }
            }
        });
    }
}
