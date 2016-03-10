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
import com.cz.library.widget.TabLinearLayout;

/**
 * Created by cz on 16/3/9.
 */
public class TabLinearLayoutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_linear_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TabLinearLayout layout1 = (TabLinearLayout) view.findViewById(R.id.tab_layout1);
        final TabLinearLayout layout2 = (TabLinearLayout) view.findViewById(R.id.tab_layout2);
        final TabLinearLayout layout3 = (TabLinearLayout) view.findViewById(R.id.tab_layout3);
        final TabLinearLayout layout4 = (TabLinearLayout) view.findViewById(R.id.tab_layout4);
        SeekLayout widthSeek = (SeekLayout) view.findViewById(R.id.sl_tab_width);
        SeekLayout heightSeek = (SeekLayout) view.findViewById(R.id.sl_tab_height);
        SeekLayout horizontalSeek = (SeekLayout) view.findViewById(R.id.sl_horizontal_padding);
        SeekLayout verticalSeek = (SeekLayout) view.findViewById(R.id.sl_vertical_padding);
        RadioLayout radioLayout = (RadioLayout) view.findViewById(R.id.rl_tab_type);
        radioLayout.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                layout1.setTabMode(position);
                layout2.setTabMode(position);
                layout3.setTabMode(position);
            }
        });

        widthSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                layout1.setTabWidth(progress);
                layout2.setTabWidth(progress);
                layout3.setTabWidth(progress);
            }
        });
        heightSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                layout1.setTabHeight(progress);
                layout2.setTabHeight(progress);
                layout3.setTabHeight(progress);
            }
        });
        horizontalSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                layout1.setHorizontalPadding(progress);
                layout2.setHorizontalPadding(progress);
                layout3.setHorizontalPadding(progress);
                layout4.setHorizontalPadding(progress);
            }
        });
        verticalSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                layout1.setVerticalPadding(progress);
                layout2.setVerticalPadding(progress);
                layout3.setVerticalPadding(progress);
                layout4.setVerticalPadding(progress);
            }
        });
    }
}
