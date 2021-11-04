package com.example.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eventsapp.R;
import com.parse.ParseUser;

public class OtherProfileActivity extends AppCompatActivity {

    private TextView tvPersonRealName,tvPersonUserName;
    private ImageView ivProfilePicture;
    private Button btnSendMessage;
    private Bundle bundle;
    ParseUser OtherPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        getSupportActionBar().hide();
        tvPersonRealName=findViewById(R.id.tvOtherPersonRealName);
        tvPersonUserName=findViewById(R.id.tvOtherPersonUsername);
        ivProfilePicture=findViewById(R.id.ivOtherPersonProfilePicturee);
        btnSendMessage=findViewById(R.id.btnSendOtherPersonMessage);

        bundle=getIntent().getBundleExtra("BundleFromAdapter");
        OtherPerson=bundle.getParcelable("UserProfileFromAdapter");

        //need to check if business and set up accordingly

        tvPersonUserName.setText(OtherPerson.getUsername());
        tvPersonRealName.setText(OtherPerson.getString("RealName"));
        Glide.with(this).load(OtherPerson.getParseFile("ProfilePicture").getUrl()).circleCrop().into(ivProfilePicture);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putParcelable("UserProfileBeforeMsg",OtherPerson);
                Intent intent=new Intent(OtherProfileActivity.this, DirectMessageActivity.class);
                intent.putExtra("Bundle12345",bundle);
                startActivity(intent);

            }
        });
    }
}