package com.example.smax.hackprinceton;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.io.File;
import java.util.List;

import static com.here.android.mpa.search.ReverseGeocodeMode.RETRIEVE_ADDRESSES;


public class HomePage extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private FusedLocationProviderClient mFusedLocationClient;
    private int[] permissionsGranted;
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
        //initMapEngine();
        TextView welcomeBanner = (TextView) findViewById(R.id.welcomeBanner);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if(permissionsGranted[0] == PackageManager.PERMISSION_GRANTED) {
            Task<Location> locationTask = mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location == null) {
                                updateLocation("null");
                            }else{
                                updateLocation(location.toString());
                                //updateCity(location);
                            }
                        }
                    });

            //System.out.println(coords.toString());
            //updateLocation(coords.toString());
        }
        else{
            updateLocation("not found");
        }
        /*
        GeoCoordinate geoCoordinate = new GeoCoordinate(coords.getLatitude(), coords.getLongitude());
        ReverseGeocodeRequest2 request = new ReverseGeocodeRequest2(geoCoordinate);
        request.execute(new ResultListener<com.here.android.mpa.search.Location>() {
            @Override
            public void onCompleted(com.here.android.mpa.search.Location location, ErrorCode errorCode) {
                if (errorCode==ErrorCode.NONE) {
                    updateLocation(location.getAddress().getCity());
                }
            }
        });
        */
    }
    private void updateCity(Location location){
        GeoCoordinate coords = new GeoCoordinate(location.getLatitude(), location.getLongitude());
        ReverseGeocodeRequest2 request = new ReverseGeocodeRequest2(coords);
        request.execute(new ResultListener<com.here.android.mpa.search.Location>() {
            @Override
            public void onCompleted(com.here.android.mpa.search.Location location, ErrorCode errorCode) {
                if(errorCode == ErrorCode.NONE){
                    updateLocation(location.getAddress().getCity());
                }
            }
        });
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
                    Toast.makeText(thisActivity, "Map Engine initialized with error code:" + error,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

