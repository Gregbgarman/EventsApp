package com.example.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.eventsapp.R;

public class SignUpIntermediateActivity extends AppCompatActivity {

    private CardView cvStudent,cvRestarauntBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_intermediate);
        getSupportActionBar().hide();
        cvStudent=findViewById(R.id.CVFSUSTUDENT);
        cvRestarauntBar=findViewById(R.id.CVRESTORBAR);

        cvStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpIntermediateActivity.this,SignUpStudentActivity.class));
            }
        });

        cvRestarauntBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpIntermediateActivity.this,SignUpBusinessActivity.class));

            }
        });


    }
}