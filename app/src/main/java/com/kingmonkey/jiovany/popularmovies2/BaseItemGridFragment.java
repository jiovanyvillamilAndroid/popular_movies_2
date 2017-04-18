package com.kingmonkey.jiovany.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kingmonkey.jiovany.popularmovies2.model.Movie;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kingmonkey.jiovany.popularmovies2.BaseItemGridFragment.FragmentType.FAVORITES;
import static com.kingmonkey.jiovany.popularmovies2.BaseItemGridFragment.FragmentType.HIGHEST_RATED;
import static com.kingmonkey.jiovany.popularmovies2.BaseItemGridFragment.FragmentType.MOST_POPULAR;

/**
 * Created by jiovany on 3/27/17.
 */

public class BaseItemGridFragment extends Fragment implements OnItemClick, SwipeRefreshLayout.OnRefreshListener {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({HIGHEST_RATED, MOST_POPULAR, FAVORITES})
    public @interface FragmentType {
        String HIGHEST_RATED = "HIGHEST RATED";
        String MOST_POPULAR = "MOST POPULAR";
        String FAVORITES = "FAVORITES";
    }

    public static String FRAGMENT_TYPE_KEY = "fragment_type_key";
    private String currentFragmentType;
    private MoviesGridAdapter moviesAdapter;
    private NetworkHelper networkHelper;


    @BindView(R.id.rv_movies)
    RecyclerView recyclerViewMovies;
    @BindView(R.id.srl_movies)
    SwipeRefreshLayout swipeRefreshLayout;

    public static BaseItemGridFragment newInstance(@FragmentType String fragmentType) {
        BaseItemGridFragment fragment = new BaseItemGridFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(FRAGMENT_TYPE_KEY, fragmentType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getArgs();
        View view = inflater.inflate(R.layout.base_item_grid_fragment, container, false);
        ButterKnife.bind(this, view);
        initFragment();
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void getArgs() {
        Bundle args = getArguments();
        if (args != null) {
            currentFragmentType = args.getString(FRAGMENT_TYPE_KEY);
        } else {
            throw new RuntimeException("Please create this fragment using newInstance(@FragmentType int fragmentType) method.");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initFragment() {
        swipeRefreshLayout.setRefreshing(true);
        networkHelper = new NetworkHelper();
        onRefresh();
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        moviesAdapter = new MoviesGridAdapter(this);
        recyclerViewMovies.setAdapter(moviesAdapter);
        recyclerViewMovies.hasFixedSize();
        recyclerViewMovies.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onRefresh() {
        if (currentFragmentType.equals(FAVORITES)) {
            //TODO: retrieve data from Content Provider
        } else {
            String typeName = currentFragmentType.equals(HIGHEST_RATED) ? Constants.HIGHEST_RATED : Constants.MOST_POPULAR;
            String urlToGetData = Constants.BASE_URL_MOVIES_DATA.concat(typeName).concat(Constants.API_KEY);
            networkHelper.getMoviesData(urlToGetData, new OnRequestFinish() {
                @Override
                public void onSuccess(final ArrayList response) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            if (response != null) {
                                moviesAdapter.setMoviesData(response);
                                recyclerViewMovies.smoothScrollToPosition(0);
                            } else {
                                onFailure(R.string.response_empty_message);
                            }
                        }
                    });
                }

                @Override
                public void onFailure(final int errorMessageResId) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getContext(), getString(errorMessageResId), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onItemClick(Movie movie, View moviePoster) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_KEY, movie);
        startActivity(intent);
    }

}
