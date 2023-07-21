package com.paveways.Appointment;

import android.content.Context;
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

public class Appointment_History  extends AppCompatActivity {
    private String TAG = "Appointment History";
    private RecyclerView recyclerView_appointment;
    private ArrayList<Appointment_Model> Models = new ArrayList<>();
    private Appointment_Adapter adapter;
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

        adapter = new Appointment_Adapter(Appointment_History.this, Models);

        recyclerView_appointment.setAdapter(adapter);
        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);

        getUserAppointmentHistory();

    }

    public void getUserAppointmentHistory() {
        if (!NetworkUtility.isNetworkConnected(Appointment_History.this)) {
            Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();

        } else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AppointmentHistoryAPI> call = service.getAppointmentHistoryCall("1234", sharedPreferenceActivity.getItem(Constant.USER_DATA));
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

                                    Models.add(new Appointment_Model(response.body().getInformation().get(i).getAppointment_id(), response.body().getInformation().get(i).getDate(),
                                            response.body().getInformation().get(i).getTime(), response.body().getInformation().get(i).getStatus(), response.body().getInformation().get(i).getComment(), response.body().getInformation().get(i).getTitle(),response.body().getInformation().get(i).getFullnaame(),response.body().getInformation().get(i).getEmail(),response.body().getInformation().get(i).getPhone()));

                                }
                                adapter.notifyDataSetChanged();
                            }

                        } else {
                            AppUtilits.displayMessage(Appointment_History.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(Appointment_History.this, getString(R.string.network_error));

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
}