package com.ergasia.omada5.WhiteRabbit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.ergasia.omada5.WhiteRabbit.R;
import com.ergasia.omada5.WhiteRabbit.entities.Poi;
import com.ergasia.omada5.WhiteRabbit.Services.GeoService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class PoiActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("poi");
    private DatabaseReference categoryRef = database.getReference("category");

    FirebaseAuth mAuth;

    private TextView addressView, catTxt;
    private Spinner categorySpiner;

    private Poi poi;
    private String key;

    private String TAG = "banana poi activity";

    private ChildEventListener categoryListener;

    private List<String> categoryList = new ArrayList();
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_dtl_0_master);

//        TabHost host = (TabHost)findViewById(R.id.tab_host);
//        host.setup();
//
//        //Tab 1
//        TabHost.TabSpec spec = host.newTabSpec("Tab One");
//        spec.setContent(R.id.tab1);
//        spec.setIndicator("Tab One");
//        host.addTab(spec);
//
//        //Tab 2
//        spec = host.newTabSpec("Tab Two");
//        spec.setContent(R.id.tab2);
//        spec.setIndicator("Tab Two");
//        host.addTab(spec);
//
//        //Tab 3
//        spec = host.newTabSpec("Tab Three");
//        spec.setContent(R.id.tab3);
//        spec.setIndicator("Tab Three");
//        host.addTab(spec);



        mAuth = FirebaseAuth.getInstance();

        addressView = (TextView) findViewById(R.id.addressView);
        categorySpiner = (Spinner) findViewById(R.id.categorySpiner);
        catTxt = (TextView) findViewById(R.id.categoryTxt);
        categorySpiner.setOnItemSelectedListener(this);

        poi = (Poi) getIntent().getSerializableExtra("poi");
        key = getIntent().getStringExtra("key");

        // Log.v(TAG, "poi received :" + poi.toString());

        if (key == null) {
            Log.v(TAG, "poi creating new key");
            key = myRef.push().getKey();
        } else {
            Log.v(TAG, "editing existing poi");
            int i = categoryList.indexOf(poi.category);
            categorySpiner.setSelection(i);
        }

        String address = GeoService.getAddress(this, poi.lat, poi.lon);
        //  Log.v(TAG,"POI Adress is " + address);
        addressView.setText(address);
        catTxt.setText(poi.category);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    public void onAddBtnClick(View view) {
        // εξακολουθω να θελω να ειναι popup ...
        Toast.makeText(this, "ΕΙΣΑΙ ΕΔΩ ΓΙΑ ΛΙΓΟ ....", Toast.LENGTH_LONG).show();
        poi.uid = mAuth.getCurrentUser().getUid();
//
        Log.v(TAG, poi.category);

        //Toast.makeText(this, "ΠΑΤΗΣΑΤΕ ΤΟ ΚΟΥΜΠΙ ΑΛΛΑ Η ΚΑΤΑΧΩΡΗΣΗ ΔΕΝ ΕΓΙΝΕ", Toast.LENGTH_LONG).show();
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

                if (poi.category != null) {
                    if (poi.category.equals(category) ) {
                        Log.v(TAG,"category was found !!!");
                        categorySpiner.setSelection(categoryList.size());
                    }
                } else {
                    Log.v(TAG,"category is NULL !!!");
                }
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
        poi.category = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        poi.category = (String) parent.getSelectedItem();
    }
}
