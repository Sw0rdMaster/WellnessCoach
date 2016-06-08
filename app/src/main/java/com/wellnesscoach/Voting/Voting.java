package com.wellnesscoach.Voting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wellnesscoach.Authentifizierung.MainWindow;
import com.wellnesscoach.Kurvorschlag.Treatment_Fragment;
import com.example.roman.wellnesscoach.R;
import com.wellnesscoach.Server.ServerSchnittstelle;

import org.json.JSONArray;
import org.json.JSONException;
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
    private JSONObject currentTreatment;

    SharedPreferences pref;
    String currentUser;
    SharedPreferences.Editor editor;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_treatment);
        listView = (ListView)findViewById(R.id.list_view);
        treatmentString = getIntent().getExtras().getString("JSON", "");
        treatmentCounter = getIntent().getExtras().getInt("Counter", 0);

        getUser();

        myLayout = (RelativeLayout)findViewById(R.id.votingLayout);

        setLisData(treatmentString);

        adapter = new ListViewAdapter(this, R.layout.item_listview, arrayList);

        listView.setAdapter(adapter);

        listView.getAdapter().getItem(3);


    }

    public void getUser()
    {
        pref = ctx.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        currentUser = pref.getString("Username", null);
    }


    public void createTable() {

        try {
            Button titleButton = (Button)findViewById(R.id.titleButton);
            titleButton.setText(currentTreatment.getString("Device"));
            if(!currentTreatment.getString("Device").equals("false")) {
                arrayList.add(new VotingBar(1, Treatment_Fragment.getTabbedString("Gerät:", currentTreatment.getString("Device"), 350), "Device"));
            }
            if(!currentTreatment.getString("Temperatur").equals("false")) {
                arrayList.add(new VotingBar(1, Treatment_Fragment.getTabbedString("Temperatur:", currentTreatment.getString("Temperatur"), 350),"Temperatur"));
            }
            if(!currentTreatment.getString("Light").equals("Ohne")) {
                arrayList.add(new VotingBar(1, Treatment_Fragment.getTabbedString("Lichtfarbe:", currentTreatment.getString("Light"), 350), "Light"));
            }
            if(!currentTreatment.getString("Krauter").equals("false")) {
                arrayList.add(new VotingBar(1, Treatment_Fragment.getTabbedString("Kräuter:", currentTreatment.getString("Krauter"), 350), "Krauter"));
            }
            if(!currentTreatment.getString("Musik").equals("Ohne")) {
                arrayList.add(new VotingBar(1, Treatment_Fragment.getTabbedString("Musik:", currentTreatment.getString("Musik"), 350), "Musik"));
            }

        }
        catch(Exception e)
        {

        }
    }



    private void setLisData(String treatment) {

        arrayList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(treatment);
            currentTreatment = jsonArray.getJSONObject(treatmentCounter);
            createTable();
        }
        catch(Exception e)
        {
            System.err.println("Fehler beim Anzeigen der Kur");
        }


    }

    public float getRating(String searchFor)
    {
        System.out.println("Adapterlänge = " + adapter.getCount());
        for(int i = 0; i < adapter.getCount(); i++)
        {
            VotingBar temp = adapter.getItem(i);
            if(searchFor.equals(temp.getProperty()))
            {
                return temp.getRatingStar();
            }
        }
        return 0;
    }

    public JSONObject fillVotingJSON(String property, JSONObject json)
    {
        try{
            json.put(property, currentTreatment.getString(property));
            json.put(currentTreatment.getString(property), getRating(property));
        }
        catch(JSONException e)
        {
            System.err.println("JSONException");
            System.err.println(e.getMessage());
        }
        return json;
    }

    public void sendVotingToServer()
    {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("Task", "Voting");
            jsonObject.put("User", currentUser);

            jsonObject = fillVotingJSON("Device", jsonObject);
            jsonObject = fillVotingJSON("Temperatur", jsonObject);
            jsonObject = fillVotingJSON("Light", jsonObject);
            jsonObject = fillVotingJSON("Krauter", jsonObject);
            jsonObject = fillVotingJSON("Musik", jsonObject);
        }
        catch(Exception e)
        {
            System.err.println("JSONException");
            System.err.println(e.getMessage());
        }

        ServerSchnittstelle asyncTask = new ServerSchnittstelle(new ServerSchnittstelle.AsyncResponse()
        {
            @Override
            public void processFinish(String output){

            }
        }, ctx);
        asyncTask.execute(jsonObject.toString());
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
        sendVotingToServer();

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

    public void skipVoting(View v)
    {
        Intent goBack = new Intent(this, MainWindow.class);
        startActivity(goBack);
    }
}
