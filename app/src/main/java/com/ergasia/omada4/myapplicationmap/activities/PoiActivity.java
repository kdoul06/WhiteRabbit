package com.ergasia.omada4.myapplicationmap.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ergasia.omada4.myapplicationmap.R;
import com.ergasia.omada4.myapplicationmap.entities.Poi;
import com.ergasia.omada4.myapplicationmap.Services.GeoService;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;



public class PoiActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("poi");

    private EditText catTxt;
    public TextView addressView;

    private Poi poi;

    private String key;

    private String TAG = "banana poi activity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        catTxt = (EditText) findViewById(R.id.catTxt);
        addressView = (TextView) findViewById(R.id.addressView);
        poi = (Poi) getIntent().getSerializableExtra("poi");
        key = getIntent().getStringExtra("key");

       // Log.v(TAG, "poi received :" + poi.toString());

        if (key == null) {
            Log.v(TAG, "poi creating new key");
            key = myRef.push().getKey();
        } else {
            Log.v(TAG, "editing existing poi");
            catTxt.setText(poi.catDescr);
        }

        String address = GeoService.getAddress(this, poi.lat, poi.lon);
      //  Log.v(TAG,"POI Adress is " + address);
        addressView.setText(address);
    }

    public void onAddBtnClick(View view) {
        // εξακολουθω να θελω να ειναι popup ...
        poi.catDescr = catTxt.getText().toString();
        myRef.child(key).setValue(poi);
        finish();
    }

}
