package com.ergasia.omada4.myapplicationmap.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ergasia.omada4.myapplicationmap.entities.Poi;
import com.ergasia.omada4.myapplicationmap.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PoiActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("poi");

    private EditText catTxt;

    private Poi poi;

    private String key;

    private String TAG = "banana poi activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        catTxt = (EditText) findViewById(R.id.catTxt);
        poi = (Poi) getIntent().getSerializableExtra("poi");
        key = getIntent().getStringExtra("key");

        Log.v(TAG, "poi received :" + poi.toString());

        if (key == null) {
            Log.v(TAG, "poi creating new key");
            key = myRef.push().getKey();
        } else {
            catTxt.setText(poi.catDescr);
        }

    }

    public void onAddBtnClick(View view) {
        // εξακολουθω να θελω να ειναι popup ...

        poi.catDescr = catTxt.getText().toString();
        myRef.child(key).setValue(poi);
        finish();
    }

}
