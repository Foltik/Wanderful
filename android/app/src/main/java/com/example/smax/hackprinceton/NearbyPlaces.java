package com.example.smax.hackprinceton;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

public class NearbyPlaces extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;
    private LinearLayout entry;
    private LinearLayout entryList;
    private LayoutInflater inflater;
    private ArrayList<String> entryNameURLs;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        entryNameURLs = new ArrayList<String>();
        setContentView(R.layout.activity_nearby);
        entryList = findViewById(R.id.nearbyList);
        entry = findViewById(R.id.nearbyEntry);
        entryList.removeView(entry);
        inflater = LayoutInflater.from(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                new stdlibAPICall("/nearby", new stdlibAPICallback() {
                    @Override
                    public void onComplete(JSONObject result) {
                        Log.e("nearby", result.toString());
                        try {
                            JSONArray entries = result.getJSONArray("result");
                            for(int i = 0; i<entries.length(); i++){
                                addEntry(entries.getJSONObject(i));
                            }
                        } catch (JSONException e) {
                            Log.e("hackprinceton",e.toString());
                        }
                    }
                }).add("num",new Integer(10)).add("location",location.getLatitude()+","+location.getLongitude()).execute();
            }
        });
        new setImage().execute(entryNameURLs.toArray(new String[entryNameURLs.size()]));
    }
    private void addEntry(JSONObject place){
        try {
            LinearLayout newEntry = (LinearLayout) inflater.inflate(R.layout.place_entry, null);
            String entryName = place.getString("name");
            TextView title = (TextView) ((GridLayout) entry.getChildAt(1)).getChildAt(0);
            title.setText(entryName);
            TextView rating = (TextView) ((GridLayout) entry.getChildAt(1)).getChildAt(1);
            rating.setText(place.getString("rating"));
            entryList.addView(newEntry);
            new stdlibAPICall("/photo", new stdlibAPICallback() {
                @Override
                public void onComplete(JSONObject result) {
                    try {
                        entryNameURLs.add(result.getString("result"));
                    }catch(Exception e){
                        Log.e("hackprinceton",e.toString());
                    }
                }
            }).add("place",entryName).execute();

        }catch(Exception e){
            Log.e("hackprinceton",e.toString());
        }
    }
    public class setImage extends AsyncTask<String, Void, ArrayList<Bitmap>> {
        @Override
        protected ArrayList<Bitmap> doInBackground(String... strings) {
            ArrayList<Bitmap> pictures = new ArrayList<>();
            for(String urlStr : strings){
                try{
                    URL url = new URL(urlStr);
                    pictures.add( BitmapFactory.decodeStream((InputStream) url.getContent()));
                }
                catch (Exception e){
                    Log.e("err",e.toString());
                }
            }
            return pictures;
        }
        protected void onPostExecute(ArrayList<Bitmap> images){
            for(int i = 0; i<images.size(); i++){
                ImageView current = (ImageView)((LinearLayout)entryList.getChildAt(i)).getChildAt(0);
                current.setImageBitmap(images.get(i));
            }
        }
    }
}
