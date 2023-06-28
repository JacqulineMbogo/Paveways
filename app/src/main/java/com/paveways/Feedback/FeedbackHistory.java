package com.paveways.Feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.paveways.Home.HomeActivity;
import com.paveways.R;
import com.paveways.StaffProfile;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.feedhistoryAPI;
import com.paveways.WebServices.ServiceWrapper;
import com.paveways.cart.CartDetails;
import com.paveways.cart.Order_Summary;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeedbackHistory extends AppCompatActivity {

    private String TAG = "feedhistory";
    SharedPreferenceActivity sharedPreferenceActivity;
    Context context;
    private String securecode;
    private TextView feednew;
    private RecyclerView recyclerView_order;
    private ArrayList<feedhistory_model> Models = new ArrayList<>();
    private feedhistory_Adapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbackhistory);

        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);
        feednew = findViewById(R.id.newfeedback);


        recyclerView_order = (RecyclerView) findViewById(R.id.recycler_orderhistory);
        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
        recyclerView_order.setLayoutManager(mLayoutManger3);
        recyclerView_order.setItemAnimator(new DefaultItemAnimator());

        adapter = new feedhistory_Adapter( FeedbackHistory.this, Models);

        recyclerView_order.setAdapter(adapter);

        if(!sharedPreferenceActivity.getItem(Constant.DEPARTMENT).isEmpty()){
            securecode = "00";
        }else{
            securecode = "1";
        }
        getUserFeedHistory(securecode);


        feednew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedbackHistory.this, feedback.class);
                startActivity(intent);
            }
        });
    }

    public void getUserFeedHistory(String securecode){
        if (!NetworkUtility.isNetworkConnected(FeedbackHistory.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();

        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<feedhistoryAPI> call = service.getfeedhistorycall(securecode, sharedPreferenceActivity.getItem(Constant.USER_DATA));
            call.enqueue(new Callback<feedhistoryAPI>() {
                @Override
                public void onResponse(Call<feedhistoryAPI> call, Response<feedhistoryAPI> response) {
                    Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {
                            //      Log.e(TAG, "  ss sixe 3 ");
                            Models.clear();

                            if (response.body().getInformation().size()>0){

                                for (int i =0; i<response.body().getInformation().size(); i++){

                                    Models.add(  new feedhistory_model(response.body().getInformation().get(i).getComment(),response.body().getInformation().get(i).getReply(),response.body().getInformation().get(i).getCommentdate(),
                                          response.body().getInformation().get(i).getReplydate(), response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getStaff()));



                                }
                                adapter.notifyDataSetChanged();
                            }

                        } else {
                            AppUtilits.displayMessage(FeedbackHistory.this, response.body().getMsg());

                        }

                    }
                }
                @Override
                public void onFailure(Call<feedhistoryAPI> call, Throwable t) {
                    Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    Toast.makeText(getApplicationContext(),"Failed to get order history",Toast.LENGTH_LONG).show();

                }
            });


        }


    }
    @Override
    public void onBackPressed() {


        if(sharedPreferenceActivity.getItem(Constant.DEPARTMENT).isEmpty()){
            Intent intent1 = new Intent(FeedbackHistory.this, HomeActivity.class);

            startActivity(intent1);
        }else {
            Intent intent1 = new Intent(FeedbackHistory.this, StaffProfile.class);

            startActivity(intent1);

        }




    }

}
