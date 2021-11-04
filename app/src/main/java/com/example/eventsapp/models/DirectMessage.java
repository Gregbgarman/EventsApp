package com.example.eventsapp.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("DirectMessage")
public class DirectMessage extends ParseObject {

    public static final String KEY_CREATED_AT="createdAt";
    public static final String KEY_MESSAGECONTENT="MessageContent";
    public static final String KEY_OTHERPERSONID="OtherPersonID";
    public static final String KEY_SENDERID="SenderID";

    public Date getTimeCreatedAt(){
        return getDate(KEY_CREATED_AT);
    }

    public void setSender(String sender){
        put(KEY_SENDERID,sender);
    }

    public String getSender(){
        return getString(KEY_SENDERID);
    }

    public void SetMessage(String msg){

        put(KEY_MESSAGECONTENT,msg);
    }

    public void SetOtherPerson(String id){
        put(KEY_OTHERPERSONID,id);
    }

    public String getMessage(){
        return getString(KEY_MESSAGECONTENT);
    }
    public String getOtherPerson(){
        return getString(KEY_OTHERPERSONID);
    }

}
