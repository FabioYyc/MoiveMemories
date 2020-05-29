package com.example.moivememoir.viewModel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moivememoir.entities.MovieToWatch;
import com.example.moivememoir.repository.WatchListRepo;

import java.util.List;

public class WatchListViewModel extends ViewModel {
    private WatchListRepo mRepo;
    private MutableLiveData<List<MovieToWatch>> watchList;

    public WatchListViewModel() {
        watchList = new MutableLiveData<>();
    }

    public void setWatchList(List<MovieToWatch> movies) {
        watchList.setValue(movies);
    }

    public LiveData<List<MovieToWatch>> getWatchlist() {
        return mRepo.getWatchList();
    }

    public void initalizeVars(Application application) {
        mRepo = new WatchListRepo(application);
    }

    public void insert(MovieToWatch movie) {
        mRepo.insert(movie);
    }

    public void insertAll(MovieToWatch... movies) {
        mRepo.insertAll(movies);
    }

    public void delete(MovieToWatch movie) {
        mRepo.delete(movie);
    }
    public MovieToWatch findByID(int movieId){
        return mRepo.findByID(movieId);
    }
}

