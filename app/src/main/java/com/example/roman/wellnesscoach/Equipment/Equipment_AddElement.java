package com.example.roman.wellnesscoach.Equipment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.roman.wellnesscoach.R;
import com.example.roman.wellnesscoach.Server.ServerSchnittstelle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Roman on 09.04.2016.
 */
public class Equipment_AddElement extends AppCompatActivity{
    Context ctx=this;
    ArrayAdapter<String> spinnerArrayAdapter;
    ArrayList<Equipment> dampfbadList;
    ArrayList<Equipment> saunaList;
    ArrayList<Equipment> whirlpoolList;
    ArrayList<Equipment> hotSpringList;
    SharedPreferences pref;
    String currentUser;
    SharedPreferences.Editor editor;

    ArrayList<Equipment> equipList;
    //String[] tempArray = null;
    String individuell = "Individuell";


    CheckBox music;
    CheckBox krauter;
    CheckBox light;
    CheckBox smell;
    CheckBox sole;

    Spinner equipSpinner;
    Spinner specificDeviceSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_equipment);
        getUser();
        initialize();
        spinnerEvent();
        //checkFunctions();
        JSONObject json = getAllDevicesJSON();
        asyncTask(json);

    }

    public void asyncTask(JSONObject json)
    {
        ServerSchnittstelle asyncTask = new ServerSchnittstelle(new ServerSchnittstelle.AsyncResponse()
        {
            @Override
            public void processFinish(String output){
                try {
                    JSONArray jsonArray = new JSONArray(output);

                    int i;
                    for(i = 0; i < jsonArray.length(); i++)
                    {

                        JSONObject jsono = jsonArray.getJSONObject(i);
                        Equipment equipment = new Equipment(jsono.get("Name").toString(), jsono.get("Device").toString());
                        equipment.setMusic(Boolean.parseBoolean(jsono.get("Musik").toString()));
                        equipment.setSole(Boolean.parseBoolean(jsono.get("Sole").toString()));
                        equipment.setSole(Boolean.parseBoolean(jsono.get("Light").toString()));
                        equipment.setSole(Boolean.parseBoolean(jsono.get("Krauter").toString()));
                        equipment.setSole(Boolean.parseBoolean(jsono.get("Smell").toString()));

                        switch(jsono.get("Device").toString())
                        {
                            case "Dampfbad":    dampfbadList.add(equipment);    break;
                            case "Sauna":       saunaList.add(equipment);       break;
                            case "Whirlpool":   whirlpoolList.add(equipment);   break;
                            case "Hotspring":   hotSpringList.add(equipment);   break;
                            default:
                                System.out.println("Ich bin im Default gelandet");
                        }


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


    public void initialize()
    {
        music = (CheckBox)findViewById(R.id.cMusik);
        krauter = (CheckBox)findViewById(R.id.cKrauter);
        light = (CheckBox)findViewById(R.id.cFarblicht);
        smell = (CheckBox)findViewById(R.id.cDufte);
        sole = (CheckBox)findViewById(R.id.cSole);

        //specificDeviceSpinner = (Spinner)findViewById(R.id.spinnerSpecific);

        dampfbadList = new ArrayList<>();
        saunaList = new ArrayList<>();
        whirlpoolList = new ArrayList<>();
        hotSpringList = new ArrayList<>();
    }

    public JSONObject getAllDevicesJSON()
    {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("Task", "GetAllDevices");
        }
        catch(Exception e)
        {
            System.err.println("JSONException");
            System.err.println(e.getMessage());
        }

        return jsonObject;
    }

    public String[] listToArray(ArrayList<Equipment> a)
    {
        String[] temp = new String[a.size()];
        for(int i = 0; i < a.size(); i++)
        {
            Equipment b = a.get(i);
            temp[i] = b.getArticleName();
        }
        return temp;
    }

    public void onEquipConfirmed(View v)
    {
        JSONObject myJSON = createAddDeviceJSON();
        ServerSchnittstelle asyncTask = new ServerSchnittstelle(new ServerSchnittstelle.AsyncResponse()
        {
            @Override
            public void processFinish(String output){
                Toast.makeText(ctx, output, Toast.LENGTH_LONG).show();
                Intent backToDevices = new Intent(ctx, Equipment_Overview.class);
                startActivity(backToDevices);
            }
        });
        asyncTask.execute(myJSON.toString());
    }

    public JSONObject createAddDeviceJSON()
    {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("Task", "AddDevice");
            jsonObject.put("Device", equipSpinner.getSelectedItem().toString());
            jsonObject.put("Name", individuell);
            jsonObject.put("Musik",  Boolean.toString(music.isChecked()));
            jsonObject.put("Sole", Boolean.toString(sole.isChecked()));
            jsonObject.put("Krauter",  Boolean.toString(krauter.isChecked()));
            jsonObject.put("Smell",  Boolean.toString(smell.isChecked()));
            jsonObject.put("Light",  Boolean.toString(light.isChecked()));
        }
        catch(Exception e)
        {
            System.err.println("JSONException");
            System.err.println(e.getMessage());
        }

        return jsonObject;
    }

    public ArrayList<Equipment> concatenate()
    {
        //TODO
        return null;
    }

    public void checkFunctions()
    {

        specificDeviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Boolean bool = false;
                Equipment temp = null;
                Object o = parent.getSelectedItem();
                String selected = o.toString();
                int i = 0;
                while (bool == false) {


                    if (selected.equals(individuell) || selected.equals("")) {
                        music.setChecked(false);
                        music.setClickable(true);

                        krauter.setChecked(false);
                        krauter.setClickable(true);

                        light.setChecked(false);
                        light.setClickable(true);

                        smell.setChecked(false);
                        smell.setClickable(true);

                        sole.setChecked(false);
                        sole.setClickable(true);

                        bool = true;
                    } else {
                        if (dampfbadList.get(i).getArticleName().equals(selected)) {
                            temp = dampfbadList.get(i);

                            music.setChecked(temp.isMusic());
                            music.setClickable(false);

                            krauter.setChecked(temp.isHerbs());
                            krauter.setClickable(false);

                            light.setChecked(temp.isColor());
                            light.setClickable(false);

                            smell.setChecked(temp.isFragrance());
                            smell.setClickable(false);

                            sole.setChecked(temp.isSole());
                            sole.setClickable(false);

                            bool = true;

                        } else {
                            i++;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void spinnerEvent()
    {
        equipSpinner = (Spinner)findViewById(R.id.spinEquip);
        equipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            ViewFlipper vf = (ViewFlipper) findViewById(R.id.vf);


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getSelectedItem();
                String[] tempArray = null;
                switch (o.toString()) {
                    case "":
                        vf.setDisplayedChild(0);
                        tempArray = new String[0];
                        break;
                    case "Dampfbad":
                        vf.setDisplayedChild(1);
                        tempArray = listToArray(dampfbadList);
                        break;
                    case "Hotspring":
                        vf.setDisplayedChild(2);
                        tempArray = listToArray(hotSpringList);
                        break;
                    case "Sauna":
                        vf.setDisplayedChild(3);
                        tempArray = listToArray(saunaList);
                        break;
                    case "Whirlpool":
                        vf.setDisplayedChild(4);
                        tempArray = listToArray(whirlpoolList);
                        break;

                }

                /*spinnerArrayAdapter = new ArrayAdapter<String>(Equipment_AddElement.this,
                        android.R.layout.simple_spinner_item, tempArray); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                specificDeviceSpinner.setAdapter(spinnerArrayAdapter);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
