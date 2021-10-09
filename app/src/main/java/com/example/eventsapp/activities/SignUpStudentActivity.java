package com.example.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eventsapp.R;

import java.util.Collection;
import java.util.Collections;

public class SignUpStudentActivity extends AppCompatActivity {

    private EditText etEmail;
    private TextView tvErrorMsg;
    private Button btnEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_student);
        getSupportActionBar().hide();
        etEmail=findViewById(R.id.etStudentEmail);
        tvErrorMsg=findViewById(R.id.tvErrorEmail);
        btnEmail=findViewById(R.id.btnSubmitEmail);

        tvErrorMsg.setVisibility(View.INVISIBLE);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etEmail.getText().toString().trim();
                if (text.contains("fsu.edu")){
                    startActivity(new Intent(SignUpStudentActivity.this,FinalStudentSignUpActivity.class));
                }
                else{
                    tvErrorMsg.setVisibility(View.VISIBLE);
                }

            }
        });

    }
}