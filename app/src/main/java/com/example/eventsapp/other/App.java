package com.example.eventsapp.other;

import android.app.Application;
import android.provider.CalendarContract;

import com.example.eventsapp.R;
import com.example.eventsapp.models.DirectMessage;
import com.parse.Parse;
import com.parse.ParseObject;

public class App extends Application {

    public void onCreate() {
        super.onCreate();


        ParseObject.registerSubclass(DirectMessage.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getResources().getString(R.string.back4app_app_id))
                .clientKey(getResources().getString(R.string.back4app_client_key))
                .server("https://partystarters.b4a.io")     //new url for live events
                .build()
        );
    }


}
