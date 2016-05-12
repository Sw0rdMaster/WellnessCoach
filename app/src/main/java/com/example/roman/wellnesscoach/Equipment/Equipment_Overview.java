package com.example.roman.wellnesscoach.Equipment;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.roman.wellnesscoach.Authentifizierung.MainWindow;
import com.example.roman.wellnesscoach.R;
import com.example.roman.wellnesscoach.Server.ServerSchnittstelle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Roman on 31.03.2016.
 */
public class Equipment_Overview extends ListActivity {
    Context ctx=this;
    ArrayList<String> equipment;
    ArrayAdapter<String> adapter;
    SharedPreferences pref;
    String currentUser;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment);
        getUser();
        JSONObject myJson = createGetDevicesJSON();
        startAsyncTask(myJson);

    }

    public void startAsyncTask(JSONObject json)
    {
        ServerSchnittstelle asyncTask = new ServerSchnittstelle(new ServerSchnittstelle.AsyncResponse()
        {
            @Override
            public void processFinish(String output){
                try {
                    equipment = new ArrayList<>();
                    System.out.println("Ich bin das JSONArray " +output);
                    JSONArray jsonArray = new JSONArray(output);

                    int i;
                    for(i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsono = jsonArray.getJSONObject(i);
                        equipment.add(jsono.get("Name").toString());
                        adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, equipment);
                        getListView().setAdapter(adapter);
                    }
                }
                catch(Exception e)
                {
                    System.err.print(e.getStackTrace());
                }

            }
        });
        asyncTask.execute(json.toString());
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

    public void onClickAddElement(View v)
    {
        equipment.add("Mitsuru");
        adapter.notifyDataSetChanged();
    }

    public void onClickCustomize(View v)
    {
        Intent customize = new Intent(ctx, Equipment_AddElement.class);
        startActivityForResult(customize, 101);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void backToOverview(View v)
    {
        Intent goToOverview = new Intent(this, MainWindow.class);
        startActivity(goToOverview);
    }





}
