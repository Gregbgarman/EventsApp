package com.example.eventsapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventsapp.R;
import com.example.eventsapp.activities.SignUpBusinessActivity;
import com.parse.ParseUser;

import java.util.List;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.ViewHolder> {

    private List<ParseUser> UserList;
    private Context context;

    public UserProfileAdapter(List<ParseUser> UserList, Context context){
        this.UserList=UserList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.eachprofile,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseUser parseUser=UserList.get(position);
        holder.bind(parseUser);
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvUserName,tvPersonName;
        private ImageView ivProfilepic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName=itemView.findViewById(R.id.tveachprofileusername);
            ivProfilepic=itemView.findViewById(R.id.iveachprofileprofilepic);
            tvPersonName=itemView.findViewById(R.id.tveachprofilePersonName);
        }

        public void bind(ParseUser parseUser){
            boolean isbusinessprofile= (boolean) parseUser.get("IsBusinessProfile");

            if (isbusinessprofile==true){
                tvUserName.setText((String)parseUser.get("RealName"));
                tvPersonName.setText( (String)parseUser.get("BusinessAddress"));
                String imageurl=(String)(parseUser.get("BusinessImageURL"));

                if (!imageurl.contentEquals("N/A")){
                    Uri uri=Uri.parse(imageurl + context.getResources().getString(R.string.google_maps_key));
                    Glide.with(context).load(uri).circleCrop().into(ivProfilepic);

                }
                else{
                    Glide.with(context).load(parseUser.getParseFile("ProfilePicture").getUrl()).circleCrop().into(ivProfilepic);
                }

            }
            else {
                tvUserName.setText(parseUser.getUsername());
                tvPersonName.setText((String)parseUser.get("RealName"));
                Glide.with(context).load(parseUser.getParseFile("ProfilePicture").getUrl()).circleCrop().into(ivProfilepic);
            }


        }
    }
}
