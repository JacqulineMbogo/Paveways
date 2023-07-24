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
import com.paveways.Home.HomeActivity;
import com.paveways.MyAccount.OrderHistory;
import com.paveways.R;
import com.paveways.StaffProfile;
import com.paveways.Tenant.TenantHome;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.ServicesListAPI;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesHome extends AppCompatActivity {

    String TAG = "ServicesHomeActivity";
    private RecyclerView recyclerView_services;
    private ArrayList<Services_Model> Models = new ArrayList<>();
    private Services_Adapter adapter;
    SharedPreferenceActivity sharedPreferenceActivity;

    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_home);

        recyclerView_services = (RecyclerView) findViewById(R.id.recycler_services);
        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_services.setLayoutManager(mLayoutManger3);
        recyclerView_services.setItemAnimator(new DefaultItemAnimator());

        adapter = new Services_Adapter(ServicesHome.this, Models);

        recyclerView_services.setAdapter(adapter);
        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);

        getServicesList();

    }

    private void getServicesList() {
        if (!NetworkUtility.isNetworkConnected(ServicesHome.this)) {
            Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();

        } else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<ServicesListAPI> call = service.getServiceListCall("1234");
            call.enqueue(new Callback<ServicesListAPI>() {
                @Override
                public void onResponse(Call<ServicesListAPI> call, Response<ServicesListAPI> response) {
                    Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {
                            //      Log.e(TAG, "  ss sixe 3 ");
                            Models.clear();

                            if (response.body().getInformation().size() > 0) {

                                for (int i = 0; i < response.body().getInformation().size(); i++) {

                                    Models.add(new Services_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(), response.body().getInformation().get(i).getDescription()));

                                }
                                adapter.notifyDataSetChanged();
                            }

                        } else {
                            AppUtilits.displayMessage(ServicesHome.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(ServicesHome.this, getString(R.string.network_error));

                    }
                }

                @Override
                public void onFailure(Call<ServicesListAPI> call, Throwable t) {
                    Log.e(TAG, "  fail- add to cart item " + t.toString());
                    Toast.makeText(getApplicationContext(), "Failed to get order history", Toast.LENGTH_LONG).show();

                }
            });


        }


    }

    @Override
    public void onBackPressed() {

        if (!sharedPreferenceActivity.getItem(Constant.DEPARTMENT).isEmpty()) {
            Intent intent1 = new Intent(ServicesHome.this, StaffProfile.class);

            startActivity(intent1);

        } else {

            Intent intent1 = new Intent(ServicesHome.this, TenantHome.class);

            startActivity(intent1);
        }


    }
}

