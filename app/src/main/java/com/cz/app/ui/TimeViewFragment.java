package com.cz.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cz.app.R;
import com.cz.app.widget.RadioLayout;
import com.cz.library.util.Utils;
import com.cz.library.widget.TimeDownView;

public class TimeViewFragment extends Fragment {
    private TimeDownView timer;
    private RadioLayout intervalMode;
    private TextView filletValue;
    private SeekBar fillet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timerview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timer = (TimeDownView) view.findViewById(R.id.tv_timer);
        intervalMode = (RadioLayout) view.findViewById(R.id.rl_indicate_gravity);
        filletValue = (TextView) view.findViewById(R.id.tv_fillet_value);
        fillet = (SeekBar) view.findViewById(R.id.sb_fillet);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        intervalMode.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                timer.setIntervalMode(position);
            }
        });

        fillet.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //0-10;
                timer.setItemIntervalRoundRadius(progress);
                filletValue.setText(Utils.getString(R.string.fillet_value, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
