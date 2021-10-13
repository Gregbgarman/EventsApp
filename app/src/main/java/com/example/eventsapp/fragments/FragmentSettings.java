package com.example.eventsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.eventsapp.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class FragmentSettings extends Fragment {

    ParseUser currentUser = ParseUser.getCurrentUser();
    private ImageView ivSettingsProfilePic;
    private EditText etSettingsUsername;
    private EditText etSettingsPassword;
    private Button btnSettingsCancel;
    private Button btnSettingsSaveChanges;

    public FragmentSettings(){
        // Empty constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivSettingsProfilePic = view.findViewById(R.id.ivSettingsProfilePic);
        etSettingsUsername = view.findViewById(R.id.etSettingsUsername);
        etSettingsPassword = view.findViewById(R.id.etSettingsPassword);
        btnSettingsCancel = view.findViewById(R.id.btnSettingsCancel);
        btnSettingsSaveChanges = view.findViewById(R.id.btnSettingsSaveChanges);

        // Load user image from parse
        ParseFile profilePicImage = currentUser.getParseFile("ProfilePicture");
        if (profilePicImage != null) {
            Glide.with(getContext()).load(profilePicImage.getUrl()).circleCrop().into(ivSettingsProfilePic);
        } else {
            Glide.with(getContext()).load(R.drawable.img_1).circleCrop().into(ivSettingsProfilePic);
        }

        // Show 'hint' as current Username
        etSettingsUsername.setHint(currentUser.getUsername());

        // Profile picture on click
        ivSettingsProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Allow user to change profile pic
            }
        });

        // Cancel button on click
        btnSettingsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go back to profile fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        // Save changes button on click
        btnSettingsSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save the changes
                if(etSettingsUsername.getText().toString().length() <= 0)
                    System.out.println("Error: Username could not be changed, must be longer than 0 characters");
                else
                    currentUser.setUsername(etSettingsUsername.getText().toString());

                if(etSettingsPassword.getText().toString().length() <= 0)
                    System.out.println("Error: Password could not be changed, must be longer than 0 characters");
                else
                    currentUser.setPassword(etSettingsPassword.getText().toString());

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

}
