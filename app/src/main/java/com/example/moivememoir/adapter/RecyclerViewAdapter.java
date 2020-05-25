package com.example.moivememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moivememoir.R;
import com.example.moivememoir.entities.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter
        <RecyclerViewAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder should contain variables for all the views in each row of the
        public TextView tvMovieName;
        public TextView tvReleaseYear;
        public ImageView posterView;
        // a constructor that accepts the entire View (itemView)
        // provides a reference and access to all the views in each row
        public ViewHolder(View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.movieName);
            tvReleaseYear= itemView.findViewById(R.id.releaseYear);
            posterView=itemView.findViewById(R.id.posterView);
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
    public List<Movie> movieList;

    // Pass in the contact array into the constructor
    public RecyclerViewAdapter(List<Movie> movies) {
        movieList = movies;
    }

//    public void addUnits(List<CourseResult> units) {
//        courseResults = units;
//        notifyDataSetChanged();
//    }
//    //This method creates a new view holder that is constructed with a new View, inflated
//    from a layout
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
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
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder,
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