package com.example.moivememoir.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moivememoir.entities.MovieToWatch;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WatchListDAO {
    @Query("SELECT * FROM movieToWatch")
   LiveData <List<MovieToWatch>> getAll();

    @Query("SELECT * FROM movietowatch WHERE movieId = :id LIMIT 1")
    MovieToWatch findByID(int id);

    @Insert
    void insertAll(MovieToWatch... movies);
    @Insert
    long insert(MovieToWatch movie);
    @Delete
    void delete(MovieToWatch movie);
//
//    @Update(onConflict = REPLACE)
//    void updateCustomers(int REPLACE, MovieToWatch... movies);


    @Query("DELETE FROM movietowatch")
    void deleteAll();
}
