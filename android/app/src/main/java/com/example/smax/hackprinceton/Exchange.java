package com.example.smax.hackprinceton;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
    /*
    private double Exchanged(String to,  String from){

        return 1.0;//placeholder
    }
    */

    class exchangeClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            exchange.setText("hello");
          //  Exchange();

        }
    }
}

