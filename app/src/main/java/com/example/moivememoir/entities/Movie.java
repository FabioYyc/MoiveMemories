package com.example.moivememoir.entities;

import java.util.Date;

public class Movie {
    private String name;
    private Date releaseDate;
    private String detail;
    private String imageLink;

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
}
