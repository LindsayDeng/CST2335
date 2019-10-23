package com.example.androidlabs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<Message> messageLog = new ArrayList();
    BaseAdapter messageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroomactivity);

        ListView theList = findViewById(R.id.theList);
        theList.setAdapter(messageAdapter= new MyListAdapter());

        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        String [] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGE, MyDatabaseOpenHelper.COL_SENT};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        int messageColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int sentColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_SENT);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        while(results.moveToNext())
        {
            boolean booleanSent;
            String previousMessage = results.getString(messageColIndex);
            //String previousSent = results.getString(sentColIndex);
            long id = results.getLong(idColIndex);
            if (results.getInt(sentColIndex) == 1 ){
                booleanSent = true;
            } else {
                booleanSent = false;
            }

            //add the new Contact to the array list:
            messageLog.add(new Message(previousMessage, booleanSent, id));
        }


        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(click -> {
            EditText edit = findViewById(R.id.textMessage);
            String message = edit.getText().toString();

            ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, message);
            //put string email in the EMAIL column:
            newRowValues.put(MyDatabaseOpenHelper.COL_SENT, 1);

            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
            printCursor(results);
            messageLog.add(new Message(message, true, newId));
            messageAdapter.notifyDataSetChanged();
            edit.getText().clear();

        });

        Button receiveButton = findViewById(R.id.receiveButton);
        receiveButton.setOnClickListener(click -> {
            EditText edit = findViewById(R.id.textMessage);
            String message = edit.getText().toString();

            ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, message);
            //put string email in the EMAIL column:
            newRowValues.put(MyDatabaseOpenHelper.COL_SENT, 0);

            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
            printCursor(results);
            messageLog.add(new Message(message, false, newId));
            messageAdapter.notifyDataSetChanged();
            edit.getText().clear();

        });

    }


    private void printCursor( Cursor c){
        Log.e("Database version number", "" + MyDatabaseOpenHelper.VERSION_NUM);
        Log.e("Cursor column number", "" + c.getColumnCount());
        Log.e("Cursor column name", "" + c.getColumnName(0) + ", " + c.getColumnName(1)
                + ", " + c.getColumnName(2));
        Log.e("Cursor result numer", "" + c.getCount());
        Log.e("Each row of results", ""
        );

    }

    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messageLog.size();
        }

        @Override
        public Message getItem(int position) {
            return messageLog.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View thisRow = convertView;
            Message rowMessage = getItem(position);
            if (rowMessage.getIsSend()){
                thisRow = getLayoutInflater().inflate(R.layout.send, null);
                TextView  itemText = thisRow.findViewById(R.id.sendTextMessage);
                itemText.setText(rowMessage.getMessage());
            }else {
                thisRow = getLayoutInflater().inflate(R.layout.receive, null);
                TextView  itemText = thisRow.findViewById(R.id.receiveTextMessage);
                itemText.setText(rowMessage.getMessage());
            }


            return thisRow;
        }
    }
}
