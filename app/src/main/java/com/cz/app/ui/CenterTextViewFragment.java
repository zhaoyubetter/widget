package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.cz.app.R;
import com.cz.app.widget.CheckLayout;
import com.cz.app.widget.RadioLayout;
import com.cz.app.widget.SeekLayout;
import com.cz.library.util.Utils;
import com.cz.library.widget.CenterTextView;

import java.util.ArrayList;

/**
 * Created by cz on 16/3/7.
 */
public class CenterTextViewFragment extends Fragment {
    private static final String TAG = "CenterTextViewFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_center_text_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final CenterTextView textView = (CenterTextView) view.findViewById(R.id.cv_text_view);
        RadioLayout layout = (RadioLayout) view.findViewById(R.id.rl_mode);
        layout.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int index, boolean isChecked) {
                textView.setDrawableMode(index);
            }
        });
        CheckLayout modeLayout = (CheckLayout) view.findViewById(R.id.rl_gravity);
        modeLayout.setOnCheckedListener(new CheckLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, ArrayList<Integer> items, int position) {
                int[] drawableRes = new int[4];
                for (Integer index : items) {
                    drawableRes[index] = R.mipmap.task_icon;
                }
                textView.setCompoundDrawablesWithIntrinsicBounds(drawableRes[0], drawableRes[1], drawableRes[2], drawableRes[3]);
            }
        });

        CheckLayout sizeLayout = (CheckLayout) view.findViewById(R.id.rl_size);
        sizeLayout.setOnCheckedListener(new CheckLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, ArrayList<Integer> items, int position) {
                int mode = 0;
                int[] modes = {1, 2, 4, 8};
                for (Integer index : items) {
                    mode += modes[index];
                }
                textView.setSizeMode(mode);
            }
        });
        SeekLayout layout1 = (SeekLayout) view.findViewById(R.id.sl_width);
        SeekLayout layout2 = (SeekLayout) view.findViewById(R.id.sl_height);
        SeekLayout layout3 = (SeekLayout) view.findViewById(R.id.sl_padding);

        layout1.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                textView.setDrawableWidth(Utils.dip2px(20) + progress);
            }
        });
        layout2.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                textView.setDrawableHeight(Utils.dip2px(20) + progress);
            }
        });
        layout3.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                textView.setCompoundDrawablePadding(progress);
            }
        });


        SwitchCompat switchCompat = (SwitchCompat) view.findViewById(R.id.sc_switch);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textView.setDebugDraw(isChecked);
            }
        });
    }
}
