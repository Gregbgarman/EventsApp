package com.example.eventsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventsapp.R;
import com.example.eventsapp.models.Events;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;


public class EventsActivity extends AppCompatActivity {

        private ImageView backArrow;

    private TextView tvEPEventName,tvEPDate, tvEPTime,tvEPDesc, tvEPUserName;
    private ImageView ivEPUserPic;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        backArrow = findViewById(R.id.backBtn);

        tvEPEventName=findViewById(R.id.tvEPEventName);
        tvEPDate=findViewById(R.id.tvEPDate);
        tvEPTime=findViewById(R.id.tvEPTime);
        tvEPDesc=findViewById(R.id.tvEPDesc);
        //tvEPUserName=findViewById(R.id.tvEPUserName);


        Events event = Parcels.unwrap(getIntent().getParcelableExtra("eventStuff"));



        tvEPDate.setText(event.GetEventDate());
        tvEPEventName.setText(event.GetEventName());
        tvEPTime.setText(event.GetEventTime());
        tvEPDesc.setText(event.GetEventDescription());
        //tvEPUserName.setText(event.GetEventCreator().toString());

        //ParseUser user = event.GetEventCreator();
        //<ParseUser> query = ParseUser.getQuery();
        //query.whereContains("objectId", user.getObjectId());

        //tvEPUserName.setText(user.toString());


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMainActivity();
            }

        });

    }
    private void goMainActivity() {
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}