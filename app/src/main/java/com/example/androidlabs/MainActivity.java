package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    protected SharedPreferences prefs;
    protected EditText editText;
    protected SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab3_grid);

        prefs = getSharedPreferences("ReserveName", MODE_PRIVATE);
        editText = findViewById(R.id.editText1);
        edit = prefs.edit();

        String previous = prefs.getString("ReserveName", "");
        editText.setText(previous);


        Button page1Button = findViewById(R.id.button1);
        if(page1Button != null)
            page1Button.setOnClickListener(v -> {
                Intent goToPage2 = new Intent(MainActivity.this, ProfileActivity.class);
                goToPage2.putExtra("email", editText.getText().toString());
                startActivity(goToPage2);

            });

        EditText editText = findViewById(R.id.editText1);

    }
    @Override
    protected void onPause(){
        super.onPause();
        edit.putString("ReserveName", editText.getText().toString());
        edit.commit();
    }


}
