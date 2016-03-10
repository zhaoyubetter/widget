package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cz.app.R;
import com.cz.app.widget.RadioLayout;
import com.cz.library.widget.BadgerTextView;

import java.util.Random;

/**
 * Created by cz on 16/3/7.
 */
public class BadgerTextViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_badger_text_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final BadgerTextView badgerTextView = (BadgerTextView) view.findViewById(R.id.tv_menu3);
        RadioLayout layout1 = (RadioLayout) view.findViewById(R.id.rg_layout1);
        RadioLayout layout2 = (RadioLayout) view.findViewById(R.id.rg_layout2);
        layout1.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                switch (position) {
                    case 0:
                        badgerTextView.setBadgerGravity(BadgerTextView.L_T);
                        break;
                    case 1:
                        badgerTextView.setBadgerGravity(BadgerTextView.R_T);
                        break;
                    case 2:
                        badgerTextView.setBadgerGravity(BadgerTextView.L_B);
                        break;
                    case 3:
                        badgerTextView.setBadgerGravity(BadgerTextView.R_B);
                        break;
                }
            }
        });

        layout2.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                switch (position) {
                    case 0:
                        badgerTextView.setBadgerText(null);
                        break;
                    case 1:
                        badgerTextView.setBadgerText(String.valueOf(new Random().nextInt(100)));
                        break;
                    case 2:
                        badgerTextView.setBadgerText("99+");
                        break;
                }
            }
        });
    }
}
