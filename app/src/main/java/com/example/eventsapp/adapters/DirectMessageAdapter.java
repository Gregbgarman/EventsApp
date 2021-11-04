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
import com.example.eventsapp.models.DirectMessage;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class DirectMessageAdapter extends RecyclerView.Adapter<DirectMessageAdapter.ViewHolder> {


    //next-make the recyclerview and make queries
    //before anything, do a query to get other person's profile pic and store user's in a parsefile so it creates the adapter

    private Context context;
    private List<DirectMessage> MsgList;
    private ParseFile OtherPersonProfilePic;
    private ParseFile MyProfilePic;
    private Boolean IsMe;

    public DirectMessageAdapter(Context context,List<DirectMessage> MsgList,ParseFile MyProfilePic,ParseFile OtherPersonProfilePic){
        this.context=context;
        this.MsgList=MsgList;
        this.MyProfilePic=MyProfilePic;
        this.OtherPersonProfilePic=OtherPersonProfilePic;
    }

    @Override
    public int getItemViewType(int position) {
        DirectMessage directMessage=MsgList.get(position);

        String thesender=directMessage.getSender();
        String ME= ParseUser.getCurrentUser().getObjectId();

        if (directMessage.getSender().equals(ParseUser.getCurrentUser().getObjectId())){
            IsMe=true;
            return 1;
        }
        else{
            IsMe=false;
            return 0;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;

        if (viewType==1) {      //is me
            view = LayoutInflater.from(context).inflate(R.layout.eachmessagefromme, parent, false);
        }
        else{       //other person
            view = LayoutInflater.from(context).inflate(R.layout.eachmessagefromotherperson, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(MsgList.get(position));
    }

    @Override
    public int getItemCount() {
        return MsgList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvMessageContent;
        private ImageView ivProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if (IsMe==true) {
                tvMessageContent = itemView.findViewById(R.id.tvMessageMe);
                ivProfilePic = itemView.findViewById(R.id.ivMessageMe);
            }
            else{
                tvMessageContent = itemView.findViewById(R.id.tvMessageOtherPerson);
                ivProfilePic = itemView.findViewById(R.id.ivMessageOtherUserPhoto);
            }

        }
        public void bind(DirectMessage directMessage){
                tvMessageContent.setText(directMessage.getMessage());
                if (IsMe==true){
                    Glide.with(context).load(MyProfilePic.getUrl()).circleCrop().into(ivProfilePic);

                }
                else{
                    Glide.with(context).load(OtherPersonProfilePic.getUrl()).circleCrop().into(ivProfilePic);

                }

        }
    }



}
