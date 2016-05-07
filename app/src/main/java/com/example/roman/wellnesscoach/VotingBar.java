package com.example.roman.wellnesscoach;

/**
 * Created by Roman on 22.04.2016.
 */
public class VotingBar {

    private float ratingStar;
    private String name;
    private String country;

    public VotingBar(int ratingStar, String name, String country) {
        this.ratingStar = ratingStar;
        this.name = name;
        this.country = country;
    }

    public float getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(float ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}