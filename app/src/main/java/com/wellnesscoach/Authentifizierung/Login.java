package com.wellnesscoach.Authentifizierung;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roman.wellnesscoach.R;
import com.wellnesscoach.Server.ServerSchnittstelle;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class Login extends AppCompatActivity {

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
        getUser();

    }

    public void getUser()
    {
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
            Intent i = new Intent(ctx, MainWindow.class);
            i.putExtra("name", currentUser);
            startActivity(i);
        }
    }


    public void main_register(View v){
        Intent registerIntent = new Intent(this, Register.class);
        startActivity(registerIntent);

    }

    public void loginRequest(View v){
        Name = name.getText().toString();
        Password = password.getText().toString();
        JSONObject myJSON = createLoginJSON(Name);
        ServerSchnittstelle asyncTask = new ServerSchnittstelle(new ServerSchnittstelle.AsyncResponse()
        {
            @Override
            public void processFinish(String output){
                checkPassword(output);
            }
        }, ctx);
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
        if (pw.endsWith("\r\n")) {
            pw = pw.substring(0, pw.length() - 2);
        }
        if(!pw.equals("")) {
                if (BCrypt.checkpw(Password, pw)) {
                Intent i = new Intent(ctx, MainWindow.class);
                i.putExtra("name", Name);

                editor.putString("Username", Name);
                editor.commit();
                startActivity(i);
            } else {
                Toast.makeText(ctx, "Ungültiger Benutzername/Passwort", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(ctx, "Ungültiger Benutzername", Toast.LENGTH_LONG).show();
        }
    }


}