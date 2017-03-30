package com.kingmonkey.jiovany.popularmovies2;

import java.util.ArrayList;

/**
 * Created by jiovany on 3/29/17.
 */

public interface OnRequestFinish<T> {
    void onSuccess(ArrayList<T> response);

    void onFailure(int errorMessageResId);
}
