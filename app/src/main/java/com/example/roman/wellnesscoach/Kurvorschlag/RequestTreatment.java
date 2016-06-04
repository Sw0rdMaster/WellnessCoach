package com.example.roman.wellnesscoach.Kurvorschlag;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.roman.wellnesscoach.Authentifizierung.MainWindow;
import com.example.roman.wellnesscoach.R;
import com.example.roman.wellnesscoach.Server.ServerSchnittstelle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 12.04.2016.
 */
public class RequestTreatment extends AppCompatActivity {

    SharedPreferences pref;
    String currentUser;
    SharedPreferences.Editor editor;
    Context ctx = this;
    ListView mainListView;
    Spinner aimSpinner;
    Spinner timeSpinner;

    ArrayAdapter<CustomCheckbox> listAdapter;

    MyCustomAdapter dataAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_treatment);
        getUser();
        //Generate list View from ArrayList
        getCondListFromServer();
        getTreamentAimFromServer();
        setTimerange();

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
            ArrayList<CustomCheckbox> condList = new ArrayList<>();
            @Override
            public void processFinish(String output){
                try {
                    JSONArray jsonArray = new JSONArray(output);
                    int i;
                    for(i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsono = jsonArray.getJSONObject(i);
                        condList.add(i, new CustomCheckbox(jsono.getString("Condition"), false));
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

    public void fillCondList(ArrayList<CustomCheckbox> conditions)
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
            jsonObject.put("Aim", aimSpinner.getSelectedItem());
            jsonObject.put("Time", timeSpinner.getSelectedItem());


            ArrayList<CustomCheckbox> checkBoxList = dataAdapter.countryList;

            for(int i = 0; i < checkBoxList.size(); i++)
            {
                CustomCheckbox x = checkBoxList.get(i);
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

    public void backToOverview(View v)
    {
        Intent back = new Intent(ctx, MainWindow.class);
        startActivity(back);
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
        aimSpinner = (Spinner)findViewById(R.id.sAimSpinner);
        ArrayAdapter<String> spinnerArrayAdapter;
        spinnerArrayAdapter = new ArrayAdapter<String>(RequestTreatment.this,
                android.R.layout.simple_spinner_item, aims); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aimSpinner.setAdapter(spinnerArrayAdapter);
    }

    private class MyCustomAdapter extends ArrayAdapter<CustomCheckbox> {

        private ArrayList<CustomCheckbox> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<CustomCheckbox> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<CustomCheckbox>();
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
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    CustomCheckbox country = (CustomCheckbox) cb.getTag();
                    country.setSelected(cb.isChecked());
                }
            });

            CustomCheckbox country = countryList.get(position);
            //holder.code.setText(" (" + country.getCode() + ")");
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isSelected());
            holder.name.setTag(country);

            return convertView;

        }

    }

    public void setTimerange()
    {
        List<String> spinnerArray =  new ArrayList<String>();

        for(int i = 1; i <= 24; i++)
        {
            spinnerArray.add(Integer.toString(i*5));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
        timeSpinner.setAdapter(adapter);
    }


    private class CustomCheckbox {

        String name = null;
        boolean selected = false;

        public CustomCheckbox(String name, boolean selected) {
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