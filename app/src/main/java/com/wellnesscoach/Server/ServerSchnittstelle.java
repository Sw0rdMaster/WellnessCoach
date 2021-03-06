package com.wellnesscoach.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Roman on 10.04.2016.
 */
public class ServerSchnittstelle extends AsyncTask<String, String, String>{
    String URL_String = "http://192.168.1.117:8080/WellnessCoachServer/NameServlet";
    //String URL_String = "http://10.0.2.2:8080/WellnessCoachServer/NameServlet";
    //String URL_String = "http://bopa.one:7070/WellnessCoachServer/NameServlet";
    String data = "";
    AsyncResponse delegate = null;
    Context ctx;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public ServerSchnittstelle(AsyncResponse delegate, Context context) {
        this.delegate = delegate;
        ctx = context;
    }

    @Override
    protected String doInBackground(String... params) {
        // Bekommt Art des Auftrags
        String json = params[0];
        int tmp;

        // Wandelt URL String in URL Objekt um
        URL url = stringToUrl(URL_String);

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            System.out.println("Request = " + json.toString());
            os.write(json.toString().getBytes("utf-8"));
            os.flush();
            os.close();
            InputStream is = httpURLConnection.getInputStream();
            while ((tmp = is.read()) != -1) {
                data += (char) tmp;
            }
            is.close();
            httpURLConnection.disconnect();

            System.out.println("Die Antwort vom Server lautet: " + data);
            return data;

        }
        catch(IOException e)
        {

        }
        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s != null) {
            delegate.processFinish(s);
        }
        else
        {
            Toast.makeText(ctx, "Keine Verbindung zum Server", Toast.LENGTH_LONG).show();
        }


    }

    public URL stringToUrl(String s_url)
    {
        URL url = null;
        try{
            url = new URL(s_url);
        }
        catch(MalformedURLException e)
        {
            System.err.println(e.getMessage());
        }
        return url;
    }


}
