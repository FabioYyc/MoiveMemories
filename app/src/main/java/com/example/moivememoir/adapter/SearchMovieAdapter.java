package com.example.moivememoir.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moivememoir.R;
import com.example.moivememoir.entities.Movie;
import com.example.moivememoir.ui.fragments.MovieViewFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class SearchMovieAdapter extends RecyclerView.Adapter
        <SearchMovieAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // ViewHolder should contain variables for all the views in each row of the
        public TextView tvMovieName;
        public TextView tvReleaseYear;
        public ImageView posterView;
        private final Context context;
        // a constructor that accepts the entire View (itemView)
        // provides a reference and access to all the views in each row
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvMovieName = itemView.findViewById(R.id.movieName);
            tvReleaseYear= itemView.findViewById(R.id.releaseYear);
            posterView=itemView.findViewById(R.id.posterView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);


        }
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            int position = getAdapterPosition();
            Movie movieObj =  movieList.get(position);
            Gson gson = new Gson();
//            intent.putExtra("movieObject", gson.toJson(movieObj));
//            intent.putExtra("test", "test str");
//            context.startActivity(intent);
            bundle.putString("movieJson", gson.toJson(movieObj));
            MovieViewFragment fragment = new MovieViewFragment();
            fragment.setArguments(bundle);
            replaceFragment(fragment, v);
        }

    }
    private void replaceFragment(Fragment nextFragment, View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        if(movieList!=null)return movieList.size();
        return 0;

    }
    public List<Movie> movieList;

    // Pass in the contact array into the constructor
    public SearchMovieAdapter(List<Movie> movies) {
        movieList = movies;
    }

//    public void addUnits(List<CourseResult> units) {
//        courseResults = units;
//        notifyDataSetChanged();
//    }
//    //This method creates a new view holder that is constructed with a new View, inflated
//    from a layout
    @Override
    public SearchMovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the view from an XML layout file
        View moviesView = inflater.inflate(R.layout.rv_layout, parent, false);
        // construct the viewholder with the new view
        ViewHolder viewHolder = new ViewHolder(moviesView);
        return viewHolder;
    }
    // this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull SearchMovieAdapter.ViewHolder viewHolder,
                                 int position) {
        final Movie movie = movieList.get(position);
        // viewholder binding with its data at the specified position
        TextView tvMovieName= viewHolder.tvMovieName;
        tvMovieName.setText(movie.getName());
        TextView tvReleaseYear = viewHolder.tvReleaseYear;
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        String dateStr = dateFormat.format(movie.getReleaseDate());
        tvReleaseYear.setText(dateStr);
        ImageView posterView=viewHolder.posterView;
        String url = movie.getImageLink();


        Picasso.get().load(url).into(posterView);
      // todo: set poster to the image url using Picasso

    }
}