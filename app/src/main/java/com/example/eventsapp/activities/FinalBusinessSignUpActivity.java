package com.example.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventsapp.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class FinalBusinessSignUpActivity extends AppCompatActivity {

    private EditText etUsername,etPassword;
    private Button btnSubmit;
    private boolean HasUsername,HasPassword;

    //TODO: Get Real Business Name pushed to Parse. Email is only login criterion.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_business_sign_up);
        getSupportActionBar().hide();
        etUsername=findViewById(R.id.etBusinessEmail);
        etPassword=findViewById(R.id.etBusinessPassword);
        btnSubmit=findViewById(R.id.btnCreateBusinessProfile);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateBusinessProfile();
            }
        });





    }

    private void CreateBusinessProfile(){
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    Toast.makeText(FinalBusinessSignUpActivity.this, "Account Created", Toast.LENGTH_LONG).show();
                    ParseUser.getCurrentUser().put("IsBusinessProfile", true);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            startActivity(new Intent(FinalBusinessSignUpActivity.this,MainActivity.class));
                            finish();
                        }
                    });

                }
                else{
                    Toast.makeText(FinalBusinessSignUpActivity.this, "Error Creating Account", Toast.LENGTH_LONG).show();

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
                        btnSubmit.setVisibility(View.VISIBLE);
                    }

                }
                else{
                    HasUsername=false;
                    btnSubmit.setVisibility(View.INVISIBLE);
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
                        btnSubmit.setVisibility(View.VISIBLE);
                    }

                }
                else{
                    HasPassword=false;
                    btnSubmit.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}