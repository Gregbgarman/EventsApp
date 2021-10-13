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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eventsapp.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.IOException;
import java.security.Principal;


public class FragmentProfile extends Fragment {

    ParseUser currentUser = ParseUser.getCurrentUser();
    private ImageView ivProfilePic;
    private TextView tvUsername;
    private Button btnSettings;

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvUsername = view.findViewById(R.id.tvProfileUsername);
        btnSettings = view.findViewById(R.id.btnSettings);

        // load user image from parse
        ParseFile profilePicImage = currentUser.getParseFile("ProfilePicture");
        if (profilePicImage != null) {
            Glide.with(getContext()).load(profilePicImage.getUrl()).circleCrop().into(ivProfilePic);
        } else {
            Glide.with(getContext()).load(R.drawable.img_1).circleCrop().into(ivProfilePic);
        }

        //load username from parse
        tvUsername.setText(currentUser.getUsername());

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to a settings fragment
                Fragment fragment = new FragmentSettings();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .replace(R.id.FLContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}