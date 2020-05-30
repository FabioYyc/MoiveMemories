package com.example.moivememoir.entities;

public class Memoir {
    private int memoirId;
    private String memoirComment;
    private String memoirMovieName;
    private String memoirMovieReleaseDate;
    private float memoirRating;
    private String memoirWatchDatetime;
    private Cinema cinemaId;
    private Person personId;

    public Memoir() {

    }

    public Memoir(int memoirId, String memoirComment, String memoirMovieName, String memoirMovieReleaseYear,
                  float memoirRating, String memoirWatchDateTime, Cinema cinemaId, Person personId) {
        this.memoirId = memoirId;
        this.memoirComment = memoirComment;
        this.memoirMovieName = memoirMovieName;
        this.memoirMovieReleaseDate = memoirMovieReleaseYear;
        this.memoirRating = memoirRating;
        this.memoirWatchDatetime = memoirWatchDateTime;
        this.cinemaId = cinemaId;
        this.personId = personId;
    }

    public int getMemoirId() {
        return memoirId;
    }

    public void setMemoirId(int memoirId) {
        this.memoirId = memoirId;
    }

    public String getMemoirComment() {
        return memoirComment;
    }

    public void setMemoirComment(String memoirComment) {
        this.memoirComment = memoirComment;
    }


    public void setMemoirMovieReleaseDate(String memoirMovieReleaseDate) {
        this.memoirMovieReleaseDate = memoirMovieReleaseDate;
    }

    public String getMemoirMovieReleaseDate() {
        return memoirMovieReleaseDate;
    }

    public void setMemoirMovieName(String memoirMovieName) {
        this.memoirMovieName = memoirMovieName;
    }

    public float getMemoirRating() {
        return memoirRating;
    }

    public void setMemoirRating(float memoirRating) {
        this.memoirRating = memoirRating;
    }

    public String getMemoirWatchDateTime() {
        return memoirWatchDatetime;
    }

    public void setMemoirWatchDateTime(String memoirWatchDateTime) {
        this.memoirWatchDatetime = memoirWatchDateTime;
    }

    public Cinema getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Cinema cinemaId) {
        this.cinemaId = cinemaId;
    }

    public Person getPersonId() {
        return personId;
    }

    public void setPersonId(Person personId) {
        this.personId = personId;
    }
}
