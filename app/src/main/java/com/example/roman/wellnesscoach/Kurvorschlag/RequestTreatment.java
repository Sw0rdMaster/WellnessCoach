package com.example.roman.wellnesscoach.Kurvorschlag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.roman.wellnesscoach.R;
import com.example.roman.wellnesscoach.Server.ServerSchnittstelle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Roman on 12.04.2016.
 */
public class RequestTreatment extends Activity {

    SharedPreferences pref;
    String currentUser;
    SharedPreferences.Editor editor;
    Context ctx = this;
    ListView mainListView;

    ArrayAdapter<Checkbox> listAdapter;

    MyCustomAdapter dataAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_treatment);
        getUser();
        //Generate list View from ArrayList
        getCondListFromServer();
        getTreamentAimFromServer();

        //checkButtonClick();

    }

    public void getCondListFromServer()
    {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("Task", "getConditions");
        }
        catch(Exception e)
        {
            System.err.println("JSONException");
            System.err.println(e.getMessage());
        }

        ServerSchnittstelle asyncTask = new ServerSchnittstelle(new ServerSchnittstelle.AsyncResponse()
        {
            ArrayList<Checkbox> condList = new ArrayList<>();
            @Override
            public void processFinish(String output){
                try {
                    JSONArray jsonArray = new JSONArray(output);
                    int i;
                    for(i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsono = jsonArray.getJSONObject(i);
                        condList.add(i, new Checkbox(jsono.getString("Condition"), false));
                    }
                } catch (Exception e)
                {
                    System.err.print(e.getStackTrace());
                }
                fillCondList(condList);

            }
        });
        asyncTask.execute(jsonObject.toString());
    }

    public void fillCondList(ArrayList<Checkbox> conditions)
    {
        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.country_info, conditions);
        ListView listView = (ListView) findViewById(R.id.conditionList);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    public void confirmButton(View v)
    {
        final JSONObject myJSON = createTreatmentJSON();
        ServerSchnittstelle asyncTask = new ServerSchnittstelle(new ServerSchnittstelle.AsyncResponse()
        {
            @Override
            public void processFinish(String output){
                Intent showTreatment = new Intent(ctx, ProposeTreatment.class);
                System.out.println("Output = " + output);
                showTreatment.putExtra("JSON", output);
                startActivity(showTreatment);
            }
        });
        asyncTask.execute(myJSON.toString());
    }

    public JSONObject createTreatmentJSON()
    {
        JSONObject jsonObject = null;
        ListView listView = (ListView) findViewById(R.id.conditionList);


        try {
            jsonObject = new JSONObject();
            jsonObject.put("Task", "Treatment");
            jsonObject.put("Username", currentUser);

            ArrayList<Checkbox> checkBoxList = dataAdapter.countryList;

            for(int i = 0; i < checkBoxList.size(); i++)
            {
                Checkbox x = checkBoxList.get(i);
                jsonObject.put(x.getName(), Boolean.toString(x.isSelected()));
            }
        }
        catch(Exception e)
        {
            System.err.println("JSONException");
            System.err.println(e.getMessage());
        }

        return jsonObject;
    }

    public void getUser()
    {
        pref = ctx.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        currentUser = pref.getString("Username", null);
    }

    public void getTreamentAimFromServer()
    {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("Task", "getTreatmentAims");
        }
        catch(Exception e)
        {
            System.err.println("JSONException");
            System.err.println(e.getMessage());
        }

        ServerSchnittstelle asyncTask = new ServerSchnittstelle(new ServerSchnittstelle.AsyncResponse()
        {
            ArrayList<String> aimList = new ArrayList<>();
            @Override
            public void processFinish(String output) {
                try {
                    JSONArray jsonArray = new JSONArray(output);
                    int i;
                    for(i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsono = jsonArray.getJSONObject(i);
                        aimList.add(i, jsono.getString("Treatment"));
                    }
                }
                catch(Exception e)
                {
                    System.err.print(e.getStackTrace());
                }
                fillAimList(aimList);

            }
        });
        asyncTask.execute(jsonObject.toString());
    }

    public void fillAimList(ArrayList<String> aims)
    {
        Spinner aimSpinner = (Spinner)findViewById(R.id.sAimSpinner);
        ArrayAdapter<String> spinnerArrayAdapter;
        spinnerArrayAdapter = new ArrayAdapter<String>(RequestTreatment.this,
                android.R.layout.simple_spinner_item, aims); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aimSpinner.setAdapter(spinnerArrayAdapter);
    }

    private class MyCustomAdapter extends ArrayAdapter<Checkbox> {

        private ArrayList<Checkbox> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Checkbox> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Checkbox>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.country_info, null);

                holder = new ViewHolder();
                //holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                /*holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Checkbox country = (Checkbox) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        country.setSelected(cb.isChecked());
                    }
                });*/
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Checkbox country = countryList.get(position);
            //holder.code.setText(" (" + country.getCode() + ")");
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isSelected());
            holder.name.setTag(country);

            return convertView;

        }

    }


    private class Checkbox {

        String name = null;
        boolean selected = false;

        public Checkbox(String name, boolean selected) {
            super();
            this.name = name;
            this.selected = selected;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }
        public void setSelected(boolean selected) {
            this.selected = selected;
        }

    }

}