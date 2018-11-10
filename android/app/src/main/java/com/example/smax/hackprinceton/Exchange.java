package com.example.smax.hackprinceton;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
public class Exchange extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String countryCode = intent.getStringExtra("COUNTRY_CODE");//from HomePage

    }
    private double ExchangeRate(String to,  String from){

        return 1.0;//placeholder
    }
}

