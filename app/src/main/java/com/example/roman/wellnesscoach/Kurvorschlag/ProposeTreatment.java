package com.example.roman.wellnesscoach.Kurvorschlag;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.roman.wellnesscoach.R;
import com.example.roman.wellnesscoach.Voting.Voting;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Roman on 28.04.2016.
 */
public class ProposeTreatment extends FragmentActivity {


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
                    Treatment_Fragment treatmentFragment = new Treatment_Fragment();

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



