package com.kingmonkey.jiovany.popularmovies2;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.kingmonkey.jiovany.popularmovies2.BaseItemGridFragment.FragmentType.HIGHEST_RATED;
import static com.kingmonkey.jiovany.popularmovies2.BaseItemGridFragment.FragmentType.MOST_POPULAR;

/**
 * Created by jiovany on 3/27/17.
 */

public class BaseItemGridFragment extends Fragment {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HIGHEST_RATED, MOST_POPULAR})
    public @interface FragmentType {
        int HIGHEST_RATED = 7777;
        int MOST_POPULAR = 8888;
    }

    public static String FRAGMENT_TYPE_KEY = "fragment_type_key";
    private int currentFragmentType;

    public static BaseItemGridFragment newInstance(@FragmentType int fragmentType) {
        BaseItemGridFragment fragment = new BaseItemGridFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt(FRAGMENT_TYPE_KEY, fragmentType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            currentFragmentType = args.getInt(FRAGMENT_TYPE_KEY);
        } else {
            throw new RuntimeException("Please create this fragment using newInstance(@FragmentType int fragmentType) method.");
        }
        return inflater.inflate(R.layout.most_popular_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
