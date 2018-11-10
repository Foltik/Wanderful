package com.example.smax.hackprinceton;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.here.android.mpa.common.*;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.ReverseGeocodeRequest2;

import java.util.List;

import static com.here.android.mpa.search.ReverseGeocodeMode.RETRIEVE_ADDRESSES;


public class HomePage extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        TextView welcomeBanner = (TextView) findViewById(R.id.welcomeBanner);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String cityName;
        Task<Location> locationTask = mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                        }
                    }
                });
        Location coords = locationTask.getResult();
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
    }
    private void updateLocation(final String txt){
        TextView banner = (TextView) findViewById(R.id.welcomeBanner);
        banner.setText("Welcome to "+txt);
    }
}

