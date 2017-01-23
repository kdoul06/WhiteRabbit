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
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;




public class PoiActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("poi");

    FirebaseAuth mAuth =  FirebaseAuth.getInstance();

    private TextView addressView;

    private Spinner categorySpiner ;

    private Poi poi;

    private String key;

    private String TAG = "banana poi activity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);

       // mAuth = FirebaseAuth.getInstance();

        Log.v(TAG,mAuth.getCurrentUser().getDisplayName());



        addressView = (TextView) findViewById(R.id.addressView);
        categorySpiner = (Spinner) findViewById(R.id.categorySpiner);
//
//        ArrayAdapter categoriesAdapter = new ArrayAdapter<Category>
//                (this, android.R.layout.simple_list_item_1, walletDAO.getCategories());
//        categoriesSpin.setAdapter(categoriesAdapter);

        String[] categoryArray = {"ΠΑΙΔΙΚΗ ΧΑΡΑ","ΠΑΡΚΟ","ΑΛΣΟΣ","ΑΛΑΝΑ"};

        ArrayAdapter categoryAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,categoryArray);

        categorySpiner.setAdapter(categoryAdapter);
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
        //poi.catDescr = categorySpiner.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Log.v(TAG,"current user is null");
        } else {
            poi.userId = currentUser.getDisplayName();
        }

        myRef.child(key).setValue(poi);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        poi.catDescr = (String) parent.getItemAtPosition(position);
        Log.v(TAG,"selected :" +poi.catDescr);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        poi.catDescr = (String) parent.getSelectedItem();
    }
}
