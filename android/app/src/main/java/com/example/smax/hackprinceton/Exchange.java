package com.example.smax.hackprinceton;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Exchange extends AppCompatActivity{
    private Button exchange;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_exchange);
        exchange = (Button)findViewById(R.id.currencyChange);
        exchange.setOnClickListener(new exchangeClick());
        String countryCode = intent.getStringExtra("COUNTRY_CODE");//from HomePage
    }
    class exchangeClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            exchange.setText("hello");
          //  Exchange();

        }
    }
    /*
    public class exchangeRate extends AsyncTask<String, Void, Double>{
        private String fixerAPIKey = "4d764507f666731a05a6ff3411aae672";
        @Override
        protected Double doInBackground(String... params){
            StringBuilder request = new StringBuilder();
            request.append("http://data.fixer.io/api/latest")
                    .append("?access_key="+fixerAPIKey)
                    .append("&base="+param[0])
                    .append("&symbols="+param[1])
                    .append("&format=1");
            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;
            InputStream inStream = null;
            try{
                url = new URL(request.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                inStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                String temp, response = "";
                while ((temp = reader.readLine()) != null) {
                    response += temp;
                }
                object = (JSONObject) new JSONTokener(response).nextValue();
            }catch(Exception e){

            }finally {
                if (inStream != null) {
                    try {
                        // this will close the bReader as well
                        inStream.close();
                    } catch (IOException ignored) {
                    }
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

        }
    }
    */
}

