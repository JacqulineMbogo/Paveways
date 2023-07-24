package com.paveways.Tenant.Services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.paveways.R;
import com.paveways.StaffProfile;
import com.paveways.Tenant.TenantHome;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.ServiceRequestsAPI;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Service_Requests_History extends AppCompatActivity {
    private String TAG = "Service Requests History";
    private RecyclerView recyclerView_service_requests;
    private ArrayList<Service_Requests_Model> Models = new ArrayList<>();
    private Service_Requests_Adapter adapter;
    SharedPreferenceActivity sharedPreferenceActivity;

    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_requests_history);


        recyclerView_service_requests = (RecyclerView) findViewById(R.id.recycler_service_requests_history);
        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_service_requests.setLayoutManager(mLayoutManger3);
        recyclerView_service_requests.setItemAnimator(new DefaultItemAnimator());

        adapter = new Service_Requests_Adapter(Service_Requests_History.this, Models);

        recyclerView_service_requests.setAdapter(adapter);
        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);

        if(sharedPreferenceActivity.getItem(Constant.DEPARTMENT).isEmpty()) {
            getServiceRequestsHistory("1234",sharedPreferenceActivity.getItem(Constant.USER_DATA));
        }else{
            getServiceRequestsHistory("1234","");
        }

    }

    private void getServiceRequestsHistory(String securecode, String user) {
        if (!NetworkUtility.isNetworkConnected(Service_Requests_History.this)) {
            Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();

        } else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<ServiceRequestsAPI> call = service.getServiceRequestsHistoryCall(securecode, user);
                call.enqueue(new Callback<ServiceRequestsAPI>() {
                @Override
                public void onResponse(Call<ServiceRequestsAPI> call, Response<ServiceRequestsAPI> response) {
                    Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {
                            //      Log.e(TAG, "  ss sixe 3 ");
                            Models.clear();

                            if (response.body().getInformation().size() > 0) {

                                for (int i = 0; i < response.body().getInformation().size(); i++) {

                                    Models.add(new Service_Requests_Model(response.body().getInformation().get(i).getRequestId(),response.body().getInformation().get(i).getServiceName(),response.body().getInformation().get(i).getStatus(),response.body().getInformation().get(i).getStaffComments(),response.body().getInformation().get(i).getComments(),response.body().getInformation().get(i).getFullname(),response.body().getInformation().get(i).getUemail(),response.body().getInformation().get(i).getUphone(),response.body().getInformation().get(i).getTotalAmount(),response.body().getInformation().get(i).getCreateDate(),response.body().getInformation().get(i).getUpdateDate()));

                                }
                                adapter.notifyDataSetChanged();
                            }

                        } else {
                            AppUtilits.displayMessage(Service_Requests_History.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(Service_Requests_History.this, getString(R.string.network_error));

                    }
                }

                @Override
                public void onFailure(Call<ServiceRequestsAPI> call, Throwable t) {
                    Log.e(TAG, "  fail- fetch requested service" + t.toString());
                    Toast.makeText(getApplicationContext(), "Failed to fetch requested service", Toast.LENGTH_LONG).show();

                }
            });


        }


    }
    @Override
    public void onBackPressed() {

        if (!sharedPreferenceActivity.getItem(Constant.DEPARTMENT).isEmpty()) {
            Intent intent1 = new Intent(Service_Requests_History.this, StaffProfile.class);

            startActivity(intent1);

        } else {

            Intent intent1 = new Intent(Service_Requests_History.this, TenantHome.class);

            startActivity(intent1);
        }


    }
}
