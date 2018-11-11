package com.example.smax.hackprinceton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HomePage extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;
    private com.here.android.mpa.search.Location lastGeocodedLocation;

    private int[] permissions;
    private boolean permissionsGranted;

    private TextView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        permissions = new int[1];
        permissionsGranted = requestPermissions();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        banner = findViewById(R.id.welcomeBanner);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        initMapEngine(() -> {
            updateLocation((location, geocoded) -> {
                lastLocation = location;
                lastGeocodedLocation = geocoded;

                findViewById(R.id.currency).setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), Exchange.class);
                    intent.putExtra("COUNTRY_CODE", geocoded.getAddress().getCountryCode());
                    startActivity(intent);
                });

                updateCity2();
            });
        });
    }

    private interface UpdateLocationCallback {
        void onComplete(Location location, com.here.android.mpa.search.Location geocoded);
    }

    private void updateLocation(UpdateLocationCallback callback) {
        if (!permissionsGranted)
            return;

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location == null)
                return;

            GeoCoordinate coords = new GeoCoordinate(location.getLatitude(), location.getLongitude());
            new ReverseGeocodeRequest2(coords).execute((geocodedLocation, errorCode) -> {
                callback.onComplete(location, geocodedLocation);
            });
        });
    }

    private void updateCity2() {
        if (!permissionsGranted)
            return;

        TextView banner = findViewById(R.id.welcomeBanner);
        banner.setText("Welcome to " + lastGeocodedLocation.getAddress().getCity());

        Log.d("APICall", "Starting API Call");

        new APICall("/photo", result -> {
            Log.d("APICall", result.getString("result"));
            new SetCityImageFromURL(this).execute(result.getString("result"));
        }).param("place", lastGeocodedLocation.getAddress().getCity()).execute();

    }

    private void updateCity(){
        boolean granted = requestPermissions();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (granted){
            @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationClient.getLastLocation()
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

                                            final String code = location.getAddress().getCountryCode();
                                            findViewById(R.id.currency).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(v.getContext(), Exchange.class);
                                                    intent.putExtra("COUNTRY_CODE",code);
                                                    startActivity(intent);
                                                }
                                            });
                                            /*
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

    private boolean requestPermissions() {
        final List<String> requiredSDKPermissions = new ArrayList<String>();
        requiredSDKPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        requiredSDKPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requiredSDKPermissions.add(Manifest.permission.INTERNET);
        requiredSDKPermissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        requiredSDKPermissions.add(Manifest.permission.ACCESS_NETWORK_STATE);

        boolean needGrant = false;
        for (String s : requiredSDKPermissions)
            if (ActivityCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED)
                needGrant = true;

        if (needGrant)
            ActivityCompat.requestPermissions(this,
                    requiredSDKPermissions.toArray(new String[0]),
                    0);

        for (int perm : permissions)
            if (perm != PackageManager.PERMISSION_GRANTED) return false;

        return true;
    }
    private void updateLocation(final String txt) {
        TextView banner = (TextView) findViewById(R.id.welcomeBanner);
        banner.setText("Welcome to " + txt);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        this.permissions = grantResults;
    }

    private interface MapEngineInitCompleteCallback {
        void onComplete();
    }

    private void initMapEngine(MapEngineInitCompleteCallback callback) {
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
            MapEngine.getInstance().init(new ApplicationContext(thisActivity), error -> callback.onComplete());
        }
    }

    private static class SetCityImageFromURL extends AsyncTask<String, Void, Bitmap> {
        private WeakReference<HomePage> activityReference;

        SetCityImageFromURL(HomePage context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Log.d("SetPhoto", strings[0]);
            InputStream stream = null;
            try {
                URL url = new URL(strings[0]);
                stream = (InputStream)url.getContent();
            } catch (java.lang.Throwable e) {
                Log.e("SetCityImageFromURL", e.toString());
            }
            return BitmapFactory.decodeStream(stream);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView image = activityReference.get().findViewById(R.id.welcomeBannerImage);
            image.setImageBitmap(bitmap);
        }
    }
}


