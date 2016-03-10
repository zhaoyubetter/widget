package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cz.app.R;
import com.cz.app.widget.RadioLayout;
import com.cz.library.widget.IndicateView;

/**
 * Created by cz on 16/3/7.
 */
public class IndicateViewFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_indicate_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final IndicateView indicateView1 = (IndicateView) view.findViewById(R.id.indicate1);
        final IndicateView indicateView2 = (IndicateView) view.findViewById(R.id.indicate2);
        RadioLayout radioLayout = (RadioLayout) view.findViewById(R.id.rl_gravity);
        radioLayout.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                switch (position) {
                    case 0:
                        indicateView1.setIndicateGravity(IndicateView.TOP);
                        indicateView2.setIndicateGravity(IndicateView.TOP);
                        break;
                    case 1:
                        indicateView1.setIndicateGravity(IndicateView.CENTER);
                        indicateView2.setIndicateGravity(IndicateView.CENTER);
                        break;
                    case 2:
                        indicateView1.setIndicateGravity(IndicateView.BOTTOM);
                        indicateView2.setIndicateGravity(IndicateView.BOTTOM);
                        break;
                }
            }
        });
    }
}
