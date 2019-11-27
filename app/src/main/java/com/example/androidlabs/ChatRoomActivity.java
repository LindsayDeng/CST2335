package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

    public static final String ITEM_ID = "ID";
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final int EMPTY_ACTIVITY = 345;
    ArrayList<Message> messageLog = new ArrayList();
    MyListAdapter messageAdapter;
    SQLiteDatabase db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroomactivity);

        ListView theList = findViewById(R.id.theList);
        theList.setAdapter(messageAdapter= new MyListAdapter());

        boolean isTablet = findViewById(R.id.frame) != null;

        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();

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


        theList.setOnItemClickListener( (list, item, position, id) -> {

            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, messageLog.get(position).getMessage() );
            dataToPass.putLong(ITEM_ID, messageLog.get(position).getId());

            if(isTablet)
            {
                DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });


        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(click -> {
            EditText edit = findViewById(R.id.textMessage);
            String message = edit.getText().toString();

            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, message);
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

    public void deleteMessageId(int id){
        db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID + "=?", new String[]{Long.toString(id)});
        messageAdapter.deleteItem(id);
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(ITEM_ID, 0);
                deleteMessageId((int)id);
            }
        }
    }
    private void printCursor( Cursor c){
        Log.i("Database version number", "" + MyDatabaseOpenHelper.VERSION_NUM);
        Log.i("Cursor column number", "" + c.getColumnCount());
        Log.i("Cursor column name", "" + c.getColumnName(0) + ", " + c.getColumnName(1)
                + ", " + c.getColumnName(2));
        Log.i("Cursor result numer", "" + c.getCount());
        Log.i("Each row of results", "" + DatabaseUtils.dumpCursorToString(c)
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

        public void deleteItem(long id){
            for (Message i:messageLog){
                if(i.getId() ==id ){
                    messageLog.remove(i);
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }
}
