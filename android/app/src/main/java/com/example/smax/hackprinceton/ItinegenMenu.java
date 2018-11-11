package com.example.smax.hackprinceton;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.smax.hackprinceton.util.api.APICall;

public class ItinegenMenu extends AppCompatActivity {
    private SeekBar SeekBar1;
    private SeekBar SeekBar2;
    private SeekBar SeekBar3;
    private SeekBar SeekBar4;
    private SeekBar SeekBar5;
    private String coords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinegen);
        coords = getIntent().getStringExtra("COORDINATES");
        SeekBar1 = findViewById(R.id.slider1);
        SeekBar2 = findViewById(R.id.slider2);
        SeekBar3 = findViewById(R.id.slider3);
        SeekBar4 = findViewById(R.id.slider4);
        SeekBar5 = findViewById(R.id.slider5);
        SeekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((TextView)findViewById(R.id.display1)).setText(progress+1+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((TextView)findViewById(R.id.display2)).setText(progress+1+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SeekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((TextView)findViewById(R.id.display3)).setText(progress+1+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SeekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((TextView)findViewById(R.id.display4)).setText(progress+1+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SeekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((TextView)findViewById(R.id.display5)).setText(progress+1+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button submit = findViewById(R.id.submitGen);
        submit.setOnClickListener(new submitItin());
    }
    public class submitItin implements View.OnClickListener{
        String[] categories = {"museum","shopping","restaurants","sightseeing","nightlife"};
        @Override
        public void onClick(View v) {
            StringBuilder query = new StringBuilder();
            query.append("[").append(jsonFormer(categories[0],SeekBar1.getProgress()))
                    .append(',').append(jsonFormer(categories[1],SeekBar2.getProgress()))
                    .append(',').append(jsonFormer(categories[2],SeekBar3.getProgress()))
                    .append(',').append(jsonFormer(categories[3],SeekBar4.getProgress()))
                    .append(',').append(jsonFormer(categories[4],SeekBar5.getProgress()))
                    .append("]");
            String radius = "2000";
            String num = ((EditText)findViewById(R.id.numItems)).getText().toString();
            new APICall("/itinerary",result -> {
                Intent intent = new Intent(v.getContext(),Itinerary.class);
                intent.putExtra("ENTRIES",result.toString());
                startActivity(intent);
            }).param("radius",radius)
                    .param("num",num)
                    .param("queries", query.toString())
                    .param("center", coords)
                    .execute();
        }
    }
    public String jsonFormer(String cat, int num){
        StringBuilder json = new StringBuilder();
        json.append("{\"query\":").append('\"'+cat+"\",")
                .append("\"weight\":").append(num).append('}');
        return json.toString();
    }
}