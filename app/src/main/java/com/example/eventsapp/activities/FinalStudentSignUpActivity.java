package com.example.eventsapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eventsapp.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.IOException;
import java.io.InputStream;

public class FinalStudentSignUpActivity extends AppCompatActivity {

    private EditText etUsername,etPassword;
    private ImageView ivProfilePic;
    private Button btnCreateProfile;
    private ParseFile NewProfilePicture;
    private boolean HasUsername,HasPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_student_sign_up);
        getSupportActionBar().hide();
        etUsername=findViewById(R.id.etUserNameEntry);
        etPassword=findViewById(R.id.etPasswordEntry);
        ivProfilePic=findViewById(R.id.ivInitialProfilePicture);
        btnCreateProfile=findViewById(R.id.btnCreateProfile);
        btnCreateProfile.setVisibility(View.INVISIBLE);

        Glide.with(this).load(R.drawable.img_1).circleCrop().into(ivProfilePic);
        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessPhonePhotoGallery();
            }
        });

        btnCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateProfile();
            }
        });

        EnableTextListeners();

    }

    private void CreateProfile() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);




        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(FinalStudentSignUpActivity.this, "Account Created", Toast.LENGTH_LONG).show();

                    if (NewProfilePicture != null) {
                        ParseUser.getCurrentUser().put("ProfilePicture", NewProfilePicture);
                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                startActivity(new Intent(FinalStudentSignUpActivity.this, MainActivity.class));
                                finish();
                            }
                        });

                    }

                    startActivity(new Intent(FinalStudentSignUpActivity.this, MainActivity.class));
                    finish();
                }

                else if (e != null) {
                    Toast.makeText(FinalStudentSignUpActivity.this, "Could not create account", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void EnableTextListeners(){
        HasUsername=false;
        HasPassword=false;

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etUsername.getText().length()>0){
                    HasUsername=true;
                    if (HasPassword==true && HasUsername==true){
                        btnCreateProfile.setVisibility(View.VISIBLE);
                    }

                }
                else{
                    HasUsername=false;
                    btnCreateProfile.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPassword.getText().length()>0){
                    HasPassword=true;

                    if (HasPassword==true && HasUsername==true){
                        btnCreateProfile.setVisibility(View.VISIBLE);
                    }

                }
                else{
                    HasPassword=false;
                    btnCreateProfile.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }       ////

    private void AccessPhonePhotoGallery(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,321);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && requestCode==321 && data!=null && data.getData()!=null){

            Uri imageuri=data.getData();
            Glide.with(this).load(imageuri).circleCrop().into(ivProfilePic);
            InputStream inputstream=null;
            String filename=getFileName(imageuri);

            try {
                inputstream = this.getContentResolver().openInputStream(imageuri);
                byte buffer[] = new byte[inputstream.available()];
                inputstream.read(buffer);
                NewProfilePicture = new ParseFile(filename, buffer);
                inputstream.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
       //     Toast.makeText(FinalStudentSignUpActivity.this, "eeeeeeeeeeeeeeeeeee", Toast.LENGTH_LONG).show();

        //    ParseUser.getCurrentUser().put("ProfilePicture",NewProfilePicture);
         //   ParseUser.getCurrentUser().saveInBackground();
        }
    }

    public String getFileName(Uri uri) {    //converting uri to string
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
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