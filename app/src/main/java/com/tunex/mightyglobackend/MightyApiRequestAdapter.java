//package com.tunex.mightyglobackend;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//
//
//import com.tunex.mightyglobackend.model.DataModel;
//
//import java.util.ArrayList;
//
//
//
///**
// * Created by MIGHTY5 on 5/30/2018.
// */
//
//public class MightyApiRequestAdapter extends ArrayAdapter<DataModel>{
//
//
//    public MightyApiRequestAdapter(Context context, ArrayList<DataModel> dataModels) {
//        super(context, R.layout.list_mighty_api_request_item);
//    }
//
//    //View lookup cache
//    private static class ViewHolder {
//        TextView orderId;
//        TextView phoneNumber;
//        TextView quantity;
//        TextView network;
//        TextView status;
//        TextView statusCode;
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        DataModel data = getItem(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        ViewHolder viewHolder; // view lookup cache stored in tag
//        if (convertView == null) {
//            // If there's no view to re-use, inflate a brand new view for row
//            viewHolder = new ViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.list_mighty_api_request_item, parent, false);
//            viewHolder.orderId = (TextView) convertView.findViewById(R.id.orderId);
//            viewHolder.phoneNumber = (TextView) convertView.findViewById(R.id.phoneNo);
//            viewHolder.quantity = (TextView) convertView.findViewById(R.id.quantity);
//            viewHolder.network = (TextView) convertView.findViewById(R.id.network);
//            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
//            viewHolder.statusCode = (TextView) convertView.findViewById(R.id.statusCode);
//            // Cache the viewHolder object inside the fresh view
//            convertView.setTag(viewHolder);
//        } else {
//            // View is being recycled, retrieve the viewHolder object from tag
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//
//        // Populate the data from the data object via the viewHolder object
//        // into the template view.
//        viewHolder.orderId.setText(data.getOrderId());
//        viewHolder.phoneNumber.setText(data.getPhoneNo());
//        viewHolder.quantity.setText(data.getQuantity());
//        viewHolder.network.setText(data.getNetwork());
//        viewHolder.status.setText(data.getStatus());
//        viewHolder.statusCode.setText(data.getStatusCode());
//        // Return the completed view to render on screen
//        return convertView;
//    }
//
//
//}
//
//
//
//
////