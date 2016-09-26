package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.cz.app.R;
import com.cz.app.widget.SeekLayout;
import com.cz.library.widget.DialView;

/**
 * Created by Administrator on 2016/9/26.
 */
public class DialViewFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dial_view,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final DialView dialView= (DialView) view.findViewById(R.id.dv_view);
        SeekLayout layout= (SeekLayout) view.findViewById(R.id.sl_layout);
        layout.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                float fraction=progress*1.0f/seekBar.getMax();
                dialView.setProgress(fraction);
            }
        });
    }
}
