package com.example.smax.hackprinceton;

import android.os.AsyncTask;

public class stdlibAPICall<T> {
    private String apifunction;
    private StringBuilder query;
    private boolean firstParam = true;
    stdlibAPICallback<T> callbackFunc;
    public stdlibAPICall(String function, stdlibAPICallback<T> callback){
        query = new StringBuilder();
        apifunction = function;
        callbackFunc = callback;
        query.append("https://foltik.lib.id/itinegen@dev/")
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

    public void execute() {
        new APICall().execute(query.toString());
    }
    public class APICall extends AsyncTask<String, Void, T>{

        @Override
        protected T doInBackground(String... strings) {
            return null;
        }
        protected void onPostExecute(T result){
            callbackFunc.onComplete(result);
        }
    }
}
