package com.example.eventsapp.fragments;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventsapp.R;
import com.example.eventsapp.activities.LoginActivity;
import com.example.eventsapp.adapters.ShowEventsAdapter;
import com.example.eventsapp.adapters.UserProfileAdapter;
import com.example.eventsapp.models.Events;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class FragmentHome extends Fragment {



    EditText etProfileSearch;
    RecyclerView rvProfiles;
    List<ParseUser> UserList;
    List<Events> EventList;
    ShowEventsAdapter showEventsAdapter;
    UserProfileAdapter userProfileAdapter;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        etProfileSearch=view.findViewById(R.id.etSearchUserProfiles);
        rvProfiles=view.findViewById(R.id.rvSearchProfiles);
        UserList=new ArrayList<>();
        EventList=new ArrayList<>();
        userProfileAdapter=new UserProfileAdapter(UserList,getContext());
        showEventsAdapter=new ShowEventsAdapter(getContext(),EventList);
        rvProfiles.setLayoutManager(new LinearLayoutManager(getContext()));

        ParseQuery<Events> query= ParseQuery.getQuery(Events.class);    //gets public events
        query.findInBackground(new FindCallback<Events>() {
            @Override
            public void done(List<Events> AllEvents, ParseException e) {
                for (Events event: AllEvents){
                    if (event.EventIsPrivate()==true){
                        EventList.add(event);
                    }
                }
                rvProfiles.setAdapter(showEventsAdapter);
                showEventsAdapter.notifyDataSetChanged();
            }
        });


        //query posts and set to recyclerview


       // rvProfiles.setAdapter(userProfileAdapter);


        etProfileSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rvProfiles.setAdapter(userProfileAdapter);

                if (s.length()==0) {
                    UserList.clear();
                    userProfileAdapter.notifyDataSetChanged();
                    rvProfiles.setAdapter(showEventsAdapter);

                }

                if (s.length() > 0) {

                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    List<String> mylist = new ArrayList<>();
                    query.whereStartsWith("RealName", s.toString());
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> users, ParseException e) {
                            if (e == null) {
                                UserList.clear();       //this needs to be here to prevent duplicates appearing
                                for (ParseUser user:users){
                                    if (!user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                                        UserList.add(user);
                                    }
                                }
                               //UserList.addAll(users);

                                userProfileAdapter.notifyDataSetChanged();
                            }

                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


}