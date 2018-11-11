package com.example.smax.hackprinceton;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class stdlibAPICall {
    private String apifunction;
    private StringBuilder query;
    private boolean firstParam = true;
    stdlibAPICallback callbackFunc;
    public stdlibAPICall(String function, stdlibAPICallback callback){
        query = new StringBuilder();
        apifunction = function;
        callbackFunc = callback;
        query.append("https://foltik.lib.id/itinegen@dev")
            .append(function);
    }
    public stdlibAPICall add(String key, Object o){
        if (firstParam)
            query.append("?");
        else
            query.append("&");

        query.append(key)
                .append("=")
                .append(o.toString());
        return this;
    }

    public String execute() {
        new APICall().execute(query.toString());
        return query.toString();
    }
    public class APICall extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;
            InputStream inStream = null;
            try{
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                inStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                String temp, response = "";
                while ((temp = reader.readLine()) != null) {
                    response += temp;
                }
                Log.e("response length", ""+response.length());
                object = (JSONObject) new JSONTokener(response).nextValue();
            }catch(Exception e){
                Log.e("exception", e.toString());
            }finally {
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
            Log.e("object?: ", object != null ? object.toString() : "it was null fuq");
            return object;
        }
        protected void onPostExecute(JSONObject result){
            callbackFunc.onComplete(result);
        }
    }
}
