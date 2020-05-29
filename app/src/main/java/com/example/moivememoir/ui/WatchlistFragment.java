package com.example.moivememoir.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moivememoir.R;
import com.example.moivememoir.adapter.WatchlistAdapter;
import com.example.moivememoir.entities.MovieToWatch;
import com.example.moivememoir.viewModel.WatchListViewModel;

import java.util.List;

public class WatchlistFragment extends Fragment {
    private TextView tvWatchlist;
    private WatchListViewModel watchListViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private WatchlistAdapter adapter;
    private Button deleteAll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);
        recyclerView = view.findViewById(R.id.watchlistView);
        tvWatchlist = view.findViewById(R.id.watchlistTitle);
        watchListViewModel = new
                ViewModelProvider(getActivity()).get(WatchListViewModel.class);
        deleteAll = view.findViewById(R.id.watchlistDeleteAll);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                watchListViewModel.deleteAll();
            }
        });


        watchListViewModel.getWatchlist().observe(getViewLifecycleOwner(), new Observer<List<MovieToWatch>>() {
            @Override
            public void onChanged(List<MovieToWatch> movieToWatches) {

                //todo: add recycler view
                adapter = new WatchlistAdapter(movieToWatches);
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                        LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(new WatchlistAdapter(movieToWatches));
                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);

            }
        });

        return view;


    }


}
