package com.example.androidlabs;

public class Message {
    protected String message;
    protected Boolean isSend;
    protected long id;

    public Message (){

    }

    public Message(String message, boolean isSend, long id){
        this.message = message;
        this.isSend = isSend;
        this.id = id;
    }

    public String getMessage(){
        return message;
    }

    public boolean getIsSend(){
        return isSend;
    }

    public long getId(){return id; }


}
