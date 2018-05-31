package com.tunex.mightyglobackend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.tunex.mightyglobackend.model.DataModel;
import com.tunex.mightyglobackend.model.Feed;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MightyApiRequestActivity extends AppCompatActivity {

    private static final String TAG = MightyApiRequestActivity.class.getSimpleName();

    Toolbar toolbar;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;

    private MightyApi mightyApi;
    private ArrayList<DataModel> dataList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mighty_api_request);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);

        // set up the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
//        adapter = new MyRecyclerViewAdapter(this, data);
//        adapter.setClickListener(this);
//        recyclerView.setAdapter(adapter);



        mightyApi = ApiClient.getApiClient().create(MightyApi.class);

        Call<Feed> call = mightyApi.getData();

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {

                Log.i("api", "onResponse: Server Response: " + response.toString());

                Log.d("api", "onResponse: received information: " + response.body().toString());


                dataList = response.body().getData();

                adapter = new RecyclerAdapter(dataList);
                recyclerView.setAdapter(adapter);

//                ArrayList<DataModel> datamodelList = response.body().getData();
//
//                for (int i = 0; i < datamodelList.size(); i++){
//
//                    Log.i("api", "onResponse: \n" +
//                            "orderId: " + datamodelList.get(i).getOrderId()+ "\n" +
//                            "phoneNo: " + datamodelList.get(i).getPhoneNo()+ "\n" +
//                            "quantity: " + datamodelList.get(i).getQuantity()+ "\n" +
//                            "network: " + datamodelList.get(i).getNetwork()+ "\n" +
//                            "status: " + datamodelList.get(i).getStatus()+ "\n" +
//                            "statusCode: " + datamodelList.get(i).getStatusCode());
//
//                }

            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

                Log.i("apiInfo", "onFailure :Something went wrong: " + t.getMessage());

                Toast.makeText(MightyApiRequestActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

            }
        });


    }
}
