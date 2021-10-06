package com.example.eventsapp.other;

import android.app.Application;

import com.example.eventsapp.R;
import com.parse.Parse;

public class App extends Application {

    public void onCreate() {
        super.onCreate();


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getResources().getString(R.string.back4app_app_id))
                .clientKey(getResources().getString(R.string.back4app_client_key))
                .server(getResources().getString(R.string.back4app_server_url))
                .build()
        );
    }

}
