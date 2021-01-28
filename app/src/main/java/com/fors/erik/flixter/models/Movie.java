package com.fors.erik.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {
    int movieID;
    String backdropPath;
    String postPath;
    String title;
    String overview;
    String releaseDate;
    float rating;

    // empty constructor needed by the Parceler library
    public Movie(){};

    public Movie(JSONObject jsonObject) throws JSONException {
        this.postPath = jsonObject.getString("poster_path");
        this.title = jsonObject.getString("title");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.rating = Float.parseFloat( jsonObject.getString("vote_average"));
        this.movieID = jsonObject.getInt("id");
        this.releaseDate = jsonObject.getString("release_date");
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {

        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < movieJsonArray.length(); i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }

        return movies;
    }

    public float getRating() {
        return rating;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s" ,backdropPath);
    }

    public String getPostPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s" ,postPath);
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public int getMovieID() {
        return movieID;
    }
}
