package com.example.eventsapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import com.example.eventsapp.R;

import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;


public class FragmentEvents extends Fragment{


    private TextView etGetEventName;
    private Button btnGetTime;
    private TextView etGetDescription;
    boolean privacyFlag = true;
    private String currentDate;
    private TextView showDateTextView;
    private Button btnGetDate;

    private DatePickerDialog datePickerDialog;

    int hour, minute;


    public FragmentEvents() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etGetEventName = view.findViewById(R.id.etGetEventName);
        etGetDescription = view.findViewById(R.id.etGetDescription);
        btnGetDate = view.findViewById(R.id.btnGetDate);
        btnGetTime = view.findViewById(R.id.btnGetTime);
        CheckBox cbPrivacy = view.findViewById(R.id.cbPrivacy);
        Button btnCreateEvent = view.findViewById(R.id.btnCreateEvent);
        cbPrivacy.setChecked(true);
        btnGetDate.setText(getTodaysDate());
        initDatePicker();

        // Listener for the checkbox
        cbPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                privacyFlag = checked;
            }
        });

        // Listener for Create Button
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createObject();
            }
        });

        //Listener for Get Date button
        btnGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });

        btnGetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(v);
            }
        });



    }

    public void openTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                btnGetTime.setText(String.format(Locale.getDefault(),"%02d:%02d", hour, minute));
            }
        };

       int style = R.style.ThemeOverlay_MaterialComponents_Dialog_Alert;


        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), style, onTimeSetListener, hour, minute, false);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();


    }


    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }


    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                btnGetDate.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = R.style.ThemeOverlay_MaterialComponents_Dialog_Alert;

        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view){
        datePickerDialog.show();
    }


    // Helper functions to grab values
    public String getEventName() {
        return etGetEventName.getText().toString().trim();
    }

    public String getEventDescription() {
        return etGetDescription.getText().toString().trim();
    }

    public String getEventDate() {
        return btnGetDate.getText().toString().trim();
    }

    public String getEventTime() {
        return btnGetTime.getText().toString().trim();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    // This function is used to create the new parse object and then add in the information
    public void createObject() {
        ParseObject newEvent = new ParseObject("Events");
        ParseUser currentUser = ParseUser.getCurrentUser();


        newEvent.put("eventName", getEventName());
        newEvent.put("createdByUser", currentUser);
        newEvent.put("eventDescription", getEventDescription());
        newEvent.put("eventDate", getEventDate());
        newEvent.put("eventTime", getEventTime());

        // Check for private event
        if(privacyFlag){
            newEvent.put("eventPrivate", true);
        }
        else{
            newEvent.put("eventPrivate", false);
        }

        newEvent.saveInBackground(e -> {
            // If the form is good, go to the confirmation fragment
            if(e==null){
                Fragment fragment = new FragmentConfirmation();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .replace(R.id.FLContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            // If not good, pop snackbar message saying to fill out the form
            // **NOTE** the validation needs to be expanded
            // With a textwatcher??? - corin
            else{
                Snackbar btnSnack = Snackbar.make(getActivity().findViewById(android.R.id.content), "Please fill out the form!", Snackbar.LENGTH_LONG);
                btnSnack.show();

            }
        });


    }





}