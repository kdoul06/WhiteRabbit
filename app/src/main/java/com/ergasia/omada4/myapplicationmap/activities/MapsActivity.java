package com.ergasia.omada4.myapplicationmap.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ergasia.omada4.myapplicationmap.R;
import com.ergasia.omada4.myapplicationmap.entities.Poi;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.password;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("poi");

    Map<String , Marker> markers;

    private String TAG = "banana maps activity";

    private GoogleMap mMap;

    SupportMapFragment mapFragment;

    private ChildEventListener poiChildEventListener;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        markers = new HashMap();

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
       Log.v(TAG,"map ready");

        mMap = googleMap;
        //θα εχω location εδω ?

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
        intent.putExtra("key", key);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        // γιατι το ενα το βαζω πριν το super και το αλλο μετα ?

        mGoogleApiClient.connect();

        super.onStart();
        // εδω ακουω για αλλαγες στα children του poi
        // το child added σκαει για ΟΟΟΟΛΑ ΤΑ POI οταν ανοιγω την εφαρμογή.

        mAuth.addAuthStateListener(mAuthListener);

        poiChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG,"previous : " + s);
                String key = dataSnapshot.getKey();
                Log.v(TAG,"current  : " + key);

                Poi poi = dataSnapshot.getValue(Poi.class);
                Log.v(TAG, "POI ADDED :" + poi.toString());
                LatLng location = new LatLng(poi.lat, poi.lon);

                Marker m = mMap.addMarker(new MarkerOptions().position(location).title(poi.catDescr).snippet(key));
                m.setTag(poi);
                markers.put(key,m);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousKey) {

                String key = dataSnapshot.getKey();
                Poi poi = dataSnapshot.getValue(Poi.class);

                Log.v(TAG, "POI CHANGED :" + poi.toString());

                Marker marker = markers.get(key);
                marker.setTitle(poi.catDescr);
                marker.setTag(poi);

                Log.v(TAG,((Poi) marker.getTag()).catDescr);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                Poi poi = dataSnapshot.getValue(Poi.class);
                markers.get(key).remove();
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


    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                            Log.v(TAG,"login failed");
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
       Log.v(TAG,"location ready");
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
       //     mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
        //    mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            LatLng loc = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,17));
            // εμφανιζει την μπλε κουκιδα.
            mMap.setMyLocationEnabled(true);
            try {
               Address address = new Geocoder(getApplicationContext()).getFromLocation(loc.latitude,loc.longitude,1).iterator().next();
                Log.v(TAG, "address is :"+address.getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

