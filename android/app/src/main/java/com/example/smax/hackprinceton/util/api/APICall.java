package com.example.smax.hackprinceton.util.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class APICall {
    private StringBuilder query;
    private boolean firstParam = true;
    private APICallback callback;

    public APICall(String resource, APICallback callbackFn) {
        callback = callbackFn;
        query = new StringBuilder("https://foltik.lib.id/itinegen@dev").append(resource);
    }

    public APICall param(String key, Object o) {
        if (firstParam) {
            query.append("?");
            firstParam = false;
        } else {
            query.append("&");
        }

        query.append(key)
                .append("=")
                .append(o.toString());
        return this;
    }

    public void execute() {
        Log.e("queryurl",query.toString());
        new AsyncCall(this).execute(query.toString());
    }

    public static class AsyncCall extends AsyncTask<String, Void, JSONObject> {
        private WeakReference<APICall> callReference;

        AsyncCall(APICall call) {
            callReference = new WeakReference<>(call);
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            InputStream inStream = null;
            JSONObject object = null;
            try {
                urlConnection = (HttpURLConnection) new URL(strings[0]).openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                inStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

                String temp;
                StringBuilder response = new StringBuilder();
                while ((temp = reader.readLine()) != null)
                    response.append(temp);

                object = (JSONObject) new JSONTokener(response.toString()).nextValue();
            } catch (Exception e) {
                Log.e("APICall", e.toString());
            } finally {
                try {
                    assert inStream != null;
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                urlConnection.disconnect();
            }

            return object;
        }

        protected void onPostExecute(JSONObject result) {
            try {
                callReference.get().callback.onComplete(result);
            } catch (JSONException e) {
                Log.e("HackPrinceton", "Callback JSON Error", e);
            }
        }
    }
}
