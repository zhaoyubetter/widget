package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.cz.app.R;
import com.cz.app.widget.CheckLayout;
import com.cz.library.widget.DivideLinearLayout;

import java.util.ArrayList;

/**
 * Created by cz on 16/3/7.
 */
public class DivideLinedarLayoutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_divide_linear_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final DivideLinearLayout layout1 = (DivideLinearLayout) view.findViewById(R.id.layout1);
        final DivideLinearLayout layout2 = (DivideLinearLayout) view.findViewById(R.id.layout2);
        CheckLayout checkLayout = (CheckLayout) view.findViewById(R.id.cl_gravity);
        SwitchCompat switchCompat = (SwitchCompat) view.findViewById(R.id.sc_divide);
        checkLayout.setOnCheckedListener(new CheckLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, ArrayList<Integer> items, int position) {
                int[] gravities = {1, 2, 4, 8};
                int gravity = 0;
                for (Integer index : items) {
                    gravity += gravities[index];
                }
                layout1.setDivideGravity(gravity);
                layout2.setDivideGravity(gravity);
            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                layout1.setItemDivide(isChecked);
                layout2.setItemDivide(isChecked);
            }
        });
    }
}
