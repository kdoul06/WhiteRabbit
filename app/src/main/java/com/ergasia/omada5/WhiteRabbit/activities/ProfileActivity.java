package com.ergasia.omada5.WhiteRabbit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ergasia.omada5.WhiteRabbit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private String uid,userLogedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null && mAuth.getCurrentUser() != null) {
            uid = mAuth.getCurrentUser().getUid();
            userLogedIn = mAuth.getCurrentUser().toString();

        } else {
            uid = "anonymous";
        }





        setContentView(R.layout.activity_profile);

        TextView userName = (TextView) findViewById(R.id.userNameTxt);
        userName.setText(userLogedIn);

    }
}
