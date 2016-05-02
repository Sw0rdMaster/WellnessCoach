package com.example.roman.wellnesscoach;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Roman on 28.04.2016.
 */
public class ProposeTreatment_2 extends FragmentActivity {


    static int treatmentcounter = 0;
    static String treatmentString;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propose_treatment);

        treatmentString = getIntent().getExtras().getString("JSON", "");
        treatmentcounter = getIntent().getExtras().getInt("Counter", 0);

        System.out.println("Treatment: " + treatmentString);
        createTreatment(treatmentString);
    }

    public void createTreatment(String input)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        try {
            JSONArray jsonArray = new JSONArray(input);
            int firstFragment = 0;
            if (treatmentcounter >= jsonArray.length()) {
                onLastResult();
            } else {
                for (int i = treatmentcounter; i < jsonArray.length(); i++) {
                    FragmentOne treatmentFragment = new FragmentOne();

                    Bundle bundle = new Bundle();
                    System.out.println("Treatment in Method = " + jsonArray.get(i).toString());
                    bundle.putString("Treatment", jsonArray.get(i).toString());
                    bundle.putInt("NbrFragment", firstFragment);
                    firstFragment++;
                    treatmentFragment.setArguments(bundle);
                    transaction.add(R.id.linearPropose, treatmentFragment);
                }
                treatmentcounter++;
                transaction.commit();
            }
        }
        catch(JSONException e)
        {
            System.err.println("Problem = " + e.getMessage());
        }
    }

    public void onLastResult() {
        Intent goToVotingIntent = new Intent(this, Voting.class);
        System.out.println("An Voting = " + treatmentString);
        goToVotingIntent.putExtra("JSON", treatmentString);
        startActivity(goToVotingIntent);
    }

}



