package com.example.smax.hackprinceton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        TextView welcomeBanner = (TextView) findViewById(R.id.welcomeBanner);
        welcomeBanner.setText("Welcome to Princeton");
    }
}
