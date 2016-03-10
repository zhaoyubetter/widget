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
import com.cz.library.widget.WaveImageView;

/**
 * Created by cz on 16/3/9.
 */
public class WaveImageViewFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wave_image_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final WaveImageView imageView = (WaveImageView) view.findViewById(R.id.iv_image);
        imageView.setImageResource(R.mipmap.label_play);
        SeekLayout levelSeek = (SeekLayout) view.findViewById(R.id.sl_wave_level);
        levelSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                imageView.setWaveLevel(progress * 1f / seekBar.getMax());
            }
        });
        view.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.toggleWave();
            }
        });
        view.findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.toggleWave();
            }
        });
    }
}
