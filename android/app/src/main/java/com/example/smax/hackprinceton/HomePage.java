package com.example.smax.hackprinceton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smax.hackprinceton.util.api.APICall;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.here.android.mpa.common.*;
import com.here.android.mpa.search.Location;
import com.here.android.mpa.search.ReverseGeocodeRequest2;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HomePage extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;

    private int[] permissions;
    private boolean permissionsGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        permissions = new int[1];
        permissionsGranted = requestPermissions();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        initMapEngine(() -> {
            updateLocation(location -> {
                lastLocation = location;

                findViewById(R.id.currency).setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), Exchange.class);
                    intent.putExtra("COUNTRY_CODE", location.getAddress().getCountryCode());
                    startActivity(intent);
                });

                findViewById(R.id.nearby).setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), NearbyPlaces.class);
                    startActivity(intent);
                });

                updateCity();
            });
        });
    }

    private interface UpdateLocationCallback {
        void onComplete(Location location);
    }

    @SuppressLint("MissingPermission")
    private void updateLocation(UpdateLocationCallback callback) {
        if (!permissionsGranted)
            return;

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, absLocation -> {
            if (absLocation == null) {
                Log.d("Location", "Location was null!");
                return;
            }

            GeoCoordinate coords = new GeoCoordinate(absLocation.getLatitude(),
                                                     absLocation.getLongitude());

            new ReverseGeocodeRequest2(coords).execute((location, errorCode) ->
                    callback.onComplete(location));
        });
    }


    private void updateCity() {
        if (!permissionsGranted)
            return;

        TextView banner = findViewById(R.id.welcomeBanner);
        banner.setText("Welcome to " + lastLocation.getAddress().getCity());

        Log.d("APICall", "Starting API Call");

        new APICall("/photo", result -> {
            Log.d("APICall", result.getString("result"));
            new SetCityImageFromURL(this).execute(result.getString("result"));
        }).param("place", lastLocation.getAddress().getCity()).execute();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        this.permissions = grantResults;
    }

    private interface MapEngineInitCompleteCallback {
        void onComplete();
    }

    private void initMapEngine(MapEngineInitCompleteCallback callback) {
        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
                getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps",
                "geolocation");
        final Activity thisActivity = this;
        if (!success) {
            Toast.makeText(thisActivity, "MapEngine Init Failed", Toast.LENGTH_SHORT).show();
        } else {
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


