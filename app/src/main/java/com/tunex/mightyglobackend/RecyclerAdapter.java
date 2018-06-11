package com.tunex.mightyglobackend;



import android.content.Context;
import android.content.Intent;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tunex.mightyglobackend.model.DataModel;
//import com.tunex.mightyglobackend.utilities.ApiDiffCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 31-Mar-18.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {


    private ArrayList<DataModel> dataList = new ArrayList<>();
    Context ctx;


    public RecyclerAdapter(ArrayList<DataModel> dataList, Context ctx){

        this.dataList = dataList;
        this.ctx = ctx;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_api_request, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, ctx, dataList);
        return myViewHolder;
        //return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {

        DataModel data = dataList.get(position);
//
//        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.orderId.setText(data.getOrderId());
        viewHolder.phoneNumber.setText(data.getPhoneNo());
        viewHolder.quantity.setText(data.getQuantity());
        viewHolder.network.setText(data.getNetwork());
        viewHolder.status.setText(data.getStatus());
        viewHolder.statusCode.setText(String.valueOf(data.getStatusCode()));

//        holder.Network.setText(data.getNetwork());
//        holder.Quantity.setText(data.getQuantity());

//        if (payloads.isEmpty()) {
//            onBindViewHolder(viewHolder, position);
//        } else {
//            DataModel data = dataList.get(position);
//
//            for (Object object : payloads) {
//
//            }
//        }



    }

    @Override
    public int getItemCount() {
        return dataList.size();

    }


//    public void updateDataModelListItems(List<DataModel> newDataModels) {
//        final ApiDiffCallback diffCallback = new ApiDiffCallback(newDataModels, dataList);
//        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
//
//        this.dataList.clear();
//        this.dataList.addAll(newDataModels);
//        diffResult.dispatchUpdatesTo(this);
//    }




    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        // TextView Network, Quantity;

        TextView orderId;
        TextView phoneNumber;
        TextView quantity;
        TextView network;
        TextView status;
        TextView statusCode;


        ArrayList<DataModel> dataList = new ArrayList<DataModel>();
        Context ctx;




        public MyViewHolder(View itemView, Context ctx,  ArrayList<DataModel> dataList) {
            super(itemView);

            this.dataList = dataList;

            this.ctx = ctx;

            itemView.setOnClickListener(this);


            orderId = (TextView) itemView.findViewById(R.id.orderId);
            phoneNumber = (TextView) itemView.findViewById(R.id.phoneNo);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            network = (TextView) itemView.findViewById(R.id.network);
            status = (TextView) itemView.findViewById(R.id.status);
            statusCode = (TextView) itemView.findViewById(R.id.statusCode);



        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            DataModel dataModel = this.dataList.get(position);

            // Toast.makeText(this.ctx, "item " + dataModel, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this.ctx, MainActivity.class);


            intent.putExtra("mOrderId", dataModel.getOrderId());
            intent.putExtra("mPhoneNumber", dataModel.getPhoneNo());
            intent.putExtra("mQuantity", dataModel.getQuantity());
            intent.putExtra("mNetwork", dataModel.getNetwork());
            intent.putExtra("mStatus", dataModel.getStatus());
            intent.putExtra("mStatusCode", dataModel.getStatusCode());

            this.ctx.startActivity(intent);


        }
    }
}





//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.tunex.mightyglobackend.model.DataModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by hp on 31-Mar-18.
// */
//
//public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
//
//
//    private ArrayList<DataModel> dataList = new ArrayList<>();
//
//
//    public RecyclerAdapter(ArrayList<DataModel> dataList){
//
//        this.dataList = dataList;
//
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mighty_api_request_item, parent, false);
//        MyViewHolder myViewHolder = new MyViewHolder(view);
//        return myViewHolder;
//        //return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
//
//        DataModel data = dataList.get(position);
//
//        // Populate the data from the data object via the viewHolder object
//        // into the template view.
//        viewHolder.orderId.setText(data.getOrderId());
//        viewHolder.phoneNumber.setText(data.getPhoneNo());
//        viewHolder.quantity.setText(data.getQuantity());
//        viewHolder.network.setText(data.getNetwork());
//        viewHolder.status.setText(data.getStatus());
//        viewHolder.statusCode.setText(String.valueOf(data.getStatusCode()));
//
////        holder.Network.setText(data.getNetwork());
////        holder.Quantity.setText(data.getQuantity());
//
//
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return dataList.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder{
//
//       // TextView Network, Quantity;
//
//        TextView orderId;
//        TextView phoneNumber;
//        TextView quantity;
//        TextView network;
//        TextView status;
//        TextView statusCode;
//
//
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//
//
//            orderId = (TextView) itemView.findViewById(R.id.orderId);
//            phoneNumber = (TextView) itemView.findViewById(R.id.phoneNo);
//            quantity = (TextView) itemView.findViewById(R.id.quantity);
//            network = (TextView) itemView.findViewById(R.id.network);
//            status = (TextView) itemView.findViewById(R.id.status);
//            statusCode = (TextView) itemView.findViewById(R.id.statusCode);
//
//
//
//        }
//    }
//}
