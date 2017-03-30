package com.kingmonkey.jiovany.popularmovies2;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jiovany on 3/29/17.
 */

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MoviesViewHolder> {
    private List<Movie> moviesData;
    private OnItemClick onItemClickListener;

    public MoviesGridAdapter(OnItemClick onItemClick) {
        onItemClickListener = onItemClick;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutItemId = R.layout.movie_item;
        View inflatedView = LayoutInflater.from(context).inflate(layoutItemId, parent, false);
        return new MoviesViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        holder.bindData(moviesData.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return moviesData != null ? moviesData.size() : 0;
    }

    public void setMoviesData(ArrayList<Movie> moviesData) {
        this.moviesData = moviesData;
        notifyDataSetChanged();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_poster_iv)
        ImageView moviePoster;
        @BindView(R.id.vote_average_tv)
        TextView voteAverage;
        @BindView(R.id.movie_title_tv)
        TextView movieTitle;
        @BindView(R.id.movie_poster_container)
        CardView moviePosterContainer;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(final Movie movie, final OnItemClick listener) {
            Glide.with(moviePoster.getContext().getApplicationContext())
                    .load(movie.getPosterCompleteUrl(false)).centerCrop().crossFade().into(moviePoster);
            movieTitle.setText(movie.getTitle());
            voteAverage.setText(String.valueOf(movie.getVoteAverage()));
            moviePosterContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }

    }
}
