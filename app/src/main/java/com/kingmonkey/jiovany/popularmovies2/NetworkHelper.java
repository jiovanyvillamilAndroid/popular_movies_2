package com.kingmonkey.jiovany.popularmovies2;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkHelper {
    private OnRequestFinish networkListener;
    private OkHttpClient okHttpClient;
    private Gson gson;

    public NetworkHelper(OnRequestFinish networkListener) {
        this.networkListener = networkListener;
        okHttpClient = new OkHttpClient();
        gson = new Gson();
    }

    public void getMoviesData(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                networkListener.onFailure(R.string.response_error_message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        networkListener.onSuccess(parseMoviesFromJSON(responseString));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        networkListener.onFailure(R.string.response_error_message);
                    }
                } else {
                    networkListener.onFailure(R.string.response_error_message);
                }
            }
        });
    }

    private ArrayList<Movie> parseMoviesFromJSON(String jsonString) throws JSONException {
        ArrayList<Movie> movieData = new ArrayList<>();
        JSONObject responseJSON = new JSONObject(jsonString);
        JSONArray resultsArray = responseJSON.getJSONArray(Constants.RESULTS_JSON_PARAMETER);
        for (int i = 0; i < resultsArray.length(); i++) {
            Movie movie = gson.fromJson(resultsArray.getJSONObject(i).toString(), Movie.class);
            movieData.add(movie);
        }
        return movieData;
    }
}
