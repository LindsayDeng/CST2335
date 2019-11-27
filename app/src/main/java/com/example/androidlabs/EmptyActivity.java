package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        Bundle dataToPass = getIntent().getExtras();
        DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
        dFragment.setArguments( dataToPass ); //pass it a bundle for information
        dFragment.setTablet(false);  //tell the fragment if it's running on a tablet or not
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame, dFragment) //Add the fragment in FrameLayout
                .addToBackStack("AnyName") //make the back button undo the transaction
                .commit(); //actually load the fragment.
    }
}
