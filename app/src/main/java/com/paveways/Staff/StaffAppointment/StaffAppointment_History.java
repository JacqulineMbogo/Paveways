package com.paveways.Staff.StaffAppointment;

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
import com.paveways.Users.Users_Home;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.AppointmentHistoryAPI;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffAppointment_History extends AppCompatActivity {
    private String TAG = "Appointment History";
    private RecyclerView recyclerView_appointment;
    private ArrayList<StaffAppointment_Model> Models = new ArrayList<>();
    private StaffAppointment_Adapter adapter;
    SharedPreferenceActivity sharedPreferenceActivity;

    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);


        recyclerView_appointment = (RecyclerView) findViewById(R.id.recycler_appointment_history);
        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_appointment.setLayoutManager(mLayoutManger3);
        recyclerView_appointment.setItemAnimator(new DefaultItemAnimator());

        adapter = new StaffAppointment_Adapter(StaffAppointment_History.this, Models);

        recyclerView_appointment.setAdapter(adapter);
        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);

        getUserAppointmentHistory();

    }

    public void getUserAppointmentHistory() {
        if (!NetworkUtility.isNetworkConnected(StaffAppointment_History.this)) {
            Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();

        } else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AppointmentHistoryAPI> call = service.getAppointmentHistoryCall("1234", "");
            call.enqueue(new Callback<AppointmentHistoryAPI>() {
                @Override
                public void onResponse(Call<AppointmentHistoryAPI> call, Response<AppointmentHistoryAPI> response) {
                    Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {
                            //      Log.e(TAG, "  ss sixe 3 ");
                            Models.clear();

                            if (response.body().getInformation().size() > 0) {

                                for (int i = 0; i < response.body().getInformation().size(); i++) {

                                    Models.add(new StaffAppointment_Model(response.body().getInformation().get(i).getAppointment_id(), response.body().getInformation().get(i).getDate(),
                                            response.body().getInformation().get(i).getTime(), response.body().getInformation().get(i).getStatus(), response.body().getInformation().get(i).getComment(), response.body().getInformation().get(i).getTitle(), response.body().getInformation().get(i).getFullnaame(),response.body().getInformation().get(i).getPhone(),response.body().getInformation().get(i).getEmail()));

                                }
                                adapter.notifyDataSetChanged();
                            }

                        } else {
                            AppUtilits.displayMessage(StaffAppointment_History.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(StaffAppointment_History.this, getString(R.string.network_error));

                    }
                }

                @Override
                public void onFailure(Call<AppointmentHistoryAPI> call, Throwable t) {
                    Log.e(TAG, "  fail- add to cart item " + t.toString());
                    Toast.makeText(getApplicationContext(), "Failed to get order history", Toast.LENGTH_LONG).show();

                }
            });


        }


    }
    @Override
    public void onBackPressed() {


        Intent intent1 = new Intent(StaffAppointment_History.this, StaffProfile.class);

        startActivity(intent1);



    }
}