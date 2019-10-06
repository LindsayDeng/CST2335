package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

public class ProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    protected ImageView mImageButton;



    protected void onCreate(Bundle savedInstanceState) {
//        Log.e(ACTIVITY_NAME, "In function: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profileactivity);

        Intent dataFromPreviousPage = getIntent();
        String stuff = dataFromPreviousPage.getStringExtra("email");

        TextView saved = findViewById(R.id.enteryouremail);
        saved.setText(stuff);


        this.mImageButton = this.findViewById(R.id.buttonpicture);
        ImageButton mButton = findViewById(R.id.buttonpicture);
        mButton.setOnClickListener(v -> {

//                    Intent takePictureIntent = new Intent(ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            dispatchTakePictureIntent();
        });

        Log.e(ACTIVITY_NAME, "In function onCreate");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
        Log.e(ACTIVITY_NAME, "In function dispatchTakePictureIntent");

    }

    protected void onStart(){
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mImageButton.setImageBitmap(photo);

        }

        Log.e(ACTIVITY_NAME, "In function onActivityResult");
    }

}
