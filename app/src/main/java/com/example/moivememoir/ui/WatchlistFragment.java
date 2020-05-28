package com.example.moivememoir.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moivememoir.R;
import com.example.moivememoir.entities.Person;
import com.example.moivememoir.viewModel.WatchListViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class WatchlistFragment extends Fragment {
    private TextView tvWatchlist;
    private WatchListViewModel watchListViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);
        tvWatchlist = view.findViewById(R.id.testWatchlist);
        watchListViewModel = new ViewModelProvider(getActivity()).get(WatchListViewModel.class);

        return view;


    }


}
