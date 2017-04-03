package com.kingmonkey.jiovany.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kingmonkey.jiovany.popularmovies2.BaseItemGridFragment.FragmentType.HIGHEST_RATED;
import static com.kingmonkey.jiovany.popularmovies2.BaseItemGridFragment.FragmentType.MOST_POPULAR;

/**
 * Created by jiovany on 3/27/17.
 */

public class BaseItemGridFragment extends Fragment implements OnItemClick,SwipeRefreshLayout.OnRefreshListener,OnRequestFinish<Movie> {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HIGHEST_RATED, MOST_POPULAR})
    public @interface FragmentType {
        int HIGHEST_RATED = 7777;
        int MOST_POPULAR = 8888;
    }

    public static String FRAGMENT_TYPE_KEY = "fragment_type_key";
    private int currentFragmentType;
    private MoviesGridAdapter moviesAdapter;
    private NetworkHelper networkHelper;


    @BindView(R.id.rv_movies)
    RecyclerView recyclerViewMovies;
    @BindView(R.id.srl_movies)
    SwipeRefreshLayout swipeRefreshLayout;

    public static BaseItemGridFragment newInstance(@FragmentType int fragmentType) {
        BaseItemGridFragment fragment = new BaseItemGridFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt(FRAGMENT_TYPE_KEY, fragmentType);
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
            currentFragmentType = args.getInt(FRAGMENT_TYPE_KEY);
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
        networkHelper = new NetworkHelper(this);
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
        String typeName = currentFragmentType == HIGHEST_RATED ? Constants.HIGHEST_RATED : Constants.MOST_POPULAR;
        String urlToGetData = Constants.BASE_URL_MOVIES_DATA.concat(typeName).concat(Constants.API_KEY);
        networkHelper.getMoviesData(urlToGetData);
    }

    @Override
    public void onItemClick(Movie movie, View moviePoster) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), moviePoster, "movie_poster");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onSuccess(final ArrayList<Movie> response) {
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
}
