package com.example.moivememoir.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moivememoir.R;
//import com.example.moivememoir.adapter.MemoirAdapter;
import com.example.moivememoir.entities.Memoir;
import com.example.moivememoir.entities.Movie;
import com.example.moivememoir.helpers.FragmentHelper;
import com.example.moivememoir.helpers.RestHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MemoirFragment extends Fragment {
    private RestHelper restHelper;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Memoir> memoirList;
    private List<Movie> movieList; // it is going to align with memoirList by position
    String apiKey;
    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memoir, container, false);
        recyclerView = view.findViewById(R.id.rv_memoir);
        apiKey = getString(R.string.movie_db_api_key);
        GetMemoirs getMemoirs = new GetMemoirs();
        displayLoader("Fetching Memoir data");
        getMemoirs.execute();

        return view;
    };


    private void displayLoader(String message) {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(message);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    //put the adapter inside this fragment so it can access the async task
    public class MemoirAdapter extends RecyclerView.Adapter
            <MemoirAdapter.ViewHolder> {


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            // ViewHolder should contain variables for all the views in each row of the
            public TextView tvMovieName;
            public TextView tvMemoirReleaseYear;
            public TextView tvMemoirComment;
            public TextView tvMemoirWatchedDate;
            public ImageView memoirPosterView;
            public RatingBar ratingBar;
            public Movie movie;
            //        public ImageView posterView;
            private final Context context;

            // a constructor that accepts the entire View (itemView)
            // provides a reference and access to all the views in each row
            public ViewHolder(View itemView) {
                super(itemView);
                context = itemView.getContext();
                tvMovieName = itemView.findViewById(R.id.tvMemoirMovieName);
                tvMemoirReleaseYear = itemView.findViewById(R.id.tvMemoirMovieReleaseYear);
                tvMemoirComment = itemView.findViewById(R.id.tvMemoirComment);
                tvMemoirWatchedDate = itemView.findViewById(R.id.tvMemoirWatchedDate);
                ratingBar = itemView.findViewById(R.id.memoirRatingBar);
                memoirPosterView = itemView.findViewById(R.id.memoirPosterView);
//            posterView=itemView.findViewById(R.id.posterView);
                itemView.setClickable(true);
                itemView.setOnClickListener(this);


            }

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                int position = getAdapterPosition();
                Movie movieObj = movieList.get(position);
                Gson gson = new Gson();
//            intent.putExtra("movieObject", gson.toJson(movieObj));
//            intent.putExtra("test", "test str");
            bundle.putString("movieJson", gson.toJson(movieObj));
                MovieViewFragment fragment = new MovieViewFragment();
                fragment.setArguments(bundle);
                FragmentHelper.replaceFragment(fragment, v);
            }

        }

        @Override
        public int getItemCount() {
            if (memoirList != null) return memoirList.size();
            return 0;

        }

        public List<Memoir> memoirList;
        public List<Movie> movieList;

        // Pass in the contact array into the constructor
        public MemoirAdapter(List<Memoir> memoirs, List<Movie> movies) {
            memoirList = memoirs;
            movieList = movies;
        }

        //    public void addUnits(List<CourseResult> units) {
//        courseResults = units;
//        notifyDataSetChanged();
//    }
//    //This method creates a new view holder that is constructed with a new View, inflated
//    from a layout
        @Override
        public MemoirAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            // Inflate the view from an XML layout file
            View memoirView = inflater.inflate(R.layout.rv_memoir, parent, false);
            // construct the viewholder with the new view
            ViewHolder viewHolder = new ViewHolder(memoirView);
            return viewHolder;
        }

        // this method binds the view holder created with data that will be displayed
        @Override
        public void onBindViewHolder(@NonNull MemoirAdapter.ViewHolder viewHolder,
                                     int position) {
            final Memoir memoir = memoirList.get(position);
            final Movie movie = movieList.get(position);
            // viewholder binding with its data at the specified position
            TextView tvMovieName = viewHolder.tvMovieName;

            tvMovieName.setText(memoir.getMemoirMovieName());
            TextView tvReleaseYear = viewHolder.tvMemoirReleaseYear;

            String dateStr = memoir.getMemoirMovieReleaseDate().substring(0,10);
            tvReleaseYear.setText(dateStr);

            TextView tvMemoirComment = viewHolder.tvMemoirComment;
            tvMemoirComment.setText(memoir.getMemoirComment());

            TextView tvWatchedDate =viewHolder.tvMemoirWatchedDate;
//        DateFormat watchDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String watchDateStr = memoir.getMemoirWatchDatetime().substring(0,10);
            tvWatchedDate.setText(watchDateStr);

            RatingBar ratingBar = viewHolder.ratingBar;
            ratingBar.setRating(memoir.getMemoirRating());

            if(movie != null) {
                ImageView posterView = viewHolder.memoirPosterView;
                String url = movie.getImageLink();
                Picasso.get().load(url).into(posterView);
            }



//        ImageView posterView=viewHolder.posterView;
//        String url = movie.getImageLink();
//
//
//        Picasso.get().load(url).into(posterView);
            // todo: set poster to the image url using Picasso

        }
    }


    private class GetMemoirs extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            restHelper = new RestHelper();
            return restHelper.getMemoirs();
        }

        @Override
        protected void onPostExecute(String result) {
          if(result != "failed"){
                Gson gson = new Gson();
              Type memoirListType = new TypeToken<ArrayList<Memoir>>(){}.getType();

              memoirList = gson.fromJson(result, memoirListType);
              //wait for memoirList to be initalized
              SearchMovieTask searchMovieTask = new SearchMovieTask();
              searchMovieTask.execute();




          }

        }
    }
    private class SearchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {
            List<Movie> movieList = new ArrayList<>();

           for(Memoir m : memoirList){
               List<Movie> retMovies = restHelper.movieSearch(m.getMemoirMovieName(), apiKey);
               if(!retMovies.isEmpty()) movieList.add(retMovies.get(0));
               else movieList.add(null);

           }


            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            //get the top result;
                movieList=result;
                pDialog.dismiss();
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                    LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(new MemoirAdapter(memoirList, movieList));
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            }

        }
    }


