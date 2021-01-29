package com.fors.erik.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.fors.erik.flixter.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "AIzaSyBQHq0vJxAetModg9Q8OjMFxD6AU1GMQtI";
    private static final String VIDEO_URL =  "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private final String TAG = "DetailActivity";

    TextView tvTitle;
    TextView tvOverview;
    TextView tvReleseDate;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        

        tvOverview = findViewById(R.id.tvdOverview);
        tvTitle = findViewById(R.id.tvdTitle);
        ratingBar = findViewById(R.id.rbRating);
        youTubePlayerView = findViewById(R.id.player);
        tvReleseDate = findViewById(R.id.tvdReleaseDate);


        Intent intent = getIntent();

        final Movie movie = Parcels.unwrap( intent.getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating(movie.getRating());
        tvReleseDate.setText(movie.getReleaseDate());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEO_URL, movie.getMovieID()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if(results.length() == 0)
                        return;
                    String youtube_key = results.getJSONObject(0).getString("key");
                    Log.d(TAG,youtube_key);

                    Boolean popularMovie = false;

                    if(movie.getRating() > 7.0)
                        popularMovie = true;

                    initializeYoutube(youtube_key,popularMovie);

                } catch (JSONException e) {
                    Log.e(TAG,e.toString());
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    private void initializeYoutube(final String youtube_key, final boolean popularMovie) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG,"onInitializationSuccess");
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(youtube_key);

                if(popularMovie)
                youTubePlayer.loadVideo(youtube_key);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG,"onInitializationFailure");
            }
        });

    }


}