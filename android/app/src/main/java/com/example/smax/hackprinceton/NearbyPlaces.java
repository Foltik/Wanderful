package com.example.smax.hackprinceton;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smax.hackprinceton.util.api.APICall;
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
import java.util.Iterator;

public class NearbyPlaces extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;
    private LinearLayout entry;
    private LinearLayout entryList;
    private LayoutInflater inflater;
    private ArrayList<String> entryNameURLs;
    private ArrayList<Integer> listEntryIDs;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        int numPlacesGenerated = 7;
        super.onCreate(savedInstanceState);
        entryNameURLs = new ArrayList<String>();
        setContentView(R.layout.activity_nearby);
        entryList = findViewById(R.id.nearbyList);
        listEntryIDs = new ArrayList<>();
        entry = findViewById(R.id.nearbyEntry);
        inflater = LayoutInflater.from(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                new APICall("/nearby", result -> {
                    JSONArray entries = result.getJSONArray("result");
                    for(int i = 0; i<entries.length(); i++){
                        Log.e("nearbyentry", entries.getJSONObject(i).toString());
                        addEntry(entries.getJSONObject(i));
                    }
                    Log.e("urls", ""+entryNameURLs.size());
                }).param("num", new Integer(numPlacesGenerated))
                        .param("location",location.getLatitude()+","+location.getLongitude())
                        .param("radius", new Integer(4000))
                        .execute();
            }
        });


    }
    private void addEntry(JSONObject place){
        try {
            LinearLayout newEntry = (LinearLayout) inflater.inflate(R.layout.place_entry,null);
            String entryName = place.getString("name");
            TextView title = (TextView) ((GridLayout) newEntry.getChildAt(1)).getChildAt(0);
            title.setText(entryName);
            TextView rating = (TextView) ((GridLayout) newEntry.getChildAt(1)).getChildAt(1);
            rating.setText(place.getString("rating"));
            int id = View.generateViewId();
            newEntry.setId(id);
            listEntryIDs.add(id);
            entryNameURLs.add(place.getString("photo"));
            entryList.addView(newEntry);
            new APICall("/photoid", result -> {
                String[] query = {result.getString("result"),""+id};
                Log.e("result", query[0]);
                new setImage().execute(query);
            }).param("reference", place.getString("photo")).param("max_width",800).execute();



        }catch(Exception e){
            Log.e("hackprinceton",e.toString());
        }
    }
    public class setImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        @Override
        protected Bitmap doInBackground(String... strings) {
           imageView = (ImageView)((LinearLayout)(findViewById(Integer.parseInt(strings[1])))).getChildAt(0);
           InputStream input = null;
           try{
               URL url = new URL(strings[0]);
               input = (InputStream)url.getContent();

           }catch(Exception e){
               Log.e("setimage",e.toString());
           }
           return BitmapFactory.decodeStream(input);
        }
        protected void onPostExecute(Bitmap image){
            /*
            for(int i = 0; i<images.size(); i++){
                LinearLayout entry = findViewById(listEntryIDs.get(i));
                ImageView current = (ImageView)entry.getChildAt(0);
                current.setImageBitmap(images.get(i));
            }*/
            imageView.setImageBitmap(image);
        }
    }
}
