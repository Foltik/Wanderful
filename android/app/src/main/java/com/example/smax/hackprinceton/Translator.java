package com.example.smax.hackprinceton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smax.hackprinceton.util.api.APICall;
import com.example.smax.hackprinceton.util.serialize.Serializer;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Translator extends AppCompatActivity {
    private static final String API_KEY = "AIzaSyCVktAWTsOabuB_YAZdLznuEtyiZnbUlss";
    private static final String FILE_NAME = "savedphrases.txt";
    private Serializer<String> savedSerializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiates a client
        setContentView(R.layout.activity_translator);
        final Button translateButton = findViewById(R.id.translateButton);
        final ImageButton saveButton = findViewById(R.id.saveButton);
        final TextView userText = findViewById(R.id.userText);
        final TextView translateText = findViewById(R.id.translateText);
        final TextView phrasesText = findViewById(R.id.phrasesText);

        savedSerializer = new Serializer<>("saved.txt");
        String oldText = savedSerializer.load();
        if (oldText != null)
            phrasesText.setText(oldText);


        String countryCode = getIntent().getStringExtra("COUNTRY_CODE");

        translateButton.setOnClickListener(v -> {
            new APICall("/translate", result -> {
                Log.d("something", result.getString("result"));
                String translatedText = result.getString("result");
                translateText.setText(translatedText);
            }).param("from", "en")
            .param("to", "jpn")
            .param("str", userText.getText().toString())
            .execute();
        });

        saveButton.setOnClickListener(v -> {
            String text = translateText.getText().toString();

            String newText = phrasesText.getText().toString() + '\n' + userText.getText().toString() + " - " + text;
            savedSerializer.save(newText);
            phrasesText.setText(newText);
        });
    }

    private String readFromSaved() {
        File f = new File("saved.txt");
        String res = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));

            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line).append('\n');
                line = reader.readLine();
            }

            res = sb.toString();


        } catch (java.lang.Throwable e) {
            Log.e("ERROR BRO ", "AAAA", e);
        }

        return res;
    }

    private void writeToSaved(String text) {
        File f = new File("saved.txt");
        try {
            FileWriter fw = new FileWriter(f, false);
            fw.write(text);
            fw.close();
        } catch (IOException e) {
            Log.e("ERROR BRO ", "AAAA", e);
        }
    }
}

