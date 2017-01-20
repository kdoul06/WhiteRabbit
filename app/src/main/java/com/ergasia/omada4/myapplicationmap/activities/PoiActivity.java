package com.ergasia.omada4.myapplicationmap.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ergasia.omada4.myapplicationmap.entities.Poi;
import com.ergasia.omada4.myapplicationmap.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PoiActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("poi");
    LatLng loc;
    EditText catTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        catTxt = (EditText) findViewById(R.id.catTxt);
        //Intent intent = getIntent();
        //LatLng loc = (LatLng) intent.getExtras().getSerializable("Location");



    //    loc = (LatLng) getIntent().getParcelableExtra("Location");


    }

    public void onAddBtnClick(View view)  {
        String key = myRef.push().getKey();

        myRef.child(key).setValue(new Poi("1",loc,"1",catTxt.getText().toString()));
        finish();

    }

}
