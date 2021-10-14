package com.example.eventsapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.eventsapp.R;
import com.example.eventsapp.adapters.PlaceAdapter;
import com.example.eventsapp.models.PlaceResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class SignUpBusinessActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG="SignUpBusinessActivity";
    private GoogleMap MygoogleMap;
    private EditText etSearchBusiness;
    private Button btnSubmit;
    private PlaceResult FinalplaceResult;
    private List<PlaceResult> PlaceList;
    private PlaceAdapter placeAdapter;
    private RecyclerView recyclerView;
    private RelativeLayout PlacesListLayout;
    private TextView tvMessage,tvAddress;
    private ImageView ivBusiness;
    private String URLNoKey="N/A";      //default value for these so can never crash next activity if null
    private String Address="No Address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_business);
        getSupportActionBar().hide();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MyGoogleMap);
        mapFragment.getMapAsync(this);
        InitializeViews();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpBusinessActivity.this,FinalBusinessSignUpActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("Address",Address);
                bundle.putString("ImageURL",URLNoKey);
                intent.putExtra("BusinessBundle",bundle);
                startActivity(intent);
            }
        });

        etSearchBusiness.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //not using
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placeAdapter.notifyDataSetChanged();
                HideWidgets();
                CheckLocations(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //not using
            }
        });


        PlaceAdapter.GetLocationInterface getLocationInterface=new PlaceAdapter.GetLocationInterface() {
            @Override
            public void GetLocation(PlaceResult placeResult) {

                etSearchBusiness.setText("");
                FinalplaceResult=placeResult;
                LatLng latLng=new LatLng(placeResult.getLat(),placeResult.getLng());
                MygoogleMap.addMarker(new MarkerOptions().position(latLng).title(placeResult.getName())).showInfoWindow();
                MygoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                CloseKeyboard();
                ShowWidgets();
                String PhotoURL="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + FinalplaceResult.getPhotoReference() + "&key=" + getResources().getString(R.string.google_maps_key);
                Uri uri =  Uri.parse(PhotoURL);

                if (!PhotoURL.contentEquals("NA") && PhotoURL!=null) {
                    Glide.with(SignUpBusinessActivity.this).load(uri).into(ivBusiness);
                    URLNoKey="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + FinalplaceResult.getPhotoReference()+ "&key=";
                }
                Address=FinalplaceResult.getAddress();
                tvAddress.setText(FinalplaceResult.getAddress());

            }
        };
        PlaceList=new ArrayList<>();
        placeAdapter=new PlaceAdapter(this,PlaceList,getLocationInterface);
        recyclerView.setAdapter(placeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HideAll();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        MygoogleMap=googleMap;
    }

    private void HideAll(){
        PlacesListLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setBackgroundColor(Color.parseColor("#000000"));

        btnSubmit.setVisibility(View.INVISIBLE);
        tvMessage.setVisibility(View.INVISIBLE);
    }

    private void ShowWidgets(){                      //after done typing
        PlacesListLayout.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        btnSubmit.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.VISIBLE);
    }

    private void HideWidgets(){              //while typing location
        btnSubmit.setVisibility(View.INVISIBLE);
        tvMessage.setVisibility(View.INVISIBLE);
        PlacesListLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setBackgroundColor(Color.parseColor("#f9f9f9"));
    }


    private void CloseKeyboard() {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private void CheckLocations(CharSequence s){

        String searchstring="https://maps.googleapis.com/maps/api/place/textsearch/json?&query="+s+
                "&key="+getResources().getString(R.string.google_maps_key);

        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        asyncHttpClient.get(searchstring, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject jsonObject= json.jsonObject;

                try {
                    PlaceList.clear();
                    JSONArray jsonArray= jsonObject.getJSONArray("results");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        if (j==10){
                            return;
                        }
                        PlaceList.add(new PlaceResult(jsonArray.getJSONObject(j)));

                    }
                    placeAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.e(TAG,"failed to query json data");
            }
        });

    }

private void InitializeViews(){
    etSearchBusiness=findViewById(R.id.etBusinessSearch);
    PlacesListLayout=findViewById(R.id.RLBusinesses);
    btnSubmit=findViewById(R.id.btnConfirmBusiness);
    tvMessage=findViewById(R.id.tvCorrectBusiness);
    ivBusiness=findViewById(R.id.ivBusinessImage);
    tvAddress=findViewById(R.id.tvBusinessAddress);
    recyclerView=findViewById(R.id.RVBusiness);
}

}