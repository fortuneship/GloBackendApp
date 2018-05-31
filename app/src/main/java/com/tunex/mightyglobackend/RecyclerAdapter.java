package com.tunex.mightyglobackend;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tunex.mightyglobackend.model.DataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 31-Mar-18.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {


    private ArrayList<DataModel> dataList = new ArrayList<>();


    public RecyclerAdapter(ArrayList<DataModel> dataList){

        this.dataList = dataList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mighty_api_request_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
        //return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {

        DataModel data = dataList.get(position);

        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.orderId.setText(data.getOrderId());
        viewHolder.phoneNumber.setText(data.getPhoneNo());
        viewHolder.quantity.setText(data.getQuantity());
        viewHolder.network.setText(data.getNetwork());
        viewHolder.status.setText(data.getStatus());
        viewHolder.statusCode.setText(String.valueOf(data.getStatusCode()));

//        holder.Network.setText(data.getNetwork());
//        holder.Quantity.setText(data.getQuantity());




    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

       // TextView Network, Quantity;

        TextView orderId;
        TextView phoneNumber;
        TextView quantity;
        TextView network;
        TextView status;
        TextView statusCode;



        public MyViewHolder(View itemView) {
            super(itemView);


            orderId = (TextView) itemView.findViewById(R.id.orderId);
            phoneNumber = (TextView) itemView.findViewById(R.id.phoneNo);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            network = (TextView) itemView.findViewById(R.id.network);
            status = (TextView) itemView.findViewById(R.id.status);
            statusCode = (TextView) itemView.findViewById(R.id.statusCode);



        }
    }
}
