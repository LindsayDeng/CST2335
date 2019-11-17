package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class TestToolbar extends AppCompatActivity {
    String dialogMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lab7toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.item1:
                Toast.makeText(this, "This is the initial message", Toast.LENGTH_LONG).show();

                break;
            case R.id.item2:
                customDialog();
                break;
            case R.id.item3:
                Snackbar sb = Snackbar.make(findViewById(R.id.toolbar), "Go Back?", Snackbar.LENGTH_LONG);
                sb.setAction("Finish", v -> finish());
                sb.show();

                break;
            case R.id.item4:
                if (dialogMessage == null) {
                    Toast.makeText(this, "You clicked on the overflow menu", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, dialogMessage, Toast.LENGTH_LONG).show();
                }
                break;

        }
        return true;
    }

    public void customDialog()
    {
        View middle = getLayoutInflater().inflate(R.layout.toolbar_custom_dialog, null);

        EditText et = (EditText)middle.findViewById(R.id.dialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Accept
                        dialogMessage = et.getText().toString();
                    }
                })
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }
}
