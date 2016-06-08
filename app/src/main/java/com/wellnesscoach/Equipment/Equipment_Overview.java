package com.wellnesscoach.Equipment;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.wellnesscoach.Authentifizierung.MainWindow;
import com.example.roman.wellnesscoach.R;
import com.wellnesscoach.Server.ServerSchnittstelle;

import org.json.JSONArray;
import org.json.JSONException;
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
    ListView equipList;
    SharedPreferences.Editor editor;
    int currentDevice;


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
                    if (!output.equals("No Devices yet\r\n")) {
                        equipment = new ArrayList<>();
                        System.out.println("Ich bin das JSONArray " + output);
                        JSONArray jsonArray = new JSONArray(output);

                        int i;
                        for (i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsono = jsonArray.getJSONObject(i);
                            equipment.add(jsono.get("Name").toString());
                            equipList = getListView();
                            adapter = new ArrayAdapter<String>(equipList.getContext(), android.R.layout.simple_list_item_1, equipment);
                            getListView().setAdapter(adapter);
                            Button removeDeviceButton = (Button) findViewById(R.id.removeButton);
                            removeDeviceButton.setEnabled(true);
                            currentDevice();
                        }
                    }
                    else
                    {
                        equipList = getListView();
                        equipment = new ArrayList<>();
                        equipment.add("Noch kein Gerät registriert");
                        adapter = new ArrayAdapter<String>(equipList.getContext(), android.R.layout.simple_list_item_1, equipment);
                        getListView().setAdapter(adapter);

                        Button removeDeviceButton = (Button) findViewById(R.id.removeButton);
                        removeDeviceButton.setEnabled(false);
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

    public void currentDevice()
    {
        equipList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentDevice = position;
            }
        });
    }

    public void removeDevice(View v)
    {
        System.out.println("Entferne: " + equipList.getItemAtPosition(currentDevice).toString());

        JSONObject jsono = null;

        try{
            jsono = new JSONObject();
            jsono.put("Task", "RemoveDevice");
            jsono.put("User", currentUser);
            jsono.put("Device", equipList.getItemAtPosition(currentDevice).toString());
        }
        catch(JSONException e)
        {

        }

        ServerSchnittstelle asyncTask = new ServerSchnittstelle(new ServerSchnittstelle.AsyncResponse()
        {
            @Override
            public void processFinish(String output){
                Intent reloadPage = new Intent(ctx, Equipment_Overview.class);
                startActivity(reloadPage);
            }
        }, ctx);
        asyncTask.execute(jsono.toString());

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
