package com.example.androidlabs;

public class Message {
    protected String message;
    protected Boolean isSend;

    public Message (){

    }

    public Message(String message, boolean isSend){
        this.message = message;
        this.isSend = isSend;
    }

    public String getMessage(){
        return message;
    }

    public boolean getIsSend(){
        return isSend;
    }


}
