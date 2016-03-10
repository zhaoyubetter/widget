package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cz.app.R;
import com.cz.app.adapter.SimpleFragmenStatePagerAdapter;

/**
 * Created by cz on 16/3/7.
 */
public class FrameViewFragment extends Fragment {
    private final int ITEM_COUNT = 20;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frame_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        Fragment[] fragments = new Fragment[ITEM_COUNT];
        String[] titles = new String[ITEM_COUNT];
        for (int i = 0; i < ITEM_COUNT; i++) {
            titles[i] = "Title" + (i + 1);
            tabLayout.addTab(tabLayout.newTab());
            fragments[i] = new ListFragment();
        }
        viewPager.setAdapter(new SimpleFragmenStatePagerAdapter(getChildFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewPager);
    }
}
