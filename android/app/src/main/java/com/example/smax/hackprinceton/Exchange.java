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

import com.example.smax.hackprinceton.util.api.APICall;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Exchange extends AppCompatActivity{
    private Button exchange;
    private double exchangeRate;
    private String currencySymbol;
    private TextView exchangedSum;
    private Double numDollars;
    private TextView exchangeRateDisplay;
    private EditText usdSum;
    private Double exchangedAmount;
    private String countryCode;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_exchange);
        exchange = (Button)findViewById(R.id.currencyChange);
        exchange.setOnClickListener(new exchangeClick());
        exchangeRateDisplay = (TextView)findViewById(R.id.exchangeRateDisplay);
        usdSum = (EditText)findViewById(R.id.usdText);
        exchangedSum = (TextView)findViewById(R.id.exchangeSum);
        //numDollars = Double.parseDouble(usdSum.getText().toString());
        countryCode = intent.getStringExtra("COUNTRY_CODE");//from HomePage


        new APICall("/exchange", result -> {
            exchangeRate = result.getDouble("result");
        }).param("from", "USD").param("to", countryCode).execute();

        new APICall("/currency",result -> {
            currencySymbol = result.getString("result");
            exchangeRateDisplay.setText("Exchange Rate from USD to "+ currencySymbol + " is " +exchangeRate);
        }).param("country",countryCode).execute();
    }

    class exchangeClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                numDollars = Double.parseDouble(usdSum.getText().toString());
            } catch (Exception e) {
                Log.e("ALEXA PLAY DESPACITO", e.toString());
            }
            if (numDollars > 0) {
                exchangedAmount = exchangeRate * numDollars;
                String formattedSum = String.format("%.2f", exchangedAmount);
                exchangedSum.setText("Amount in " + currencySymbol + " is " + formattedSum);
            }
        }

    }
}



