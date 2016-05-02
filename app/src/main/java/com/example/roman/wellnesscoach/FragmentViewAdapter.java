package com.example.roman.wellnesscoach;

import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Roman on 28.04.2016.
 */
public class FragmentViewAdapter extends ArrayAdapter<String> {

    private AppCompatActivity activity;
    private List<String> treatList;

    public FragmentViewAdapter(AppCompatActivity context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.treatList = objects;
    }

}
