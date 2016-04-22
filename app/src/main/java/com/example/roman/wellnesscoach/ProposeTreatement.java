package com.example.roman.wellnesscoach;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

/**
 * Created by Roman on 12.04.2016.
 */
public class ProposeTreatement extends Activity {

    LinearLayout linearLayout;
    Button confirmButton;
    ScrollView scrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propose_treatment);
        linearLayout = new LinearLayout(this);
        String treatment = getIntent().getExtras().getString("JSON", "");
        System.out.println("Treatment: " + treatment);
        createTreatment(treatment);

    }

    public void createTreatment(String input)
    {
        try {
            JSONArray jsonArray = new JSONArray(input);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                createTable(jsonArray.getJSONObject(i));
            }
            addButton();

        }
        catch(Exception e)
        {
            System.err.println("Fehler beim Anzeigen der Kur");
        }
    }

    public void createTable(JSONObject jsono)
    {
        ArrayList<String> treatmentList = new ArrayList<>();
        try {
            treatmentList.add("Gerät: " + jsono.getString("Device"));
            treatmentList.add("Zeitdauer: " + jsono.getString("Dauer"));
            treatmentList.add("Licht: " + jsono.getString("Light"));
            treatmentList.add("Musik: " + jsono.getString("Musik"));
            treatmentList.add("Kräuter: " + jsono.getString("Krauter"));
            treatmentList.add("Sole: " + jsono.getString("Sole"));
        }
        catch(Exception e)
        {

        }


        scrollView = new ScrollView(this);

        //setContentView(linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for( int i = 0; i < (treatmentList.size()-1); i++ )
        {
            if(!treatmentList.get(i).contains("false")) {
                TextView textView = new TextView(this);
                textView.setBackgroundResource(R.drawable.checkboxline);
                textView.setPadding(20, 20, 20, 20);
                textView.setText(treatmentList.get(i));
                linearLayout.addView(textView);
                //RatingBar ratingBar = new RatingBar(this);
                //RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
                //linearLayout.addView(ratingBar);

            }
        }

        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.checkboxline_fat);
        textView.setPadding(20, 20, 20, 20);
        textView.setText(treatmentList.get(treatmentList.size() - 1));
        linearLayout.addView(textView);

        scrollView.addView(linearLayout);
        setContentView(scrollView);

    }

    public void addButton()
    {
        confirmButton = new Button(this);
        confirmButton.setText("Hallo");
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmButton();
            }
        });
        linearLayout.addView(confirmButton);
    }

    public void onConfirmButton()
    {
        Intent goToVotingIntent = new Intent(this, Voting.class);
        startActivity(goToVotingIntent);
    }

}

