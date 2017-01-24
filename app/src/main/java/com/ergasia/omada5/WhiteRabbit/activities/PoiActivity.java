package com.ergasia.omada5.WhiteRabbit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ergasia.omada5.WhiteRabbit.services.GeoService;
import com.ergasia.omada5.WhiteRabbit.entities.Poi;
import com.ergasia.omada5.WhiteRabbit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class PoiActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("poi");
    private DatabaseReference categoryRef = database.getReference("category");

    FirebaseAuth mAuth;

    private TextView addressView;

    private Spinner categorySpiner;

    private Poi poi;

    private String key;

    private String TAG = "banana poi activity";

    private ChildEventListener categoryListener;

    private List<String> categoryList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);

        mAuth = FirebaseAuth.getInstance();

        addressView = (TextView) findViewById(R.id.addressView);
        categorySpiner = (Spinner) findViewById(R.id.categorySpiner);

        categorySpiner.setOnItemSelectedListener(this);

        poi = (Poi) getIntent().getSerializableExtra("poi");
        key = getIntent().getStringExtra("key");

        // Log.v(TAG, "poi received :" + poi.toString());

        if (key == null) {
            Log.v(TAG, "poi creating new key");
            key = myRef.push().getKey();
        } else {
            Log.v(TAG, "editing existing poi");
        }

        String address = GeoService.getAddress(this, poi.lat, poi.lon);
        //  Log.v(TAG,"POI Adress is " + address);
        addressView.setText(address);
    }

    public void onAddBtnClick(View view) {
        // εξακολουθω να θελω να ειναι popup ...
        poi.uid = mAuth.getCurrentUser().getUid();

        Log.v(TAG, poi.category);
        myRef.child(key).setValue(poi);
        finish();
    }


    private void updateCategories() {
        categorySpiner.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1, categoryList));
    }

    @Override
    protected void onStart() {
        super.onStart();

        categoryListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String categoryKey = dataSnapshot.getKey();
                String category = (String) dataSnapshot.getValue();
                Log.v(TAG,"adding category" + category);
                categoryList.add(category);
                updateCategories();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

        categoryRef.addChildEventListener(categoryListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        categoryRef.removeEventListener(categoryListener);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.v(TAG, "eskasa");
        poi.category = (String) parent.getItemAtPosition(position);
        Log.v(TAG, poi.category);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
