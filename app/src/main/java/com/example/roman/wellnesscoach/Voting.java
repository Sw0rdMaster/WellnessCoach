package com.example.roman.wellnesscoach;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by Roman on 20.04.2016.
 */
public class Voting extends ListActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_treatment);
    }

}
