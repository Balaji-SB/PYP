package com.android.pyp.property;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.pyp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by devel-73 on 21/8/17.
 */

public class DetailsMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Marker marker;
    private double latitude = 0, longitude = 0;
    private String title = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initVariables(savedInstanceState);
        latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
        longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));

    }

    private void initVariables(Bundle savedInstanceState) {
        // Gets the MapView from the XML layout and creates it
        // Gets to GoogleMap from the MapView and does initialization stuff
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        MapsInitializer.initialize(this);

        MarkerOptions marker1 = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title(getIntent().getStringExtra("address"));
        marker1.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED));

        googleMap.addMarker(marker1);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();

        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }
}
