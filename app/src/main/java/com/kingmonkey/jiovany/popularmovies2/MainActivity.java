package com.kingmonkey.jiovany.popularmovies2;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.viewpager)
    ViewPager categoriesViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        initToolbar();
        initPager();
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    private void initPager() {
        SparseArray<Fragment> pages = new SparseArray<>(2);
        pages.put(0,BaseItemGridFragment.newInstance(BaseItemGridFragment.FragmentType.HIGHEST_RATED));
        pages.put(1,BaseItemGridFragment.newInstance(BaseItemGridFragment.FragmentType.MOST_POPULAR));
        categoriesViewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), pages));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(categoriesViewPager);
        categoriesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
