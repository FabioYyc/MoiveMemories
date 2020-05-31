package com.example.moivememoir.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.moivememoir.database.WatchListDB;
import com.example.moivememoir.entities.Movie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moivememoir.R;
import com.example.moivememoir.entities.MovieToWatch;
import com.example.moivememoir.entities.Person;
import com.example.moivememoir.helpers.FragmentHelper;
import com.example.moivememoir.helpers.TwitterHelper;
import com.example.moivememoir.ui.activities.RegisterActivity;
import com.example.moivememoir.viewModel.WatchListViewModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import static androidx.core.content.ContextCompat.getSystemService;

public class MovieViewFragment extends Fragment {
    private Movie movie;
    private TextView tvMovieName;
    private TextView tvMovieYear;
    private TextView tvMovieGenre;
    private TextView tvMovieDetails;
    private TextView tvMovieCast;
    private TextView tvMovieCountry;
    private TextView tvMovieDirector;
    TextView tvCheckTwitter;
    private RatingBar ratingBar;
    private ImageView imageView;
    private Button addWatchlist;
    private Button addMemoir;
    private String genreString;
    private WatchListViewModel viewModel;
    private List<twitter4j.Status> tweets;
    TwitterHelper twitterHelper;
    ProgressDialog pDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Gson gson = new Gson();
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_movie_view, container, false);

        //todo: get data from the recycler view
//        Intent mIntent = getIntent();
//        movie = mIntent.getExtras().getParcelable("movieObject");
        tvMovieName = view.findViewById(R.id.movieName);
        imageView = view.findViewById(R.id.moviePoster);
        tvMovieDetails = view.findViewById(R.id.movieDetails);
        tvMovieYear = view.findViewById(R.id.movieYear);
        tvMovieGenre = view.findViewById(R.id.movieGenres);
        ratingBar = view.findViewById(R.id.movieRating);
        tvMovieCast = view.findViewById(R.id.movieCast);
        tvMovieCountry = view.findViewById(R.id.movieCountry);
        tvMovieDirector = view.findViewById(R.id.movieDirector);
        addWatchlist = view.findViewById(R.id.addWatchList);
        addMemoir = view.findViewById(R.id.addMemoir);
        tvCheckTwitter = view.findViewById(R.id.tvCheckTwitter);

        viewModel = new
                ViewModelProvider(getActivity()).get(WatchListViewModel.class);


//        String movieName = movie.getName();
//        etMovieName.setText(movieName);
//        String test = mIntent.getStringExtra("test");
//        String movieJson = mIntent.getStringExtra("movieObject");
        Bundle bundle = this.getArguments();
        String movieJson = "";
        if (bundle != null) {
            movieJson = bundle.getString("movieJson");
            movie = gson.fromJson(movieJson, Movie.class);

        }
        if (movie != null) {
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

            if(movie.getImageLink()!=null) {
                String url = movie.getImageLink();
                Picasso.get().load(url).into(imageView);
            }

//            initAddWatchlistListener();
            initAddMemoirListener();
            int id = movie.getId();
            MovieToWatch movieToWatch = viewModel.findByID(id);
            CheckIfExistTask checkIfExistTask = new CheckIfExistTask();
            checkIfExistTask.execute(movie.getId());
            initTwitterListener();
        }


        return view;

    }

    private void initAddWatchlistListener() {

        addWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MovieToWatch movieToWatch = new MovieToWatch();
                movieToWatch.setName(movie.getName());
                movieToWatch.setMovieDBId(movie.getId());
                movieToWatch.setReleaseDate(movie.getReleaseDate().toString());
                Date date = new Date();
                String dateStr = date.toString();
                movieToWatch.setAddedDate(dateStr);
                viewModel.insert(movieToWatch);
                Toast toast=Toast.makeText(getActivity(),"Movie added to watchlist",Toast.LENGTH_SHORT);
                toast.show();
                addWatchlist.setEnabled(false);
            }
        });


    }

    private void initAddMemoirListener() {

        addMemoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Bundle bundle = new Bundle();
                bundle.putString("movieJson", gson.toJson(movie));
                Fragment nextFragment = new AddToMemoir();
                nextFragment.setArguments(bundle);
                FragmentHelper.replaceFragment(nextFragment, v);
            }
        });


    }

    private void initTwitterListener() {

                tvCheckTwitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isNetworkAvailable()){
                            SearchTwitterTask searchTwitterTask = new SearchTwitterTask();
                            searchTwitterTask.execute(movie.getName()+" movie");
                            displayLoader("Fetching Twitter data...");
                        }
                        else{
                            Toast toast = Toast.makeText(getActivity(),"Network unavailable", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }
                });

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
                    countryStr = turnJsonArrayToString(countryArray, "name");
                    castStr = turnJsonArrayToString(castArray, "name");

                    tvMovieGenre.setText(genreString);
                    tvMovieCast.setText(castStr);
                    tvMovieCountry.setText(countryStr);
                    //find the director
                    for (int i = 0; i < crewArray.length(); i++) {
                        JSONObject obj = crewArray.getJSONObject(i);
                        if (obj.getString("job").equals("Director")) tvMovieDirector
                                .setText(obj.getString("name"));
                    }

                    if(movie.getImageLink()==null){
                        String url = returnJson.getString("backdrop_path");
                        String posterBasePath = "https://image.tmdb.org/t/p/w500";
                        url = posterBasePath+url;
                        movie.setImageLink(url);
                        Picasso.get().load(url).into(imageView);

                    }
                    if(movie.getDetail() == null){
                        String overview = returnJson.getString("overview");
                        movie.setDetail(overview);
                        tvMovieDetails.setText(overview);
                    }
                    if(movie.getRating() <1){
                        float rating = returnJson.getInt("vote_average");
                        movie.setRating(rating);
                        ratingBar.setRating(movie.getRating()/2f);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }


    }

    private String turnJsonArrayToString(JSONArray jsonArray, String keyName) throws JSONException {
        List<String> stringArray = new ArrayList<>();
        for (int i = 0; i < jsonArray.length() && i <= 5; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            stringArray.add(obj.getString(keyName));

        }
        String retString = TextUtils.join(", ", stringArray);
        return retString;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void displayLoader(String message) {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(message);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private class CheckIfExistTask extends AsyncTask<Integer, Void, MovieToWatch> {

        @Override
        protected MovieToWatch doInBackground(Integer... params) {
            int movieDBId = params[0];
            MovieToWatch movie = viewModel.findByID(movieDBId);

            return movie;
        }

        @Override
        protected void onPostExecute(MovieToWatch exist) {
            if(exist != null) addWatchlist.setEnabled(false);
            else initAddWatchlistListener();

        }
    }

    private class SearchTwitterTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String>  doInBackground(String... params) {
            ArrayList<String> tweetText = new ArrayList<String>();

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(getString(R.string.twitter_api_key))
                    .setOAuthConsumerSecret(getString(R.string.twitter_api_secret_key))
                    .setOAuthAccessToken(getString(R.string.twitter_access_token))
                    .setOAuthAccessTokenSecret(getString(R.string.twitter_access_token_secret));

            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();

            String searchText = params[0];
            Query query = new Query(searchText);
            List<twitter4j.Status> tweets = new ArrayList();
            QueryResult result;

            try {
                result = twitter.search(query);
                tweets = result.getTweets();
                for(int i =0; i < tweets.size() && i <10; i++){
                    tweetText.add(tweets.get(i).getText());
                }

            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return tweetText;
        }

        @Override
        protected void onPostExecute(ArrayList<String> tweetText) {
            String result;
            twitterHelper = new TwitterHelper(getContext());
            result = twitterHelper.calculateCategory(tweetText);

            if(result != null){

                tvCheckTwitter.setText("People are " + result + " about this movie");
            }

            pDialog.dismiss();

        }
    }

}


