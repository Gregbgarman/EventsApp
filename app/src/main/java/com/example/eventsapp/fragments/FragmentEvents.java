package com.example.eventsapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventsapp.R;

import org.w3c.dom.Text;

import java.util.Calendar;

import com.example.eventsapp.activities.DatePickerActivity;
import com.example.eventsapp.activities.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;



public class FragmentEvents extends Fragment {

    ParseUser currentUser = ParseUser.getCurrentUser();
    private TextView etGetEventName;
    public TextView etGetDate;
    private TextView etGetTime;
    private TextView etGetDescription;
    private Button btnCreateEvent;
    private Button btnGetDate;


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
        btnCreateEvent = view.findViewById(R.id.btnCreateEvent);
        btnGetDate = view.findViewById(R.id.btnGetDate);


        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createObject();
                Fragment fragment = new FragmentConfirmation();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .replace(R.id.FLContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

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

        newEvent.saveInBackground(e -> {
            if(e==null){
                //saved
            }
            else {

                    Snackbar btnSnack = Snackbar.make(getActivity().findViewById(android.R.id.content), "Please fill out the form!", Snackbar.LENGTH_LONG);
                    btnSnack.show();

            }
        });


    }


}