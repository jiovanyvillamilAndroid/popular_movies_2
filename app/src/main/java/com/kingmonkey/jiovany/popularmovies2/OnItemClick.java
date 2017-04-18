package com.kingmonkey.jiovany.popularmovies2;

import android.view.View;

import com.kingmonkey.jiovany.popularmovies2.model.Movie;

/**
 * Created by jiovany on 3/29/17.
 */

public interface OnItemClick {
    void onItemClick(Movie movie, View moviePoster);
}
