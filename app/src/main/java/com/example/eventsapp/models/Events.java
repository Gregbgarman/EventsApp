package com.example.eventsapp.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Events")

public class Events extends ParseObject {
    public static final String KEY_EVENTNAME="eventName";
    public static final String KEY_EVENTDESC="eventDescription";
    public static final String KEY_CREATOR="createdByUser";
    public static final String KEY_ISPRIVATE="eventPrivate";
    public static final String KEY_EVENTTIME="eventTime";
    public static final String KEY_EVENTDATE="eventDate";

    public String GetEventName(){
        return getString(KEY_EVENTNAME);
    }

    public String GetEventDescription(){
        return getString(KEY_EVENTDESC);
    }

    public ParseUser GetEventCreator(){
        return getParseUser(KEY_CREATOR);
    }

    public boolean EventIsPrivate(){
        return getBoolean(KEY_ISPRIVATE);
    }

    public String GetEventTime(){
        return getString(KEY_EVENTTIME);
    }
    public String GetEventDate(){
        return getString(KEY_EVENTDATE);
    }


}
