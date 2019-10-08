package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {
//    ListView myList;
//    EditText message;
//    Button  send, receive;
    ArrayList<Message> messageLog = new ArrayList();
    BaseAdapter messageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroomactivity);

        ListView theList = findViewById(R.id.theList);
        theList.setAdapter(messageAdapter= new MyListAdapter());


//        myList = findViewById(R.id.theList);
//        message = findViewById(R.id.textMessage);
//        send = findViewById(R.id.sendButton);
//        receive = findViewById(R.id.receiveButton);
//        messageList = new ArrayList<>();

//        ListAdapter aListAdapter = new ListAdapter(messageList, getApplicationContext());
//        myList.setAdapter( aListAdapter);

//        myList.setAdapter(mAdapter = new ListAdapter());

        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(click -> {
            EditText edit = findViewById(R.id.textMessage);
            String message = edit.getText().toString();
            messageLog.add(new Message(message, true));
            messageAdapter.notifyDataSetChanged();
            edit.getText().clear();

//            String stringMessage = message.getText().toString();
//            Message msg = new Message(stringMessage, true);
//            messageList.add(msg);
//            //message.setText("");
//            mAdapter.notifyDataSetChanged();
        });

        Button receiveButton = findViewById(R.id.receiveButton);
        receiveButton.setOnClickListener(click -> {
            EditText edit = findViewById(R.id.textMessage);
            String message = edit.getText().toString();
            messageLog.add(new Message(message, false));
            messageAdapter.notifyDataSetChanged();
            edit.getText().clear();

//            String stringMessage = message.getText().toString();
//            Message msg = new Message(stringMessage, true);
//            messageList.add(msg);
//            //message.setText("");
//            mAdapter.notifyDataSetChanged();
        });

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
