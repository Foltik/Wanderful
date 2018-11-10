package com.example.smax.hackprinceton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.here.android.mpa.common.*;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.ReverseGeocodeRequest2;
import com.here.sdk.analytics.internal.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.here.android.mpa.search.ReverseGeocodeMode.RETRIEVE_ADDRESSES;


public class HomePage extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private FusedLocationProviderClient mFusedLocationClient;
    private int[] permissionsGranted;
    private String googleAPIKey = "AIzaSyDljrMJe9V4a1ae3bIBEtvmewMC7DLxh5Q";
    private TextView banner;
    class GeocodeListener implements ResultListener<List<Location>> {
        @Override
        public void onCompleted(List<Location> data, ErrorCode error) {
            if (error != ErrorCode.NONE) {
                // Handle error\
            } else {
                // Process result data
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        permissionsGranted = new int[1];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        banner = findViewById(R.id.welcomeBanner);
        initMapEngine();

    }

    private void updateCity(){
        boolean granted = requestPermsandCheck();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (granted){
            @SuppressLint("MissingPermission") Task<Location> locationTask = mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location == null) {
                                updateLocation("null");
                            } else {
                                updateLocation(location.getLatitude() + ":" + location.getLongitude());
                                GeoCoordinate coords = new GeoCoordinate(location.getLatitude(), location.getLongitude());
                                ReverseGeocodeRequest2 request = new ReverseGeocodeRequest2(coords);
                                request.execute(new ResultListener<com.here.android.mpa.search.Location>() {

                                    @Override
                                    public void onCompleted(com.here.android.mpa.search.Location location, ErrorCode errorCode) {
                                        if(errorCode == ErrorCode.NONE){
                                            String city = location.getAddress().getCity();
                                            updateLocation(city);
                                            updatePicture(city);
                                            new PhotoAPI().execute(city);

                                            /*
                                            final String code = location.getAddress().getCountryCode();
                                            findViewById(R.id.currency).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(this, Exchange.class);
                                                    intent.putExtra("COUNTRY_CODE",code);
                                                    startActivity(intent);
                                                }
                                            });
                                            findViewById(R.id.translator).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(this, Translator.class);
                                                    intent.putExtra("COUNTRY_CODE",code);
                                                    startActivity(intent);
                                                }
                                            });
                                            */
                                        }
                                        else{
                                            updateLocation("geolocation failed");
                                        }
                                    }
                                });
                            }
                        }
                    });

            //System.out.println(coords.toString());
            //updateLocation(coords.toString());
        }else{
            updateLocation("permissions needed");
        }

        /*
        request.execute(new ResultListener<com.here.android.mpa.search.Location>() {
            @Override
            public void onCompleted(com.here.android.mpa.search.Location location, ErrorCode errorCode) {
                if(errorCode == ErrorCode.NONE){
                    updateLocation(location.getAddress().getCity());
                }
                else{
                    updateLocation("geolocation failed");
                }
            }
        });
        */
        updateLocation("no error");
    }
    private boolean requestPermsandCheck() {
        final List<String> requiredSDKPermissions = new ArrayList<String>();
        requiredSDKPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        requiredSDKPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requiredSDKPermissions.add(Manifest.permission.INTERNET);
        requiredSDKPermissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        requiredSDKPermissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        boolean request = false;
        for (String s : requiredSDKPermissions) {
            if (ActivityCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED) {
                request = true;
            }
        }
        if (request)
            ActivityCompat.requestPermissions(this,
                    requiredSDKPermissions.toArray(new String[requiredSDKPermissions.size()]),
                    0);
        for (int perm : permissionsGranted) {
            if (perm != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }
    private void updateLocation(final String txt) {
        TextView banner = (TextView) findViewById(R.id.welcomeBanner);
        banner.setText("Welcome to " + txt);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        permissionsGranted = grantResults;
    }

    private void initMapEngine() {
        // Set path of isolated disk cache
        Toast.makeText(this, "test toast", Toast.LENGTH_SHORT).show();
        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
                getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps",
                "geolocation");
        //Toast.makeText(m_activity,"Intent name: " +intentName,Toast.LENGTH_SHORT).show();
        final Activity thisActivity = this;
        if (!success) {
            // Setting the isolated disk cache was not successful, please check if the path is valid and
            // ensure that it does not match the default location
            // (getExternalStorageDirectory()/.here-maps).
            // Also, ensure the provided intent name does not match the default intent name.
            Toast.makeText(thisActivity, "not successful", Toast.LENGTH_SHORT).show();
        } else {
            /*
             * Even though we don't display a map view in this application, in order to access any
             * services that HERE Android SDK provides, the MapEngine must be initialized as the
             * prerequisite.
             */
            MapEngine.getInstance().init(new ApplicationContext(thisActivity), new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(Error error) {
                    updateCity();
                }
            });
        }
    }
    private void updatePicture(String city){

    }
    public class PhotoAPI extends AsyncTask<String, Void, Bitmap>{
        private String googleAPIKey = "AIzaSyDljrMJe9V4a1ae3bIBEtvmewMC7DLxh5Q";
        @Override
        protected Bitmap doInBackground(String... params) {
            StringBuilder request = new StringBuilder();
            request.append("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?")
            .append("input=").append(params[0])
            .append("&inputtype=textquery&fields=photos")
            .append("&key=").append(googleAPIKey);
            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;
            InputStream inStream = null;
            try{
                url = new URL(request.toString());
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
                object = (JSONObject) new JSONTokener(response).nextValue();
            }catch(Exception e){

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
            String imagereference = null;
            if(object != null) {
                try {
                    imagereference = object.getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONArray("photos")
                            .getJSONObject(0)
                            .getString("photo_reference");
                } catch (JSONException e) {
                    Log.e("hackprinceton", e.getMessage());
                }
            }
            if (imagereference != null){
                object = null;
                StringBuilder imageURLAPI = new StringBuilder();
                imageURLAPI.append("https://maps.googleapis.com/maps/api/place/photo?")
                        .append("maxwidth=800")
                        .append("&photoreference=").append(imagereference)
                        .append("&key=").append(googleAPIKey);
                Bitmap bannerimage = null;
                try {
                    InputStream in = new java.net.URL(imageURLAPI.toString()).openStream();
                    bannerimage = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return bannerimage;
            }else return null;
        }
        protected void onPostExecute(Bitmap response){
            ImageView image = findViewById(R.id.welcomeBannerImage);
            image.setImageBitmap(response);
        }
    }
}

