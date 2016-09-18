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
import com.cz.library.widget.BothTextView;

/**
 * Created by cz on 9/14/16.
 */
public class BothTextViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_both_text_view,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final BothTextView textView= (BothTextView) view.findViewById(R.id.bv_text_View);
        final RadioLayout layout= (RadioLayout) view.findViewById(R.id.rl_gravity);
        layout.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                textView.setImageGravity(0 == position ? BothTextView.LEFT : BothTextView.RIGHT);
            }
        });

        SeekLayout layout1= (SeekLayout) view.findViewById(R.id.sb_width);
        SeekLayout layout2= (SeekLayout) view.findViewById(R.id.sb_height);
        layout1.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                textView.setImageDrawableWidth(progress);
            }
        });
        layout2.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                textView.setImageDrawableHeight(progress);
            }
        });
    }
}
