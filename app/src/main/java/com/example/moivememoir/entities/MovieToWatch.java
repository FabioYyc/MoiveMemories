package com.example.moivememoir.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class MovieToWatch {
    @PrimaryKey(autoGenerate = true)
    public int movieId;
    @ColumnInfo(name = "movie_name")
    public String name;
    @ColumnInfo(name = "added_date")
    public String addedDate;
    @ColumnInfo(name = "release_date")
    public String releaseDate;
    @ColumnInfo(name = "movieDB_id")
    public int movieDBId;

    public MovieToWatch() {

    }

    public MovieToWatch(int movieId, String name, String addedDate, String releaseDate, int movieDBId) {
        this.movieId = movieId;
        this.name = name;
        this.addedDate = addedDate;
        this.releaseDate = releaseDate;
        this.movieDBId = movieDBId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public int getMovieDBId() {
        return movieDBId;
    }

    public void setMovieDBId(int movieDBId) {
        this.movieDBId = movieDBId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
