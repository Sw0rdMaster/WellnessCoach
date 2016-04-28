package com.example.roman.wellnesscoach;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 20.04.2016.
 */
public class Voting extends AppCompatActivity{

    private ListView listView;
    private ArrayAdapter<Movie> adapter;
    private ArrayList<Movie> arrayList;
    private RelativeLayout myLayout;
    private int treatmentCounter;
    private String treatmentString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_treatment);
        listView = (ListView)findViewById(R.id.list_view);
        treatmentString = getIntent().getExtras().getString("JSON", "");
        treatmentCounter = getIntent().getExtras().getInt("Counter", 0);

        myLayout = (RelativeLayout)findViewById(R.id.votingLayout);

        setLisData(treatmentString);

        adapter = new ListViewAdapter(this, R.layout.item_listview, arrayList);

        listView.setAdapter(adapter);

        listView.getAdapter().getItem(3);





        //listView.setAdapter(adapter);

        //listView.setOnItemClickListener(onItemClickListener());
    }

    /*private AdapterView.OnItemClickListener onItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(Voting.this);
                dialog.setContentView(R.layout.layout_dialog);
                dialog.setTitle("Movie details");

                TextView name = (TextView) dialog.findViewById(R.id.movie_name);
                TextView country = (TextView) dialog.findViewById(R.id.country);
                TextView starRate = (TextView) dialog.findViewById(R.id.rate);

                Movie movie = (Movie) parent.getAdapter().getItem(position);
                name.setText("Movie name: " + movie.getName());
                country.setText("Producing country: " + movie.getCountry());
                starRate.setText("Your rate: " + movie.getRatingStar());

                dialog.show();
            }
        };
    }*/

    public void createTable(JSONObject jsono) {

        try {
            //arrayList.add(new Movie(1, "Gerät: " + jsono.getString("Device") , null));
            //arrayList.add(new Movie(1, null, null));
            Button titleButton = (Button)findViewById(R.id.titleButton);
            titleButton.setText(jsono.getString("Device"));
            arrayList.add(new Movie(1, "Licht " + jsono.getString("Light"), null));
            arrayList.add(new Movie(1, "Musik " + jsono.getString("Musik"), null));
            arrayList.add(new Movie(1, "Kräuter " + jsono.getString("Krauter"), null));
            arrayList.add(new Movie(1, "Sole " + jsono.getString("Sole"), null));




        }
        catch(Exception e)
        {

        }
    }

    private void setLisData(String treatment) {

        arrayList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(treatment);
            createTable(jsonArray.getJSONObject(treatmentCounter));
        }
        catch(Exception e)
        {
            System.err.println("Fehler beim Anzeigen der Kur");
        }


    }

    public boolean lastVote()
    {
        int arraySize = 0;
        try{
            JSONArray jsonArray = new JSONArray(treatmentString);
            arraySize = jsonArray.length();
        }
        catch(Exception e)
        {

        }

        return (treatmentCounter < arraySize);

    }

    public void voteNext(View v)
    {
        treatmentCounter++;

        if(lastVote())
        {
            Intent nextVote = new Intent(this, Voting.class);
            nextVote.putExtra("JSON", treatmentString);
            nextVote.putExtra("Counter", treatmentCounter);
            startActivity(nextVote);
        }
        else
        {
            Intent backToOverview = new Intent(this, Overview.class);
            startActivity(backToOverview);
        }
    }
}
