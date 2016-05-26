package com.example.roman.wellnesscoach.Voting;

/**
 * Created by Roman on 22.04.2016.
 */
public class VotingBar {

    private float ratingStar;
    private String name;
    private String property;

    public VotingBar(int ratingStar, String name, String country) {
        this.ratingStar = ratingStar;
        this.name = name;
        this.property = country;
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

    public String getProperty() {
        return property;
    }

    public void setProperty(String country) {
        this.property = country;
    }
}