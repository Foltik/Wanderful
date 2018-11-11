package com.example.smax.hackprinceton;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private double exchangeRate;
    private TextView exchangedSum;
    private Double numDollars;
    private EditText usdSum;
    private Double exchangedAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_exchange);
        exchange = (Button)findViewById(R.id.currencyChange);
        exchange.setOnClickListener(new exchangeClick());
        usdSum = (EditText)findViewById(R.id.usdText);
        exchangedSum = (TextView)findViewById(R.id.currencyDisplay);
        //numDollars = Double.parseDouble(usdSum.getText().toString());
        String countryCode = intent.getStringExtra("COUNTRY_CODE");//from HomePage

        //API Call to retrieve latest exchange rate
        new stdlibAPICall("/exchange", new stdlibAPICallback() {
            @Override
            public void onComplete(JSONObject result) {
                try {
                    exchangeRate = result.getDouble("result");
                }catch(Exception e){
                    Log.e("error",e.toString());
                }

            }
        }).add("from","USD").add("to",countryCode).execute();
    }
    class exchangeClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
        exchangedAmount = exchangeRate * numDollars;
        exchangedSum.setText(""+exchangedAmount);


        }
    }

}

