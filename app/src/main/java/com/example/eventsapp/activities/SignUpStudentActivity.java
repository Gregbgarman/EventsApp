package com.example.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventsapp.R;

import java.util.Collection;
import java.util.Collections;

public class SignUpStudentActivity extends AppCompatActivity {

    private EditText etEmail,etFirstName,etLastName;
    private TextView tvErrorMsg;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_student);
        getSupportActionBar().hide();
        etEmail=findViewById(R.id.etStudentEmail);
        tvErrorMsg=findViewById(R.id.tvErrorEmail);
        btnSubmit=findViewById(R.id.btnSubmitEmail);
        etFirstName=findViewById(R.id.etStudentFirstName);
        etLastName=findViewById(R.id.etStudentLastName);


        tvErrorMsg.setVisibility(View.INVISIBLE);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etEmail.getText().toString().trim();
                String firstname=etFirstName.getText().toString().trim();
                String lastname=etLastName.getText().toString().trim();

                if (text.contains("fsu.edu")){     //could be more thorough of a check

                    if (firstname.length()>0 && lastname.length()>0) {
                        Bundle bundle=new Bundle();
                        bundle.putString("firstname",firstname);
                        bundle.putString("lastname",lastname);
                        Intent intent=new Intent(SignUpStudentActivity.this,FinalStudentSignUpActivity.class);
                        intent.putExtra("NameBundle",bundle);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(SignUpStudentActivity.this, "Enter First and Last Name", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    tvErrorMsg.setVisibility(View.VISIBLE);
                }

            }
        });

    }
}