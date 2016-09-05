package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cz.app.R;
import com.cz.app.widget.CheckLayout;
import com.cz.library.widget.DivideLinearLayout;

import java.util.ArrayList;

/**
 * Created by cz on 16/3/7.
 */
public class DivideLinedarLayoutFragment extends Fragment {
    private static final String TAG = "DivideLinedarLayoutFragment";

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
        checkLayout.setOnCheckedListener(new CheckLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, ArrayList<Integer> items, int position) {
                int[] gravities = {0x02,0x04,0x08,0x10,0x20,0x40};
                int gravity = 0;
                for (Integer index : items) {
                    gravity += gravities[index];
                }
                layout1.setDivideGravityInner(gravity);
                layout2.setDivideGravityInner(gravity);
            }
        });
    }
}
