package com.paveways.Users;

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
import com.paveways.Appointment.Appointment_Adapter;
import com.paveways.Appointment.Appointment_History;
import com.paveways.Appointment.Appointment_Model;
import com.paveways.R;
import com.paveways.Staff.Auth.StaffLogin;
import com.paveways.StaffProfile;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.UserDetails;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Users_Home extends AppCompatActivity {
    private String TAG = "Users History";
    private RecyclerView recyclerView_appointment;
    private ArrayList<Users_Model> Models = new ArrayList<>();
    private Users_Adapter adapter;
    SharedPreferenceActivity sharedPreferenceActivity;

    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);


        recyclerView_appointment = (RecyclerView) findViewById(R.id.recycler_appointment_history);
        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_appointment.setLayoutManager(mLayoutManger3);
        recyclerView_appointment.setItemAnimator(new DefaultItemAnimator());

        adapter = new Users_Adapter(Users_Home.this, Models);

        recyclerView_appointment.setAdapter(adapter);
        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);

        getUserDetails();

    }



    public void getUserDetails() {
        if (!NetworkUtility.isNetworkConnected(Users_Home.this)) {
            Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();

        } else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<UserDetails> call = service.getuserDetailsHistoryCall("1234");
            call.enqueue(new Callback<UserDetails>() {
                @Override
                public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                    Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {
                            //      Log.e(TAG, "  ss sixe 3 ");
                            Models.clear();

                            if (response.body().getInformation().size() > 0) {

                                for (int i = 0; i < response.body().getInformation().size(); i++) {

                                    Models.add(new Users_Model(response.body().getInformation().get(i).getUserId(),response.body().getInformation().get(i).getFullname(),response.body().getInformation().get(i).getUname(),response.body().getInformation().get(i).getEmail(),response.body().getInformation().get(i).getPhone(),response.body().getInformation().get(i).getDate(),response.body().getInformation().get(i).getStatus(),response.body().getInformation().get(i).getComment()));

                                }
                                adapter.notifyDataSetChanged();
                            }

                        } else {
                            AppUtilits.displayMessage(Users_Home.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(Users_Home.this, getString(R.string.network_error));

                    }
                }

                @Override
                public void onFailure(Call<UserDetails> call, Throwable t) {
                    Log.e(TAG, "  fail- add to cart item " + t.toString());
                    Toast.makeText(getApplicationContext(), "Failed to get order history", Toast.LENGTH_LONG).show();

                }
            });


        }


    }
    @Override
    public void onBackPressed() {


        Intent intent1 = new Intent(Users_Home.this, StaffProfile.class);

        startActivity(intent1);



    }
}
