package com.wellnesscoach.Authentifizierung;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wellnesscoach.Equipment.Equipment_Overview;
import com.wellnesscoach.Kurvorschlag.RequestTreatment;
import com.example.roman.wellnesscoach.R;
import com.wellnesscoach.Server.ServerSchnittstelle;

import org.json.JSONObject;

public class MainWindow extends AppCompatActivity {

    String name;
    TextView nameTV;
    Context ctx=this;
    SharedPreferences pref;
    String currentUser;
    SharedPreferences.Editor editor;
    JSONObject userDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);



        initialize();
    }

    public void initialize()
    {
        nameTV = (TextView) findViewById(R.id.home_name);
        name = getIntent().getStringExtra("name");

        pref = ctx.getSharedPreferences("MyPref", 0);
        if(name == null)
        {
            name = pref.getString("Username", null);
        }
        nameTV.setText("Willkommen " + name);

        getUser();

        userDevices = createGetDevicesJSON();
        startAsyncTask(userDevices);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        pref = ctx.getSharedPreferences("MyPref", 0);
        if(pref.getString("Username", null) != null)
        {

        }
        else
        {
            Intent logoutIntent = new Intent(ctx, Login.class);
            startActivity(logoutIntent);
        }
    }

    public void onClickKur(View v)
    {
        Intent kurIntent = new Intent(this, RequestTreatment.class);
        startActivity(kurIntent);
    }

    public void onClickEquipment(View v)
    {
        Intent equipIntent = new Intent(ctx, Equipment_Overview.class);
        startActivity(equipIntent);

    }

    public void onClickLogout(View v)
    {
        Intent logoutIntent = new Intent(ctx, Login.class);
        pref = ctx.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        editor.clear().commit();
        startActivity(logoutIntent);
    }

    public void getUser()
    {
        pref = ctx.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        currentUser = pref.getString("Username", null);
    }

    public JSONObject createGetDevicesJSON()
    {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("Task", "GetUserDevices");
            jsonObject.put("Username", currentUser);
        }
        catch(Exception e)
        {
            System.err.println("JSONException");
            System.err.println(e.getMessage());
        }

        return jsonObject;
    }

    public void startAsyncTask(JSONObject json)
    {
        ServerSchnittstelle asyncTask = new ServerSchnittstelle(new ServerSchnittstelle.AsyncResponse()
        {
            @Override
            public void processFinish(String output){
                try {
                    if (output.equals("No Devices yet\r\n")) {
                        Button kurButton = (Button) findViewById(R.id.BKurButton);
                        kurButton.setText("Noch kein Gerät vorhanden");
                        kurButton.setEnabled(false);
                    }
                }

                catch(Exception e)
                {
                    System.err.print(e.getStackTrace());
                }
            }
        }, ctx);
        asyncTask.execute(json.toString());
    }


}