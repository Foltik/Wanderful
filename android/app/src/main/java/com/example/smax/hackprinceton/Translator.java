package com.example.smax.hackprinceton;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Translator extends AppCompatActivity {
    private static final String API_KEY = "AIzaSyCVktAWTsOabuB_YAZdLznuEtyiZnbUlss";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiates a client
        setContentView(R.layout.activity_translator);
        TextView textView = findViewById(R.id.textview);
        JSONObject object = null;
        InputStream inStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("https://restcountries.eu/rest/v2/alpha/GBR?fields=languages");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp, response = "";
            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }
            textView.setText(response);
            object = (JSONObject) new JSONTokener(response).nextValue();
        } catch (Exception e) {
            Log.e("hackprinceton",e.toString());
        } finally {
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

        String lang = null;
        if (object != null) {
            try {
                lang = object.getJSONArray("languages").getJSONObject(0).getString("iso639_1");
            } catch (JSONException ignored) {
                Log.e("FUCK", "FUCK");
            }


            if (lang != null) {
                textView.setText(lang);

                new RunTranslation().execute("Hello, world!", "en", lang);

            }

        }
    }

    public static class RunTranslation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String str = params[0];
            String fromLang = params[1];
            String toLang = params[2];

            TranslateOptions opts = TranslateOptions.newBuilder()
                    .setApiKey(API_KEY)
                    .build();

            Translate translate = opts.getService();

            Translation translation =
                    translate.translate(
                            str,
                            TranslateOption.sourceLanguage(fromLang),
                            TranslateOption.targetLanguage(toLang));
            return translation.getTranslatedText();
        }

        protected void onPostExecute(String res) {

        }
    }
}

