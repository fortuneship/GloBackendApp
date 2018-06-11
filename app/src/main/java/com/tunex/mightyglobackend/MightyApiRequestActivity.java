package com.tunex.mightyglobackend;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;


import com.tunex.mightyglobackend.model.DataModel;
import com.tunex.mightyglobackend.model.Feed;
import com.tunex.mightyglobackend.utilities.SendNotification;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MightyApiRequestActivity extends AppCompatActivity {

    //app id
    //1:244644554929:android:607538841aef1396

    private static final String TAG = MightyApiRequestActivity.class.getSimpleName();

    Toolbar toolbar;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;

    private MightyApi mightyApi;
    private ArrayList<DataModel> dataList = new ArrayList<>();

    private Handler handler;


    int count;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mighty_api_request);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

//        PushNotifications.start(getApplicationContext(), "cdf6849c-d5cf-4a82-8be4-fe8ab3319047");
//        PushNotifications.subscribe("hello");
//
//        if (getIntent().getStringExtra("isMyPushNotification") != null) {
//            // This means that the onCreate is the result of a notification being opened
//            Log.i("MyActivity", "Notification incoming!");
//        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // set up the RecyclerView
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        int numberOfColumns = 4;
//        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
//        adapter = new MyRecyclerViewAdapter(this, data);
//        adapter.setClickListener(this);
//        recyclerView.setAdapter(adapter);



        fetchFromApi();


        handler = new Handler();
        handler.postDelayed(runnable, 200000);

        progressDialog = new ProgressDialog(MightyApiRequestActivity.this);
        progressDialog.setMessage("Its loading....");
        progressDialog.show();


    }

    /**
     * check balance every 5 minutes
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            fetchFromApi();

            // send notification when request comes from api
//            SendNotification.notification(MightyApiRequestActivity.this, getResources().getString(R.string.device_notification_api_title),
//                    getResources().getString(R.string.device_notification_api_message));

            SendNotification.notification(MightyApiRequestActivity.this, getResources().getString(R.string.device_notification_api_title),
                    getResources().getString(R.string.device_notification_api_message));


      /* and here comes the "trick" */
            handler.postDelayed(this, 200000);
        }
    };


    public void fetchFromApi(){

        mightyApi = ApiClient.getApiClient().create(MightyApi.class);

        Call<Feed> call = mightyApi.getData();

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {

                progressDialog.dismiss();
                Log.i("api", "onResponse: Server Response: " + response.toString());

                Log.d("api", "onResponse: received information: " + response.body().toString());


                dataList = response.body().getData();

                adapter = new RecyclerAdapter(dataList,MightyApiRequestActivity.this);
                //adapter.updateDataModelListItems(dataList);
                recyclerView.setAdapter(adapter);


//                 count = 0;
//                if (adapter != null) {
//                    count = adapter.getItemCount();
//
//                    Log.i("apiCount", String.valueOf(count));
//                }


//                ArrayList<DataModel> datamodelList = response.body().getData();
//
                for (int i = 0; i < dataList.size(); i++){

                    Log.i("api", "onResponse: \n" +
                            "orderId: " + dataList.get(i).getOrderId()+ "\n" +
                            "phoneNo: " + dataList.get(i).getPhoneNo()+ "\n" +
                            "quantity: " + dataList.get(i).getQuantity()+ "\n" +
                            "network: " + dataList.get(i).getNetwork()+ "\n" +
                            "status: " + dataList.get(i).getStatus()+ "\n" +
                            "statusCode: " + dataList.get(i).getStatusCode());

                }

            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

                Log.i("apiInfo", "onFailure :Something went wrong: " + t.getMessage());

                Toast.makeText(MightyApiRequestActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

            }
        });

    }

//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, new PushNotificationReceivedListener() {
//            @Override
//            public void onMessageReceived(RemoteMessage remoteMessage) {
//                String messagePayload = remoteMessage.getData().get("myMessagePayload");
//                if (messagePayload == null) {
//                    // Message payload was not set for this notification
//                    Log.i("MyActivity", "Payload was missing");
//                } else {
//                    Log.i("MyActivity", messagePayload);
//                    // Now update the UI based on your message payload!
//                }
//            }
//        });
//    }
}
