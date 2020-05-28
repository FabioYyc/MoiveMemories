package com.example.moivememoir.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.moivememoir.dao.WatchListDAO;
import com.example.moivememoir.database.WatchListDB;
import com.example.moivememoir.entities.MovieToWatch;

import java.util.List;

public class WatchListRepo {
    private WatchListDAO dao;
    private LiveData<List<MovieToWatch>> watchList;
    private MovieToWatch movieToWatch;

    public WatchListRepo(Application application) {
        WatchListDB db = WatchListDB.getInstance(application);
        dao = db.watchListDAO();
    }

    public LiveData<List<MovieToWatch>> getWatchList() {
        watchList=dao.getAll();
        return watchList;
    }
    public void insert(final MovieToWatch movie){
        WatchListDB.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(movie);
            }
        });
    }
    public void deleteAll(){
        WatchListDB.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }

    public void delete(final MovieToWatch movie){
        WatchListDB.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(movie);
            }
        });
    }

    public void insertAll(final MovieToWatch... movie){
        WatchListDB.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(movie);
            }
        });
    }

    public MovieToWatch findByID(final int movieId){
        WatchListDB.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                MovieToWatch runMovie= dao.findByID(movieId);
                setMovieToWatch(runMovie);
            }
        });
        return movieToWatch;
    }

    public void setMovieToWatch(MovieToWatch movie){
        this.movieToWatch=movie;
    }


}
