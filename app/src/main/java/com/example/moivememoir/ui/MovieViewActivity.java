package com.example.moivememoir.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.moivememoir.entities.Movie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
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
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieViewActivity extends AppCompatActivity {
    private Movie movie;
    private TextView tvMovieName;
    private TextView tvMovieYear;
    private TextView tvMovieGenre;
    private TextView tvMovieDetails;
    private TextView tvMovieCast;
    private TextView tvMovieCountry;
    private TextView tvMovieDirector;
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
        tvMovieCast = findViewById(R.id.movieCast);
        tvMovieCountry = findViewById(R.id.movieCountry);
        tvMovieDirector = findViewById(R.id.movieDirector);


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
        ratingBar.setRating(movie.getRating());

        GetMovieDetails getMovieDetails = new GetMovieDetails();
        String apiKey = getString(R.string.movie_db_api_key);
        //set genre, country, director and cast
        getMovieDetails.execute(apiKey);

        String url = movie.getImageLink();
        Picasso.get().load(url).into(imageView);

    }

    private class GetMovieDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            String baseUrl = "https://api.themoviedb.org/3/movie/" + movie.getId() + "?" + "api_key=";

            String apiKey = params[0];
            String url = baseUrl + apiKey + "&append_to_response=credits";
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
                JSONArray genreArray;
                JSONArray countryArray;
                JSONArray castArray;
                JSONArray crewArray;
                String director;
                String countryStr;
                String castStr;
                String genreString;

                try {
                    JSONObject returnJson = new JSONObject(result);
                    genreArray = returnJson.getJSONArray("genres");
                    countryArray = returnJson.getJSONArray("production_countries");
                    castArray = returnJson.getJSONObject("credits").getJSONArray("cast");
                    crewArray = returnJson.getJSONObject("credits").getJSONArray("crew");

                    genreString = turnJsonArrayToString(genreArray, "name");
                    countryStr= turnJsonArrayToString(countryArray,"name");
                    castStr = turnJsonArrayToString(castArray,"name");

                    tvMovieGenre.setText(genreString);
                    tvMovieCast.setText(castStr);
                    tvMovieCountry.setText(countryStr);
                    //find the director
                    for (int i = 0; i < crewArray.length(); i++) {
                        JSONObject obj = crewArray.getJSONObject(i);
                        if(obj.getString("job").equals("Director")) tvMovieDirector
                                .setText(obj.getString("name"));
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }


    }

    private String turnJsonArrayToString(JSONArray jsonArray, String keyName) throws JSONException {
        List<String> stringArray=  new ArrayList<>();
        for (int i = 0; i < jsonArray.length() && i<=5; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            stringArray.add(obj.getString(keyName));

        }
        String retString = TextUtils.join(", ", stringArray);
        return retString;
    }
}
