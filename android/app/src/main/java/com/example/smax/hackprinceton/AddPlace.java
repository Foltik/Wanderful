package com.example.smax.hackprinceton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPlace extends AppCompatActivity {
    private EditText keywordText;
    private Button searchbtn;

    protected void OnCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_addplace);
        keywordText = (EditText)(findViewById(R.id.keyword));
        searchbtn = (Button)(findViewById(R.id.search));
        searchbtn.setOnClickListener(new runSearch());
    }
    class runSearch implements View.OnClickListener{
        @Override
        public void onClick(View v){

        }
    }
}
