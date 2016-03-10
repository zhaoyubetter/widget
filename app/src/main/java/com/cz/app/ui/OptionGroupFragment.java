package com.cz.app.ui;

import android.graphics.Color;
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
import com.cz.library.util.AnimUtils;
import com.cz.library.widget.OptionGroup;

/**
 * Created by cz on 16/3/9.
 */
public class OptionGroupFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_option_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final OptionGroup optionGroup = (OptionGroup) view.findViewById(R.id.group);
        RadioLayout edgeMode = (RadioLayout) view.findViewById(R.id.rl_edge_mode);
        SeekLayout horizontalPaddingSeek = (SeekLayout) view.findViewById(R.id.sl_horizontal_padding);
        SeekLayout verticalPaddingSeek = (SeekLayout) view.findViewById(R.id.sl_vertical_padding);
        SeekLayout roundRadiusSeek = (SeekLayout) view.findViewById(R.id.sl_round_radius);
        SeekLayout divideColorSeek = (SeekLayout) view.findViewById(R.id.sl_divide_color);

        edgeMode.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                optionGroup.setItemEdgeMode(position);
            }
        });
        horizontalPaddingSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                optionGroup.setItemHorizontalPadding(progress);
            }
        });
        verticalPaddingSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                optionGroup.setItemVerticalPadding(progress);
            }
        });
        roundRadiusSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                optionGroup.setRoundRadius(progress);
            }
        });
        divideColorSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                float fraction = progress * 1f / seekBar.getMax();
                int color = AnimUtils.evaluate(fraction, Color.GREEN, Color.RED);
                optionGroup.setDivideColor(color);
            }
        });

    }
}
