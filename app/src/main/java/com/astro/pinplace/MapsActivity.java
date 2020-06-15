package com.astro.pinplace;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapLongClickListener(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                Log.i("Location", location.toString());
//                Log.i("Latitude", String.valueOf(location.getLatitude()));
                // Add a marker in Sydney and move the camera

                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));




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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000, locationListener);
        }

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("Boring").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

//        intent.putExtra("lat",latLng.latitude);

//        Toast.makeText(this, String.valueOf(latLng.latitude), Toast.LENGTH_SHORT).show();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addre = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
//            Toast.makeText(this, String.valueOf(addre.get(0)), Toast.LENGTH_SHORT).show();
            Log.i("Adress",String.valueOf(addre.get(0)));
            String adress ="";

            if(addre.get(0).getAdminArea() != null){
//                Toast.makeText(this,addre.get(0).getAdminArea(), Toast.LENGTH_SHORT).show();
                adress+=String.valueOf(addre.get(0).getAddressLine(0));
                Log.i("Adress Line",adress);
                MainActivity.arrayList.add(adress);
                MainActivity.latitudeList.add(String.valueOf(latLng.latitude));
                MainActivity.longitudeList.add(String.valueOf(latLng.longitude));

                SharedPreferences sharedPreferences = this.getSharedPreferences("com.astro.pinplace", Context.MODE_PRIVATE);

                try {





                    sharedPreferences.edit().putString("places", ObjectSerializer.serialize(MainActivity.arrayList)).apply();
                    sharedPreferences.edit().putString("lats", ObjectSerializer.serialize(MainActivity.latitudeList)).apply();
                    sharedPreferences.edit().putString("lons", ObjectSerializer.serialize(MainActivity.longitudeList)).apply();


                } catch (Exception e) {
                    e.printStackTrace();
                }











                MainActivity.arrayAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Location Saved!", Toast.LENGTH_SHORT).show();
//                Intent goback = new Intent(this,MainActivity.class);


//                goback.putExtra("state",addre.get(0).getAdminArea());
                /*goback.putExtra("state",adress);
                goback.putExtra("lat",String.valueOf(latLng.latitude));
                goback.putExtra("lon",String.valueOf(latLng.longitude));*/
//                startActivity(goback);
//                Toast.makeText(this,String.valueOf(latLng), Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
