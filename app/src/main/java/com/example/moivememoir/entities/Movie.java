package com.example.moivememoir.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Movie implements Parcelable {
    private String name;
    private Date releaseDate;
    private String detail;

    protected Movie(Parcel in) {
        name = in.readString();
        detail = in.readString();
        imageLink = in.readString();
        genre = in.readString();
        rating = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    private String imageLink;
    private String genre;
    private int rating;

    public Movie(String name, Date releaseDate) {
        this.name = name;
        this.releaseDate = releaseDate;
    }

    public Movie(String name, Date releaseDate, String detail, String imageLink) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.detail = detail;
        this.imageLink = imageLink;

    }

    public Movie() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(releaseDate);

        parcel.writeInt(rating);
        parcel.writeString(name);
        parcel.writeString(strDate);
        parcel.writeString(detail);
        parcel.writeString(imageLink);
        parcel.writeString(genre);

    }
}
