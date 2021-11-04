package com.example.eventsapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.Switch;
import android.widget.TextView;


import com.example.eventsapp.R;

import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseObject;
import com.parse.ParseUser;



public class FragmentEvents extends Fragment {


    private TextView etGetEventName;
    public TextView etGetDate;
    private TextView etGetTime;
    private TextView etGetDescription;
    boolean privacyFlag = true;



    public FragmentEvents() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etGetEventName = view.findViewById(R.id.etGetEventName);
        etGetDate = view.findViewById(R.id.etGetDate);
        etGetDescription = view.findViewById(R.id.etGetDescription);
        etGetTime = view.findViewById(R.id.etGetTime);
        Button btnCreateEvent = view.findViewById(R.id.btnCreateEvent);
        Button btnGetDate = view.findViewById(R.id.btnGetDate);
        Switch swPrivacy = (Switch) view.findViewById(R.id.swPrivacy);
        swPrivacy.setChecked(false);

        swPrivacy.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked){
                privacyFlag = true;
            }
            else{
                privacyFlag = false;
            }
        });

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createObject();


            }
        });

        btnGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


    public String getEventName() {
        return etGetEventName.getText().toString().trim();
    }

    public String getEventDescription() {
        return etGetDescription.getText().toString().trim();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    public void createObject() {
        ParseObject newEvent = new ParseObject("Events");
        ParseUser currentUser = ParseUser.getCurrentUser();


        newEvent.put("eventName", getEventName());
        newEvent.put("createdByUser", currentUser);
        newEvent.put("eventDescription", getEventDescription());

        if(privacyFlag){
            newEvent.put("eventPrivate", true);
        }
        else{
            newEvent.put("eventPrivate", false);
        }
        newEvent.saveInBackground(e -> {
            if(e==null){
                Fragment fragment = new FragmentConfirmation();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .replace(R.id.FLContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            else {

                    Snackbar btnSnack = Snackbar.make(getActivity().findViewById(android.R.id.content), "Please fill out the form!", Snackbar.LENGTH_LONG);
                    btnSnack.show();

            }
        });


    }



}