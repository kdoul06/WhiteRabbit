package com.ergasia.omada4.myapplicationmap.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ergasia.omada4.myapplicationmap.R;
import com.ergasia.omada4.myapplicationmap.entities.Poi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("poi");

    private String TAG = "banana maps activity";

    private GoogleMap mMap;

    SupportMapFragment mapFragment;

    ChildEventListener poiChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        GoogleMap.InfoWindowAdapter myInfoAdapter = new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                ImageView image = new ImageView(getApplicationContext());
                image.setImageResource(R.mipmap.playground);
                return image;
            }
        };

        mMap.setInfoWindowAdapter(myInfoAdapter);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addPoi(latLng);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                viewPoi(marker);

                // Return false to indicate that we have not consumed the event and that we wish
                // for the default behavior to occur (which is for the camera to move such that the
                // marker is centered and for the marker's info window to open, if it has one).

                return false;
            }
        });


        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(poi[1].location,15));


    }


    private void addPoi(LatLng latLng) {
        Log.v(TAG, "creating new poi at " + latLng);
        Poi poi = new Poi();
        poi.userId = "ΑΓΝΩΣΤΟΣ";
        poi.lat = latLng.latitude;
        poi.lon = latLng.longitude;
        Intent intent = new Intent(this, PoiActivity.class);
        intent.putExtra("poi", poi);
        //intent.putExtra("key", "0");
        startActivity(intent);


    }

    private void viewPoi(Marker marker) {
        Poi poi = (Poi) marker.getTag();
        String key = marker.getSnippet();
        Intent intent = new Intent(this, PoiActivity.class);
        intent.putExtra("poi", poi);
        intent.putExtra("key",key);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // εδω ακουω για αλλαγες στα children του poi
        // το child added σκαει για ΟΟΟΟΛΑ ΤΑ POI οταν ανοιγω την εφαρμογή.
        poiChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                Poi poi = dataSnapshot.getValue(Poi.class);
                Log.v(TAG, "POI ADDED :" + poi.toString());
                LatLng location = new LatLng(poi.lat,poi.lon);
                mMap.addMarker(new MarkerOptions().position(location).title(poi.catDescr).snippet(key)).setTag(poi);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Poi poi = dataSnapshot.getValue(Poi.class);

                Log.v(TAG, "POI CHANGED :" + poi.toString());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        myRef.addChildEventListener(poiChildEventListener);


    }


    @Override
    protected void onStop() {
        super.onStop();
        myRef.removeEventListener(poiChildEventListener);
    }
}

