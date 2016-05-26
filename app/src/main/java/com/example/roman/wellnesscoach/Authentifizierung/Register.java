package com.example.roman.wellnesscoach.Authentifizierung;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import com.example.roman.wellnesscoach.R;
import com.example.roman.wellnesscoach.Server.ServerSchnittstelle;


public class Register extends AppCompatActivity {

    EditText name, password;
    String Name, Password;
    Context ctx = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        name = (EditText) findViewById(R.id.register_name);
        password = (EditText) findViewById(R.id.register_password);
    }

    public void registerRequest(View v) {
        Name = name.getText().toString();
        Password = password.getText().toString();
        if (!Name.equals("") && !Password.equals("")) {
            JSONObject myJSON = createRegisterJSON(Name, Password);
            ServerSchnittstelle asyncTask = new ServerSchnittstelle(new ServerSchnittstelle.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    System.out.println(output);
                    Toast.makeText(ctx, output, Toast.LENGTH_LONG).show();
                    Intent backToLogin = new Intent(ctx, Login.class);
                    startActivity(backToLogin);
                }
            });
            asyncTask.execute(myJSON.toString());
        }
        else
        {
            Toast.makeText(ctx, "Fehlender Benutzername / Passwort", Toast.LENGTH_LONG).show();
        }
    }

    public void backToLogin(View v)
    {
        Intent backToLogin = new Intent(ctx, Login.class);
        startActivity(backToLogin);
    }

    public JSONObject createRegisterJSON(String n, String pw)
    {
        String encrypted_password = BCrypt.hashpw(pw, BCrypt.gensalt());
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("Task", "Register");
            jsonObject.put("Username", n);
            jsonObject.put("Password", encrypted_password);
        }
        catch(Exception e)
        {
            System.err.println("JSONException");
            System.err.println(e.getMessage());
        }

        return jsonObject;
    }

}