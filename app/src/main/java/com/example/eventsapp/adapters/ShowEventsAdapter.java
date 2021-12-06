package com.example.eventsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventsapp.R;
import com.example.eventsapp.models.Events;
import com.example.eventsapp.models.PlaceResult;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ShowEventsAdapter extends RecyclerView.Adapter<ShowEventsAdapter.ViewHolder> {

    private Context context;
    private List<Events> EventList;

    public ShowEventsAdapter(Context context, List<Events> EventList){
        this.context=context;
        this.EventList=EventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.eachevent,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Events events=EventList.get(position);
        holder.bind(events);
    }

    @Override
    public int getItemCount() {
        return EventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvEventName,tvUserName, tvEventTime,tvEventDate;
        private ImageView ivProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventName=itemView.findViewById(R.id.tveeEventName);
            tvUserName=itemView.findViewById(R.id.tveeCreatedBy);
            tvEventTime=itemView.findViewById(R.id.tvEETime);
            tvEventDate=itemView.findViewById(R.id.tvEEDate);
            ivProfilePic=itemView.findViewById(R.id.ivEEProfilePic);
        }

        public void bind(Events event){
            tvEventName.setText(event.GetEventName());
            tvEventDate.setText(event.GetEventDate());
            tvEventTime.setText(event.GetEventTime());
            ParseUser user=event.GetEventCreator();
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereContains("objectId",user.getObjectId());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> TheUser, ParseException e) {
                       ParseUser User=TheUser.get(0);
                       tvUserName.setText(User.getUsername());
                    Glide.with(context).load(User.getParseFile("ProfilePicture").getUrl()).circleCrop().into(ivProfilePic);

                }
            });



        }
    }
}
