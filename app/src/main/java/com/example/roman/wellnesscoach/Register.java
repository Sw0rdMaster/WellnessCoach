package com.example.roman.wellnesscoach;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import com.example.roman.wellnesscoach.ServerSchnittstelle;


public class Register extends Activity {

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

    public void register_register(View v){
        Name = name.getText().toString();
        Password = password.getText().toString();
        JSONObject myJSON = createRegisterJSON(Name, Password);
        ServerSchnittstelle asyncTask = new ServerSchnittstelle(new ServerSchnittstelle.AsyncResponse()
        {
            @Override
            public void processFinish(String output){
                System.out.println(output);
                Toast.makeText(ctx, output, Toast.LENGTH_LONG).show();
                Intent backToLogin = new Intent(ctx, Login.class);
                startActivity(backToLogin);
            }
        });
        asyncTask.execute(myJSON.toString());
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