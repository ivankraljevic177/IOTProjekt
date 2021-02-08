package com.example.iotprojekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private final String LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private final int PERMISSIONS_REQUEST_CODE = 1240;
    private TextView locationLabel;

    private LocationListener locationListener;
    private LocationManager locationManager;
    private static boolean resume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPermissions();

        locationLabel = findViewById(R.id.locationLabel);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                StringBuilder sb = new StringBuilder();

                sb.append(String.format("φ: %.6f °", location.getLatitude())).append("\n\n");
                sb.append(String.format("λ: %.6f °", location.getLongitude()));

                locationLabel.setText(sb.toString());
                LeshanClientDemo.moveLocation(location.getLatitude(),location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager = (LocationManager) getSystemService (Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        try {
            if (provider != null) {
                locationManager.requestLocationUpdates(provider, 1000, 50, locationListener);
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Greška u dozvolama", Toast.LENGTH_SHORT).show();
        }
        resume = true;
    }
    public void checkAndRequestPermissions() {
        if(ContextCompat.checkSelfPermission(this, LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{LOCATION_PERMISSION}, PERMISSIONS_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, LOCATION_PERMISSION)) {
                checkAndRequestPermissions();
            } else {
                Toast.makeText(this, "Molimo omogućite dozvole u postavkama", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
            locationManager = null;
            locationListener = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (resume) {
            resume = false;
            String[] a = {};
            LeshanClientDemo.init(a);
        }
    }
}