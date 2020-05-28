package com.example.moivememoir.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.moivememoir.dao.WatchListDAO;
import com.example.moivememoir.entities.MovieToWatch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

@Database(entities = {MovieToWatch.class}, version = 2, exportSchema = false)
public abstract class WatchListDB extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract WatchListDAO watchListDAO();
    private static WatchListDB INSTANCE;
    public static synchronized WatchListDB getInstance(final Context
                                                                    context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    WatchListDB.class, "WatchListDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
