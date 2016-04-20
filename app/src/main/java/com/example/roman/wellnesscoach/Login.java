package com.example.roman.wellnesscoach;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class Login extends Activity {

    EditText name, password;
    String Name, Password;
    Context ctx=this;
    String url_string = "http://10.0.2.2:8080/WebHelloWorld/NameServlet";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = ctx.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        currentUser = pref.getString("Username", null);
        if(currentUser == null) {
            setContentView(R.layout.login);
            name = (EditText) findViewById(R.id.main_name);
            password = (EditText) findViewById(R.id.main_password);
        }
        else
        {
            Intent i = new Intent(ctx, Overview.class);
            i.putExtra("name", currentUser);
            startActivity(i);
        }
    }

    public void main_register(View v){
        Intent registerIntent = new Intent(this, Register.class);
        startActivity(registerIntent);

    }

    public void main_login(View v){
        Name = name.getText().toString();
        Password = password.getText().toString();
        JSONObject myJSON = createLoginJSON(Name);
        ServerSchnittstelle asyncTask = new ServerSchnittstelle(new ServerSchnittstelle.AsyncResponse()
        {
            @Override
            public void processFinish(String output){
                System.out.println("Response = " + output);

                if(output.endsWith("\r\n"))
                {
                    output = output.substring(0, output.length() - 2);
                }
                if (BCrypt.checkpw(Password, output))
                {
                    Intent i = new Intent(ctx, Overview.class);
                    i.putExtra("name", Name);

                    editor.putString("Username", Name);
                    editor.commit();
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(ctx, "Invalid Login", Toast.LENGTH_LONG).show();
                }
            }
        });
        asyncTask.execute(myJSON.toString());
    }

    public JSONObject createLoginJSON(String n)
    {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("Task", "Login");
            jsonObject.put("Username", n);
        }
        catch(Exception e)
        {
            System.err.println("JSONException");
            System.err.println(e.getMessage());
        }

        return jsonObject;
    }

    public void checkPassword(String pw)
    {
        if(pw.endsWith("\r\n"))
        {
            pw = pw.substring(0, pw.length() - 2);
        }
        if (BCrypt.checkpw(Password, pw))
        {
            Intent i = new Intent(ctx, Overview.class);
            i.putExtra("name", Name);

            editor.putString("Username", Name);
            editor.commit();
            startActivity(i);
        }
        else
        {
            Toast.makeText(ctx, "Invalid Login", Toast.LENGTH_LONG).show();
        }
    }


}