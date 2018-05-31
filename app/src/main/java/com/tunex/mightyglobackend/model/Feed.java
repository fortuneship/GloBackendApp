package com.tunex.mightyglobackend.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by MIGHTY5 on 5/31/2018.
 */

public class Feed {

    //Feed class matches JSON data
    @SerializedName("data")
    @Expose
    private ArrayList<DataModel> data;

//    public ArrayList<DataModel> getData() {
//        return data;
//    }


    public ArrayList<DataModel> getData() {
        return data;
    }

    public void setData(ArrayList<DataModel> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "data=" + data +
                '}';
    }
}
