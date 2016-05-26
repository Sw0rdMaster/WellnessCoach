package com.example.roman.wellnesscoach.Authentifizierung;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.roman.wellnesscoach.Authentifizierung.Login;
import com.example.roman.wellnesscoach.Equipment.Equipment_Overview;
import com.example.roman.wellnesscoach.Kurvorschlag.RequestTreatment;
import com.example.roman.wellnesscoach.R;

public class MainWindow extends AppCompatActivity {

    String name;
    TextView nameTV;
    Context ctx = this;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

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