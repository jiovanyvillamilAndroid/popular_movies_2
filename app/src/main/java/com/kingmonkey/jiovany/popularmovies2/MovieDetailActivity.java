package com.kingmonkey.jiovany.popularmovies2;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {
    public static String MOVIE_KEY = "movie_to_show_detail";
    Movie currentMovie;
    @BindView(R.id.cv_movie_poster)
    CardView cardViewPoster;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.iv_movie_poster)
    ImageView poster;
    @BindView(R.id.iv_backdrop)
    ImageView backDrop;
    @BindView(R.id.tv_overview)
    TextView overView;
    @BindView(R.id.tv_release_date)
    TextView releaseDate;
    @BindView(R.id.tv_vote_average)
    TextView voteAverage;
    @BindView(R.id.tv_vote_count)
    TextView voteCount;
    @BindView(R.id.tv_popularity)
    TextView popularity;
    @BindView(R.id.ll_container)
    LinearLayout contentLinearLayout;
    @BindView(R.id.toolbar_subtitle)
    TextView toolbarSubtitle;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.tv_play)
    TextView playIcon;
    @BindView(R.id.view_reviews_btn)
    Button viewReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        getMovieFromIntent();
        initToolbar();
        bindData();
        scalePosterOnCollapse();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(currentMovie.getTitle().toUpperCase());
        }
        toolbarSubtitle.setText(String.valueOf(currentMovie.getVoteAverage()).concat(Constants.BLACK_STAR_UNICODE));
    }

    private void changeActionBarColor(int color) {
        collapsingToolbarLayout.setBackgroundColor(color);
        collapsingToolbarLayout.setContentScrimColor(color);
        collapsingToolbarLayout.setStatusBarScrimColor(color);
    }

    private void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    private void scalePosterOnCollapse() {
        final int initialPosterHeight = cardViewPoster.getLayoutParams().height;
        final int initialPosterWidth = cardViewPoster.getLayoutParams().width;
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percent = (100 - ((verticalOffset) + Constants.COLLAPSED_TOOLBAR_SIZE) * 100 / Constants.COLLAPSED_TOOLBAR_SIZE);
                float newHeight = initialPosterHeight - (initialPosterHeight * (percent / 100));
                float newWidth = initialPosterWidth - (initialPosterWidth * (percent / 100));
                cardViewPoster.getLayoutParams().height = Math.round(newHeight);
                cardViewPoster.getLayoutParams().width = Math.round(newWidth);
                cardViewPoster.requestLayout();
                cardViewPoster.setAlpha((100 - percent) / 100);
                FrameLayout.LayoutParams buttonLayoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(0, (cardViewPoster.getLayoutParams().height / 2), 0, 0);
                contentLinearLayout.setLayoutParams(buttonLayoutParams);
            }
        });
    }

    private void bindData() {
        loadBackDrop();
        Glide.with(this)
                .load(currentMovie.getPosterCompleteUrl(false))
                .centerCrop()
                .into(poster);
        overView.setText(currentMovie.getOverview());
        releaseDate.setText(currentMovie.getReleaseDate());
        voteAverage.setText(String.valueOf(currentMovie.getVoteAverage()).concat(Constants.BLACK_STAR_UNICODE));
        voteCount.setText(String.valueOf(currentMovie.getVoteCount()));
        popularity.setText(String.valueOf(currentMovie.getPopularity()));
        backDrop.setClickable(currentMovie.isVideo());
        playIcon.setVisibility(currentMovie.isVideo() ? View.VISIBLE : View.GONE);
    }

    private void loadBackDrop() {
        Glide.with(this)
                .load(currentMovie.getBackdropCompleteUrl(true)).asBitmap()
                .centerCrop()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.colorPrimary));
                                int darkVibrantColor = palette.getDarkVibrantColor(getResources().getColor(R.color.colorPrimaryDark));
                                changeActionBarColor(darkVibrantColor);
                                changeStatusBarColor(vibrantColor);
                            }
                        });
                        return false;
                    }
                })
                .into(backDrop);
    }


    private void getMovieFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentMovie = (Movie) extras.getSerializable(MOVIE_KEY);
        } else {
            Toast.makeText(this, getString(R.string.response_empty_message), Toast.LENGTH_LONG).show();
        }
    }
}
