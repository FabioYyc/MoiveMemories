package com.example.moivememoir.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.moivememoir.entities.Movie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.moivememoir.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieViewActivity extends AppCompatActivity {
    private Movie movie;
    private TextView tvMovieName;
    private TextView tvMovieYear;
    private TextView tvMovieGenre;
    private TextView tvMovieDetails;
    private RatingBar ratingBar;
    private ImageView imageView;
    private String genreString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Gson gson = new Gson();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent mIntent = getIntent();
//        movie = mIntent.getExtras().getParcelable("movieObject");
        tvMovieName = findViewById(R.id.movieName);
        imageView = findViewById(R.id.moviePoster);
        tvMovieDetails = findViewById(R.id.movieDetails);
        tvMovieYear = findViewById(R.id.movieYear);
        tvMovieGenre = findViewById(R.id.movieGenres);
        ratingBar = findViewById(R.id.movieRating);

//        String movieName = movie.getName();
//        etMovieName.setText(movieName);
        String test = mIntent.getStringExtra("test");
        String movieJson = mIntent.getStringExtra("movieObject");
        movie = gson.fromJson(movieJson, Movie.class);
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        String yearStr = dateFormat.format(movie.getReleaseDate());
        tvMovieYear.setText(yearStr);
        tvMovieName.setText(movie.getName());
        tvMovieDetails.setText(movie.getDetail());
        FindGenreTask findGenreTask = new FindGenreTask();
        String apiKey = getString(R.string.movie_db_api_key);
        findGenreTask.execute(apiKey);
        ratingBar.setRating(movie.getRating());

        String url = movie.getImageLink();
        Picasso.get().load(url).into(imageView);

    }

    private class FindGenreTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            String baseUrl = "https://api.themoviedb.org/3/genre/movie/list?api_key=";
            String apiKey = params[0];
            String url = baseUrl + apiKey + "&language=en-US";
            String result = "failed";

            Request.Builder builder = new Request.Builder();
            builder.url(url);
            Request request = builder.build();
            try {
                Response response = client.newCall(request).execute();
                //if get data failed
                if (response.code() != 200) return result;
                result = response.body().string();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "failed") {
                String genreNames = "";
                genreString = result;
                int[] genreIds = movie.getGenreIds();
                Map<Integer, String> genreMap = new HashMap<Integer, String>();
                try {
                    JSONObject genreJson = new JSONObject(genreString);

                    JSONArray array = genreJson.getJSONArray("genres");
                    //only take the first 5 genre names
                    for (int i = 0; i < array.length() || i < 5; i++) {
                        JSONObject jsonObj = array.getJSONObject(i);
                        genreMap.put(jsonObj.getInt("id"), jsonObj.getString("name"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (genreIds != null || genreIds.length!=0) {
                    for (int genreId : genreIds) {
                        String genreName = genreMap.get(genreId);
                        if (genreName != null) genreNames += genreName + " | ";
                    }
                    tvMovieGenre.setText(genreNames);
                }
            }


        }
    }


}
