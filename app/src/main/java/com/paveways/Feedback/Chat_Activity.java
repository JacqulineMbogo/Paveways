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

import com.paveways.Home.HomeActivity;
import com.paveways.R;
import com.paveways.StaffProfile;
import com.paveways.Tenant.TenantHome;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.StaffResponse;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat_Activity extends AppCompatActivity {

    private String TAG = "chat_activity";
    SharedPreferenceActivity sharedPreferenceActivity;
    Context context;
    String securecode;
    private RecyclerView recyclerView_chat;
    private ArrayList<Staff_Model> StaffModelList = new ArrayList<>();
    private Staff_Adapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);

        recyclerView_chat = (RecyclerView) findViewById(R.id.recycler_chat);
        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
        recyclerView_chat.setLayoutManager(mLayoutManger3);
        recyclerView_chat.setItemAnimator(new DefaultItemAnimator());

        adapter = new Staff_Adapter( Chat_Activity.this, StaffModelList);

        recyclerView_chat.setAdapter(adapter);

        if(sharedPreferenceActivity.getItem(Constant.DEPARTMENT).isEmpty()) {
            securecode="3456";
        }else{
            securecode="1234";
        }

        getUsers(securecode);
    }

    private void getUsers(String securecod) {

        if (!NetworkUtility.isNetworkConnected(Chat_Activity.this)) {
            AppUtilits.displayMessage(Chat_Activity.this, getString(R.string.network_not_connected));


        } else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<StaffResponse> call = service.StaffResponseCall(securecod);
            call.enqueue(new Callback<StaffResponse>() {
                @Override
                public void onResponse(Call<StaffResponse> call, Response<StaffResponse> response) {
                    Log.e(TAG, "Categories response is " + response.body().getInformation().toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().size() > 0) {

                                StaffModelList.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {

                                    StaffModelList.add(new Staff_Model(response.body().getInformation().get(i).getStaffId(), response.body().getInformation().get(i).getDepartment(),response.body().getInformation().get(i).getName()));

                                }
                                adapter.notifyDataSetChanged();
                            }
                        } else {

                            Toast.makeText(getApplicationContext(), "No staff found", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<StaffResponse> call, Throwable t) {

                }

            });
        }
    }
    @Override
    public void onBackPressed() {


        if (sharedPreferenceActivity.getItem(Constant.DEPARTMENT).isEmpty()) {
            if(sharedPreferenceActivity.getItem(Constant.COMMENT).isEmpty()) {
                Intent intent1 = new Intent(Chat_Activity.this, HomeActivity.class);

                startActivity(intent1);
            }else{
                Intent intent1 = new Intent(Chat_Activity.this, TenantHome.class);

                startActivity(intent1);
            }
        } else {
            Intent intent1 = new Intent(Chat_Activity.this, StaffProfile.class);

            startActivity(intent1);

        }


    }
}
