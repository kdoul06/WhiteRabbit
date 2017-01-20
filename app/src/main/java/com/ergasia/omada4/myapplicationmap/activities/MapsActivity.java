package com.ergasia.omada4.myapplicationmap.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.ergasia.omada4.myapplicationmap.R;
import com.ergasia.omada4.myapplicationmap.entities.Poi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String TAG = "map_activity";

    private GoogleMap mMap;

    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
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

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                addPoi(latLng);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.v(TAG,"ekana klik panw stin pineza " + marker.getTitle());

                viewPoi((Poi)marker.getTag());
                return false;
            }
        });



        Poi[] poi = {   new Poi("1", new LatLng(37.9891977,23.7182692),"1","PAIDIKH"),
                        new Poi("2", new LatLng(37.9891123,23.7182222),"2","8EATRO")
                    };

       for (Poi l : poi) {

           mMap.addMarker(new MarkerOptions().position(l.location).title("Pineza " + l.catDescr)).setTag(l);//.title("Marker in Sydney"));

       }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(poi[1].location,15));
        
        
    }

    private void addPoi(LatLng latLng) {
        Log.v(TAG,"lat :  "+ latLng.toString());

        Poi poi = new Poi();
        poi.location=latLng;
        Intent intent = new Intent(this, PoiActivity.class);
        intent.putExtra("poi", poi);
        intent.putExtra("key", "000000000000");
        startActivity(intent);


    }

    private void viewPoi (Poi poi) {

        Intent intent = new Intent(this, PoiActivity.class);
        intent.putExtra("poi", (Parcelable) poi);
        startActivity(intent);
    }
}
