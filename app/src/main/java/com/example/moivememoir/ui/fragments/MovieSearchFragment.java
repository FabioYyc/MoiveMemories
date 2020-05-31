package com.example.moivememoir.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moivememoir.R;
import com.example.moivememoir.adapter.SearchMovieAdapter;
import com.example.moivememoir.entities.Movie;
import com.example.moivememoir.helpers.RestHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieSearchFragment extends Fragment {


    private EditText etSearch;
    private Button searchButton;
    private List<Movie> movieList;
    private RestHelper restHelper;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchMovieAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_search, container, false);
        recyclerView = view.findViewById(R.id.movieRv);
        etSearch = view.findViewById(R.id.searchName);
        searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                String searchName = etSearch.getText().toString();
                SearchMovieTask searchMovieTask = new SearchMovieTask();
                searchMovieTask.execute(searchName);
            }
        });


        // Inflate the layout for this fragment

        return view;
    }

    private class SearchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {
            restHelper = new RestHelper();
            List<Movie> retMovieList = new ArrayList<Movie>();
            String apiKey = getString(R.string.movie_db_api_key);

            String result = "";
            String searchName = params[0];

            if (searchName.equals("test")) {//so we dont have to call the actual api every time
                Movie movie1 = new Movie();
                movie1.setName("Jake Reacher");
                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2010-10-01");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                movie1.setReleaseDate(date1);
                movie1.setDetail("Jack Reacher must uncover the truth behind a major government conspiracy " +
                        "in order to clear his name. On the run as a fugitive from the law, " +
                        "Reacher uncovers a potential secret from his past that could change his life forever.");
                movie1.setImageLink("https://image.tmdb.org/t/p/w500/4ynQYtSEuU5hyipcGkfD6ncwtwz.jpg");
                movie1.setRating(4.8f/2);
                movie1.setId(343611);
                retMovieList.add(movie1);
                return retMovieList;

            }

            retMovieList = restHelper.movieSearch(searchName, apiKey);
            return retMovieList;


        }

        @Override
        protected void onPostExecute(List<Movie> myMovieList) {
            Toast toast = Toast.makeText(getActivity(), "message", Toast.LENGTH_LONG);
            if (myMovieList.isEmpty()) {
                toast.setText("Cannot find movie");
                toast.show();
            } else movieList = myMovieList;
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                    LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(new SearchMovieAdapter(movieList));
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);


        }
    }

}
