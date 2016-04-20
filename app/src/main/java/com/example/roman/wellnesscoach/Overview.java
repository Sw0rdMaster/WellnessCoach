package com.example.roman.wellnesscoach;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Overview extends Activity {

    String name;
    TextView nameTV;
    Context ctx = this;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);

        nameTV = (TextView) findViewById(R.id.home_name);
        name = getIntent().getStringExtra("name");
        nameTV.setText("Welcome "+name);
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


}