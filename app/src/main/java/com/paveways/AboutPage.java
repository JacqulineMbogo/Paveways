package com.paveways;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.AboutResponse;
import com.paveways.WebServices.ServiceWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutPage extends AppCompatActivity {
    SharedPreferenceActivity sharedPreferenceActivity;
    private String TAG ="About";
    private TextView about_us;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        about_us = findViewById(R.id.about);

        getAbout();

    }

    private void getAbout() {

        if (!NetworkUtility.isNetworkConnected(AboutPage.this)){
            AppUtilits.displayMessage(AboutPage.this,  getString(R.string.network_not_connected));


        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AboutResponse> call = service.AboutResponseCall("1234");
            call.enqueue(new Callback<AboutResponse>() {
                @Override
                public void onResponse(Call<AboutResponse> call, Response<AboutResponse> response) {
                    Log.e(TAG, "About response is "+ response.body().getInformation().toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1) {

                            about_us.setText(response.body().getInformation());

                        }else {

                            Toast.makeText(getApplicationContext(),"No Categories found",Toast.LENGTH_LONG).show();
                        }


                    }else {
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<AboutResponse> call, Throwable t) {

                }

            });
        }
    }
}
