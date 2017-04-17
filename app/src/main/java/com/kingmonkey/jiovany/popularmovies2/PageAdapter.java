package com.kingmonkey.jiovany.popularmovies2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

/**
 * Created by jiovany on 3/27/17.
 */

public class PageAdapter extends FragmentPagerAdapter {
    private SparseArray<BaseItemGridFragment> pagerFragments;

    public PageAdapter(FragmentManager fm, SparseArray<BaseItemGridFragment> fragments) {
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
       switch (position){
           case 0:
               return BaseItemGridFragment.FragmentType.MOST_POPULAR;
           case 1:
               return BaseItemGridFragment.FragmentType.HIGHEST_RATED;
           case 2:
               return BaseItemGridFragment.FragmentType.FAVORITES;
           default:
               return null;
       }
    }
}
