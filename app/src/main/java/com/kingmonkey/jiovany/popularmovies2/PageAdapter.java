package com.kingmonkey.jiovany.popularmovies2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

/**
 * Created by jiovany on 3/27/17.
 */

public class PageAdapter extends FragmentPagerAdapter {
    SparseArray<Fragment> pagerFragments;

    public PageAdapter(FragmentManager fm, SparseArray<Fragment> fragments) {
        super(fm);
        pagerFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return pagerFragments.get(position);
    }

    @Override
    public int getCount() {
        return pagerFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Highest Rated";
    }
}
