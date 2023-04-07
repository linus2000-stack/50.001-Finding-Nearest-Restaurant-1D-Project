package com.example.a500011dproject;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class MapsActivity extends AppCompatActivity {
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationClient;
    int requestCode = 100;
    Location userLocation;
    GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        String apiKey = BuildConfig.MAPS_API_KEY;
        Places.initialize(getApplicationContext(), apiKey);
        PlacesClient placesClient = Places.createClient(this);


        // Created an instance of the Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    public void onSuccess(Location location) {
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                if (location != null) {
                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Current Location!");
                                    googleMap.addMarker(markerOptions);
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                                    // Call AsyncTask -> moving network request to background thread due to android.os.NetworkOnMainThreadException
                                    String latLngString = Double.toString(latLng.latitude) + "," + Double.toString(latLng.longitude);
                                    new GetNearbyPlacesTask(googleMap,latLngString,location).execute();

                                } else {
                                    Toast.makeText(MapsActivity.this, "Please turn on your Location App Permissions", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
    }



    /**
     * Callback method that gets called when the user responds to the permissions dialog
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("TAG", "onRequestPermissionsResults() ran");
        if (requestCode == this.requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, start using the location
                // ...
            } else {
                // Permission is denied, show an explanation or disable the functionality
                // ...
                Toast.makeText(MapsActivity.this, "HungryAlpacas can't find you...", Toast.LENGTH_LONG).show();
            }
        }
    }
}