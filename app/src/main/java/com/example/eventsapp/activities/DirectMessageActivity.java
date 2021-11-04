package com.example.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eventsapp.R;
import com.example.eventsapp.adapters.DirectMessageAdapter;
import com.example.eventsapp.models.DirectMessage;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectMessageActivity extends AppCompatActivity {

    private ImageView ivOtherPersonPic;
    private TextView tvOtherPersonname;
    private EditText etTheMessage;
    private Button btnSendMsg;
    private RecyclerView rvMessages;
    private List<DirectMessage> directMessageList;
    private ParseFile OtherPersonProfilePic;
    private ParseFile myprofilepic;
    private Bundle bundle;
    private DirectMessageAdapter directMessageAdapter;
    private ParseUser OtherPerson;
    private boolean FirstLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Initialize();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DirectMessageActivity.this);
        linearLayoutManager.setReverseLayout(true);
        directMessageList=new ArrayList<>();
        directMessageAdapter=new DirectMessageAdapter(this,directMessageList,myprofilepic,OtherPersonProfilePic);
        rvMessages.setAdapter(directMessageAdapter);
        rvMessages.setLayoutManager(linearLayoutManager);
        FirstLoad=true;
        QueryMessages();
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });

        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        ParseQuery<DirectMessage> parseQuery = ParseQuery.getQuery(DirectMessage.class);
        SubscriptionHandling<DirectMessage> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new SubscriptionHandling.HandleEventCallback<DirectMessage>() {
            @Override
            public void onEvent(ParseQuery<DirectMessage> query, DirectMessage object) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (object.getOtherPerson().equals(ParseUser.getCurrentUser().getObjectId()) && object.getSender().equals(OtherPerson.getObjectId()) ){
                            Collections.reverse(directMessageList);
                            directMessageList.add(object);
                            Collections.reverse(directMessageList);
                        }

                        directMessageAdapter.notifyDataSetChanged();
                        rvMessages.scrollToPosition(0);

                    }
                });

            }
        });


    }

    private void SendMessage(){
        String msg=etTheMessage.getText().toString().trim();
        if (msg.length()==0){
            return;
        }
        DirectMessage directMessage=new DirectMessage();
        directMessage.setSender(ParseUser.getCurrentUser().getObjectId());
        directMessage.SetOtherPerson(OtherPerson.getObjectId());
        directMessage.SetMessage(msg);
        etTheMessage.setText("");

        directMessage.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    QueryMessages();
                }

            }
        });

    }


    private void QueryMessages(){
        directMessageList.clear();
        ParseQuery<DirectMessage> query=ParseQuery.getQuery(DirectMessage.class);
        query.include(DirectMessage.KEY_SENDERID);
        query.whereEqualTo(DirectMessage.KEY_SENDERID, ParseUser.getCurrentUser().getObjectId());
        query.whereEqualTo(DirectMessage.KEY_OTHERPERSONID, OtherPerson.getObjectId());

        query.findInBackground(new FindCallback<DirectMessage>() {
            @Override
            public void done(List<DirectMessage> objects, ParseException e) {
                directMessageList.addAll(objects);
                ParseQuery<DirectMessage> query1=ParseQuery.getQuery(DirectMessage.class);
                query1.include(DirectMessage.KEY_SENDERID);
                query1.whereEqualTo(DirectMessage.KEY_SENDERID, OtherPerson.getObjectId());
                query1.whereEqualTo(DirectMessage.KEY_OTHERPERSONID, ParseUser.getCurrentUser().getObjectId());
                query1.findInBackground(new FindCallback<DirectMessage>() {
                    @Override
                    public void done(List<DirectMessage> objects, ParseException e) {
                        directMessageList.addAll(objects);
                        directMessageList.sort((o1,o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));
                        Collections.reverse(directMessageList);
                        directMessageAdapter.notifyDataSetChanged(); // update adapter

                        // Scroll to the bottom of the list on initial load
                        if (FirstLoad==true) {
                           rvMessages.scrollToPosition(0);
                            FirstLoad = false;
                        }

                    }
                });


            }
        });

    }


    private void Initialize(){
        setContentView(R.layout.activity_direct_message);
        getSupportActionBar().hide();
        ivOtherPersonPic=findViewById(R.id.ivivOtherPersonpic);
        tvOtherPersonname=findViewById(R.id.tvtvOtherPersonname);
        etTheMessage=findViewById(R.id.etTypeMessage);
        btnSendMsg=findViewById(R.id.btnSendMsg);
        rvMessages=findViewById(R.id.rvDirectMessages);

        myprofilepic=ParseUser.getCurrentUser().getParseFile("ProfilePicture");
        bundle=getIntent().getBundleExtra("Bundle12345");
        OtherPerson=bundle.getParcelable("UserProfileBeforeMsg");
        OtherPersonProfilePic=OtherPerson.getParseFile("ProfilePicture");
        Glide.with(this).load(OtherPersonProfilePic.getUrl()).circleCrop().into(ivOtherPersonPic);
        tvOtherPersonname.setText(OtherPerson.getUsername());

    }
}