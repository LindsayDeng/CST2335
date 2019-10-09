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

    ArrayList<Message> messageLog = new ArrayList();
    BaseAdapter messageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroomactivity);

        ListView theList = findViewById(R.id.theList);
        theList.setAdapter(messageAdapter= new MyListAdapter());

        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(click -> {
            EditText edit = findViewById(R.id.textMessage);
            String message = edit.getText().toString();
            messageLog.add(new Message(message, true));
            messageAdapter.notifyDataSetChanged();
            edit.getText().clear();

        });

        Button receiveButton = findViewById(R.id.receiveButton);
        receiveButton.setOnClickListener(click -> {
            EditText edit = findViewById(R.id.textMessage);
            String message = edit.getText().toString();
            messageLog.add(new Message(message, false));
            messageAdapter.notifyDataSetChanged();
            edit.getText().clear();

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
