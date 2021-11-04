package com.example.eventsapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventsapp.R;
import com.example.eventsapp.activities.LoginActivity;
import com.example.eventsapp.adapters.UserProfileAdapter;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class FragmentHome extends Fragment {

    private Button btnLogout;

    EditText etProfileSearch;
    RecyclerView rvProfiles;
    List<ParseUser> UserList;
    UserProfileAdapter userProfileAdapter;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout=view.findViewById(R.id.btnLogout);



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });

        etProfileSearch=view.findViewById(R.id.etSearchUserProfiles);
        rvProfiles=view.findViewById(R.id.rvSearchProfiles);
        UserList=new ArrayList<>();
        userProfileAdapter=new UserProfileAdapter(UserList,getContext());
        rvProfiles.setAdapter(userProfileAdapter);
        rvProfiles.setLayoutManager(new LinearLayoutManager(getContext()));

        etProfileSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()==0) {
                    UserList.clear();
                    userProfileAdapter.notifyDataSetChanged();
                }

                if (s.length() > 0) {

                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    List<String> mylist = new ArrayList<>();
                    query.whereStartsWith("RealName", s.toString());
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> users, ParseException e) {
                            if (e == null) {
                                UserList.clear();       //this needs to be here to prevent duplicates appearing
                                for (ParseUser user:users){
                                    if (!user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                                        UserList.add(user);
                                    }
                                }
                               //UserList.addAll(users);

                                userProfileAdapter.notifyDataSetChanged();
                            }

                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void LogOut(){      //probably more proper to call interface and logout from main to ensure main ends
                                //this is fine for testing for now
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(getContext(), "Logging out...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                else {
                    Toast.makeText(getContext(), "Error logging out", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}