package com.example.eventsapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.ContentResolver;
import android.content.ContextWrapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.eventsapp.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;

public class FragmentSettings extends Fragment {

    ParseUser currentUser = ParseUser.getCurrentUser();
    private ParseFile NewProfilePicture;
    private ImageView ivSettingsProfilePic;
    private EditText etSettingsUsername;
    private EditText etSettingsPassword;
    private EditText etSettingsInstagram;
    private Button btnSettingsCancel;
    private Button btnSettingsSaveChanges;
    //public static final String KEY_Instagram = "instagram";
    public FragmentSettings(){
        // Empty constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivSettingsProfilePic = view.findViewById(R.id.ivSettingsProfilePic);
        etSettingsUsername = view.findViewById(R.id.etSettingsUsername);
        etSettingsPassword = view.findViewById(R.id.etSettingsPassword);
        etSettingsInstagram = view.findViewById(R.id.etSettingsInstagram);
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
                AccessPhonePhotoGallery();
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
                    System.out.println("No username provided");
                else
                    currentUser.setUsername(etSettingsUsername.getText().toString());

                if(etSettingsPassword.getText().toString().length() <= 0)
                    System.out.println("No password provided");
                else
                    currentUser.setPassword(etSettingsPassword.getText().toString());

                if(etSettingsInstagram.getText().toString().length() <= 0)
                    System.out.println("Error: Instagram account could not be set, your Instagram Username must be longer than 0 characters");
                else
                    currentUser.put("instagram", etSettingsInstagram.getText().toString());

                if(NewProfilePicture != null) {
                    System.out.println("Picture has been changed");
                    currentUser.put("ProfilePicture", NewProfilePicture);
                }

                currentUser.saveInBackground();



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

    private void AccessPhonePhotoGallery(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,4243);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && requestCode==4243 && data!=null && data.getData()!=null){

            Uri imageuri=data.getData();
            Glide.with(this).load(imageuri).circleCrop().into(ivSettingsProfilePic);
            InputStream inputstream=null;
            String filename=getFileName(imageuri);

            try {
                inputstream = getContext().getContentResolver().openInputStream(imageuri);
                byte buffer[] = new byte[inputstream.available()];
                inputstream.read(buffer);
                NewProfilePicture = new ParseFile(filename, buffer);
                inputstream.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getFileName(Uri uri) {    //converting uri to string
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
