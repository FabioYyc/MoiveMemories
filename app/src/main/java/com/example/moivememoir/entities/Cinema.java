package com.example.moivememoir.entities;

public class Cinema {
    private int cinemaId;
    private String cinemaName;
    private String cinemaPostcode;

    public Cinema(int cinemaId, String cinemaName, String cinemaPostcode) {
        this.cinemaId = cinemaId;
        this.cinemaName = cinemaName;
        this.cinemaPostcode = cinemaPostcode;
    }


    public int getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(int cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getCinemaPostcode() {
        return cinemaPostcode;
    }

    public void setCinemaPostcode(String cinemaPostcode) {
        this.cinemaPostcode = cinemaPostcode;
    }
}
