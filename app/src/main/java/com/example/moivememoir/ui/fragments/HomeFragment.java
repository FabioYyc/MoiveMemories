package com.example.moivememoir.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.moivememoir.R;
import com.example.moivememoir.entities.Person;
import com.example.moivememoir.helpers.RestHelper;
import com.example.moivememoir.ui.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    private TextView tvGreeting;
    private TextView tvDate;
    private RestHelper restHelper = new RestHelper();
    List<HashMap<String, String>> movieListArray = new ArrayList<HashMap<String, String>>();

    SimpleAdapter myListAdapter;
    ListView movieList;

    String[] colHEAD = new String[]{"NAME", "RELEASE YEAR", "RATING"};
    int[] dataCell = new int[]{R.id.tvMemoirMovieName, R.id.tvMemoirMovieReleaseYear, R.id.rating};


    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            MainActivity activity = (MainActivity) getActivity();
            Person user = activity.getUser();
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
            String curDate = dateFormat.format(date);

            View view = inflater.inflate(R.layout.home_main, container, false);
            tvGreeting = view.findViewById(R.id.tvGreeting);
            tvDate = view.findViewById(R.id.tvDate);
            tvGreeting.setText("Hello " + user.getPersonName());
            tvDate.setText(curDate);

            movieList = view.findViewById(R.id.topMoviesView);

            movieListArray = new ArrayList<HashMap<String, String>>();
            GetMoviesTask getMoviesTask = new GetMoviesTask();
            getMoviesTask.execute(user.getPersonId());

            return view;


        }

    private class GetMoviesTask extends AsyncTask<Integer, Void, List<HashMap<String, String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(Integer... params) {
            List<HashMap<String, String>> myMovieListArray = new ArrayList<HashMap<String, String>>();

            String result = "";

            int personId = params[0];
            result = restHelper.findTop5Movies(personId);
            if (result.equals("failed")) return myMovieListArray;
            try {
                JSONArray array = new JSONArray(result);

                for (int i = 0; i < array.length(); i++) {
                    //String[] colHEAD = new String[]{"NAME", "RELEASE YEAR", "RATING"};
                    JSONObject obj = array.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("NAME", obj.getString("MovieName"));
                    map.put("RELEASE YEAR", obj.getString("MovieReleaseDate"));
                    map.put("RATING", obj.getString("Rating"));
                    myMovieListArray.add(map);


                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

            return myMovieListArray;


        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> myMovieList) {
            Toast toast = Toast.makeText(getActivity(), "message", Toast.LENGTH_LONG);
            if (myMovieList.isEmpty()) {
                toast.setText("Cannot find memoir");
                toast.show();
            }
            else movieListArray = myMovieList;

            myListAdapter = new
                    SimpleAdapter(getActivity(),movieListArray,R.layout.top_movies_list,colHEAD,dataCell);
            movieList.setAdapter(myListAdapter);


        }
    }
}
