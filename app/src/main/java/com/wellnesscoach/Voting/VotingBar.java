package com.wellnesscoach.Voting;

import android.text.SpannableStringBuilder;

/**
 * Created by Roman on 22.04.2016.
 */
public class VotingBar {

    private float ratingStar;
    private SpannableStringBuilder name;
    private String property;

    public VotingBar(int ratingStar, SpannableStringBuilder name, String country) {
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

    public SpannableStringBuilder getName() {
        return name;
    }

    public void setName(SpannableStringBuilder name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String country) {
        this.property = country;
    }
}