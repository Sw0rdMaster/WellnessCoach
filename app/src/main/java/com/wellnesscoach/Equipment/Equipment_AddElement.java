package com.wellnesscoach.Equipment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.wellnesscoach.Authentifizierung.MainWindow;
import com.example.roman.wellnesscoach.R;
import com.wellnesscoach.Server.ServerSchnittstelle;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Roman on 09.04.2016.
 */
public class Equipment_AddElement extends AppCompatActivity{
    Context ctx=this;
    SharedPreferences pref;
    String currentUser;
    SharedPreferences.Editor editor;

    //String[] tempArray = null;
    String individuell = "Individuell";


    Spinner equipSpinner;

    MyCustomAdapter dataAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_equipment);
        getUser();
        spinnerEvent();
        fillFeatureList();

    }


    public void getUser()
    {
        pref = ctx.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        currentUser = pref.getString("Username", null);
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
        }, ctx);
        asyncTask.execute(myJSON.toString());
    }

    public void equipBackToOverview(View v)
    {
        Intent goBack = new Intent(this, MainWindow.class);
        startActivity(goBack);
    }

    public JSONObject createAddDeviceJSON()
    {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("Task", "AddDevice");
            jsonObject.put("User", currentUser);
            jsonObject.put("Device", equipSpinner.getSelectedItem().toString());

            ArrayList<CustomCheckbox> checkBoxList = dataAdapter.countryList;

            for(int i = 0; i < checkBoxList.size(); i++)
            {
                CustomCheckbox x = checkBoxList.get(i);
                jsonObject.put(stringToFunction(x.getName()), Boolean.toString(x.isSelected()));
            }
            jsonObject.put("Dufte", "false");
        }
        catch(Exception e)
        {
            System.err.println("JSONException");
            System.err.println(e.getMessage());
        }

        return jsonObject;
    }

    public String stringToFunction(String a)
    {
        String result = null;
        if(a.contains("Kräuter"))
        {
            result = "Krauter";
        }
        if(a.contains("licht"))
        {
            result = "Licht";
        }
        if(a.contains("Musik"))
        {
            result = "Musik";
        }
        if(a.contains("Sole"))
        {
            result = "Sole";
        }
        return result;
    }


    public void spinnerEvent()
    {
        equipSpinner = (Spinner)findViewById(R.id.spinEquip);
        equipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            ViewFlipper vf = (ViewFlipper) findViewById(R.id.vf);


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getSelectedItem();
                switch (o.toString()) {
                    case "":
                        vf.setDisplayedChild(0);
                        break;
                    case "Dampfbad":
                        vf.setDisplayedChild(1);
                        break;
                    case "Hotspring":
                        vf.setDisplayedChild(1);
                        break;
                    case "Sauna":
                        vf.setDisplayedChild(1);
                        break;
                    case "Whirlpool":
                        vf.setDisplayedChild(1);
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

    public void fillFeatureList()
    {
        ArrayList<CustomCheckbox> featureList = new ArrayList<>();
        featureList.add(0, new CustomCheckbox(getString(R.string.licht_text), false));
        featureList.add(1, new CustomCheckbox(getString(R.string.kräuter_text), false));
        featureList.add(2, new CustomCheckbox(getString(R.string.musik_text), false));
        featureList.add(3, new CustomCheckbox(getString(R.string.sole_text), false));
        dataAdapter = new MyCustomAdapter(this, R.layout.treatment_info, featureList);

        ListView fList = (ListView)findViewById(R.id.lEigenschaften);
        fList.setAdapter(dataAdapter);
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
                convertView = vi.inflate(R.layout.treatment_info, null);

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
