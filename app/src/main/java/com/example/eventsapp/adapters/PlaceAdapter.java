package com.example.eventsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.eventsapp.R;
import com.example.eventsapp.models.PlaceResult;

import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private Context context;
    private List<PlaceResult> ResultList;
    private GetLocationInterface getLocationInterface;

    public interface GetLocationInterface{
        public void GetLocation(PlaceResult placeResult);
    }

    public PlaceAdapter(Context context,List<PlaceResult> ResultList, GetLocationInterface getLocationInterface){
        this.context=context;
        this.ResultList=ResultList;
        this.getLocationInterface=getLocationInterface;
    }

    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.eachplace,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, int position) {
        PlaceResult placeResult=ResultList.get(position);
        holder.bind(placeResult);
    }

    @Override
    public int getItemCount() {
        return ResultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName,tvAddress;
        private ConstraintLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvPlaceName);
            tvAddress=itemView.findViewById(R.id.tvPlaceAddress);
            container=itemView.findViewById(R.id.EachPlaceContainer);
        }

        public void bind(PlaceResult placeResult){
            tvName.setText(placeResult.getName());
            tvAddress.setText(placeResult.getAddress());

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        getLocationInterface.GetLocation(placeResult);
                    }catch (Exception e){

                    }
                }
            });
        }


    }
}
