package com.cz.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 可编辑fragment条目的viewpager的adapter
 *
 * @author Administrator
 */
public class SimpleFragmenStatePagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragments;
    private final List<String> titles;
    private int[] positions;

    public SimpleFragmenStatePagerAdapter(FragmentManager fm, Fragment[] fragments) {
        this(fm, fragments, null, null);
    }

    public SimpleFragmenStatePagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        this(fm, fragments, titles, null);
    }

    public SimpleFragmenStatePagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles, int[] positions) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.fragments.addAll(Arrays.asList(fragments));
        this.titles = new ArrayList<>();
        if (null != titles) {
            this.titles.addAll(Arrays.asList(titles));
        }
        if (null != positions) {
            this.positions = positions;
        } else {
            this.positions = new int[fragments.length];
            for (int i = 0; i < fragments.length; this.positions[i] = i, i++) ;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return !titles.isEmpty() ? titles.get(positions[position]) : super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(positions[position]);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
