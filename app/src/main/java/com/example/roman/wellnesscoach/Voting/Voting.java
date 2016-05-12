package com.example.roman.wellnesscoach.Voting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.roman.wellnesscoach.Authentifizierung.MainWindow;
import com.example.roman.wellnesscoach.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Roman on 20.04.2016.
 */
public class Voting extends AppCompatActivity{

    private ListView listView;
    private ArrayAdapter<VotingBar> adapter;
    private ArrayList<VotingBar> arrayList;
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


    }


    public void createTable(JSONObject jsono) {

        try {
            Button titleButton = (Button)findViewById(R.id.titleButton);
            titleButton.setText(jsono.getString("Device"));
            arrayList.add(new VotingBar(1, "Temperatur " + jsono.getString("Temperatur"), null));
            arrayList.add(new VotingBar(1, "Licht " + jsono.getString("Light"), null));
            arrayList.add(new VotingBar(1, "Kräuter " + jsono.getString("Krauter"), null));
            arrayList.add(new VotingBar(1, "Musik " + jsono.getString("Musik"), null));

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
            Intent backToOverview = new Intent(this, MainWindow.class);
            startActivity(backToOverview);
        }
    }
}
