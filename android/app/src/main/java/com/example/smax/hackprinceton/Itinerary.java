package com.example.smax.hackprinceton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Itinerary extends AppCompatActivity {
    private LinearLayout linearLayout;
    private EditText numPlaces;
    private Button genItin;
    private Button addPl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_itinerary);
        String countryCode = intent.getStringExtra("COUNTRY_CODE");//from HomePage
        linearLayout = findViewById(R.id.lnrDynamicTextHolder);
        numPlaces = findViewById(R.id.edtNoCreate);
        genItin = (Button)findViewById(R.id.addBtn);
        addPl = (Button)findViewById(R.id.addBtn);
        addPl.setOnClickListener(new addPlace());
        genItin.setOnClickListener(new generateItinerary());
    }
    class generateItinerary implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Toast.makeText(v.getContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
            int length = Integer.parseInt(numPlaces.getText().toString());
            //creates TextView elements programmatically
            for (int i = 0; i < length; i++) {
                TextView placeOfInterest = new TextView(v.getContext()); // Pass it an Activity or Context
                placeOfInterest.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                placeOfInterest.setText("Sample Text");
                placeOfInterest.setId(i + 1);
                linearLayout.addView(placeOfInterest);
            }

        }
    }
    class addPlace implements  View.OnClickListener{
        @Override
        public void onClick(View v){

        }
    }
}
